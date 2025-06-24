package dev.mayutama.smartbook.service.impl;

import dev.mayutama.smartbook.common.repository.BaseRepository;
import dev.mayutama.smartbook.common.rest.RestFilter;
import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.common.service.EngineFilterService;
import dev.mayutama.smartbook.exception.ApplicationException;
import dev.mayutama.smartbook.model.dto.request.review.ReviewReq;
import dev.mayutama.smartbook.model.dto.response.book.BookRes;
import dev.mayutama.smartbook.model.dto.response.dashboard.ActiveUserStatsRes;
import dev.mayutama.smartbook.model.dto.response.genre.GenreRes;
import dev.mayutama.smartbook.model.dto.response.review.ReviewRes;
import dev.mayutama.smartbook.model.entity.*;
import dev.mayutama.smartbook.model.mapper.ReviewMapper;
import dev.mayutama.smartbook.model.projection.ActiveUserStats;
import dev.mayutama.smartbook.repository.ReviewRepository;
import dev.mayutama.smartbook.service.BookService;
import dev.mayutama.smartbook.service.ReviewService;
import dev.mayutama.smartbook.service.UserCredentialService;
import dev.mayutama.smartbook.service.UserService;
import dev.mayutama.smartbook.util.SentimentAnalyzer;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl
    extends EngineFilterService<Review, String>
    implements ReviewService
{
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final BookService bookService;
    private final UserCredentialService userCredentialService;

    @Override
    public Iterable<ReviewRes> findAll(RestParamRequest paramRequest) {
        Specification<Review> specification = createFilter(paramRequest);
        Iterable<Review> listIterable = doFindAllFiltered(paramRequest, specification);
        if (listIterable instanceof Page<?>) {
            Page<Review> page = (Page<Review>) listIterable;
            List<ReviewRes> dtoList = page.getContent().stream()
                    .map(reviewMapper::toRes)
                    .toList();
            return new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
        } else {
            List<Review> list = (List<Review>) listIterable;
            return list.stream()
                    .map(reviewMapper::toRes)
                    .toList();
        }
    }

    @Override
    public ReviewRes findById(String id) {
        Review review = reviewRepository.findById(id).orElseThrow(
            () -> new ApplicationException(null, "Review not found", HttpStatus.NOT_FOUND)
        );

        if (!review.getIsActive()) {
            throw new ApplicationException(null, "Review not found", HttpStatus.NOT_FOUND);
        }

        return reviewMapper.toRes(review);
    }

    @Override
    @Transactional
    public ReviewRes create(ReviewReq req) {
        UserCredential userCredential = getUserCredential();

        if (userCredential == null) {
            throw new ApplicationException(null, "UserCredential is invalid", HttpStatus.BAD_REQUEST);
        }

        Book book = bookService.findEntityById(req.getBookId());
        Optional<Review> currReview = reviewRepository.findByBookIdAndUserId(book.getId(), userCredential.getUser().getId());
        if (currReview.isPresent()) {
            throw new ApplicationException(null, "User has add comment in this book", HttpStatus.BAD_REQUEST);
        }

        Review review = reviewMapper.toEntity(req, book, userCredential.getUser());
        double sentimentScore = SentimentAnalyzer.analyze(req.getComment());
        review.setSentimentScore(sentimentScore);
        review.setIsVerified(sentimentScore >= 0.0);
        reviewRepository.save(review);
        return reviewMapper.toRes(review);
    }

    @Override
    public ReviewRes update(ReviewReq req, String id) {
        UserCredential userCredential = getUserCredential();

        if (userCredential == null) {
            throw new ApplicationException(null, "UserCredential is invalid", HttpStatus.BAD_REQUEST);
        }

        Review review = reviewRepository.findById(id).orElseThrow(
            () -> new ApplicationException(null, "Review not found", HttpStatus.NOT_FOUND)
        );

        if (req.getRating() != null)
            review.setRating(req.getRating());
        if (!req.getComment().isEmpty())
            review.setComment(req.getComment());

        double sentimentScore = SentimentAnalyzer.analyze(req.getComment());
        review.setSentimentScore(sentimentScore);
        review.setIsVerified(sentimentScore >= 0.0);
        reviewRepository.save(review);
        return reviewMapper.toRes(review);
    }

    @Override
    public void delete(String id) {
        Review review = reviewRepository.findById(id).orElseThrow(
            () -> new ApplicationException(null, "Review not found", HttpStatus.NOT_FOUND)
        );

        reviewRepository.delete(review);
    }

    @Override
    @Transactional
    public List<ReviewRes> findAllByBookId(String bookId) {
        List<Review> review = reviewRepository.findAllByBookId(bookId);
        return review.stream().map(reviewMapper::toRes).toList();
    }

    @Override
    @Transactional
    public ReviewRes findByBookIdAndUserId(String bookId) {
        UserCredential userCredential = getUserCredential();
        Review currReview = reviewRepository.findByBookIdAndUserId(bookId, userCredential.getUser().getId()).orElseThrow(
            () -> new ApplicationException(null, "Review not found", HttpStatus.NOT_FOUND)
        );
        return reviewMapper.toRes(currReview);
    }

    private UserCredential getUserCredential() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserCredential userCredential = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            AppUser appUser = (AppUser) authentication.getPrincipal();
            System.out.println("appUser: " + appUser);
            if (appUser == null) {
                throw new ApplicationException(null, "User is invalid", HttpStatus.BAD_REQUEST);
            }

            userCredential = userCredentialService.findById(appUser.getId());
        }
        return userCredential;
    }

    @Override
    public List<ActiveUserStatsRes> getMostActiveUser(Integer limit) {
        List<ActiveUserStats> res = reviewRepository.findMostActiveUser(limit);
        return res.stream()
                .map((val) -> ActiveUserStatsRes.builder()
                        .fullName(val.getFullname())
                        .totalReviews(val.getTotalReviews())
                        .build()
                ).toList();
    }

    @Override
    protected Specification<Review> createPredicate(RestFilter filter) {
        return (root, query, cb) -> {
            Join<Review, User> userJoin = root.join("user", JoinType.INNER);
            Join<Review, Book> bookJoin = root.join("book", JoinType.INNER);
            return switch (filter.getPath()) {
                case "rating" -> cb.equal(
                        root.get("rating"),
                        Integer.parseInt(filter.getValue().toString())
                );
                case "isVerified" -> cb.equal(
                        root.get("isVerified"),
                        Boolean.parseBoolean(filter.getValue().toString())
                );
                case "book.title" ->  cb.equal(
                        cb.lower(bookJoin.get("title")),
                        filter.getValue().toString().toLowerCase()
                );
                case "user.fullName" ->  cb.equal(
                        cb.lower(userJoin.get("fullName")),
                        filter.getValue().toString().toLowerCase()
                );
                default -> null;
            };
        };
    }

    @Override
    protected Specification<Review> createSearchPredicate(String search) {
        return (root, query, cb) -> {
            Join<Review, User> userJoin = root.join("user", JoinType.INNER);
            Join<Review, Book> bookJoin = root.join("book", JoinType.INNER);
            return cb.or(
                    cb.like(
                            cb.lower(userJoin.get("fullName")),
                            "%" + search.toLowerCase() + "%"
                    ),
                    cb.like(
                            cb.lower(bookJoin.get("title")),
                            "%" + search.toLowerCase() + "%"
                    )
            );
        };
    }

    @Override
    protected BaseRepository<Review, String> getRepository() {
        return reviewRepository;
    }
}
