package dev.mayutama.smartbook.service.impl;

import dev.mayutama.smartbook.model.dto.response.dashboard.ActiveUserStatsRes;
import dev.mayutama.smartbook.model.dto.response.dashboard.BookRatingSummaryRes;
import dev.mayutama.smartbook.service.BookService;
import dev.mayutama.smartbook.service.DashboardService;
import dev.mayutama.smartbook.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final BookService bookService;
    private final ReviewService reviewService;

    @Override
    public List<BookRatingSummaryRes> getTopRatedBooks(Integer limit) {
        return bookService.getTopRatedBooks(limit);
    }

    @Override
    public List<ActiveUserStatsRes> getMostActiveUser(Integer limit) {
        return reviewService.getMostActiveUser(limit);
    }
}
