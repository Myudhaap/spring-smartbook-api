package dev.mayutama.smartbook.controller;

import dev.mayutama.smartbook.constant.AppPath;
import dev.mayutama.smartbook.model.dto.response.dashboard.ActiveUserStatsRes;
import dev.mayutama.smartbook.model.dto.response.dashboard.BookRatingSummaryRes;
import dev.mayutama.smartbook.service.DashboardService;
import dev.mayutama.smartbook.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppPath.DASHBOARD)
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/top-rated-book/{limit}")
    public ResponseEntity<?> getTopRatedBook(
            @PathVariable Integer limit
    ){
        List<BookRatingSummaryRes> res = dashboardService.getTopRatedBooks(limit);
        return ResponseUtil.responseSuccess(res, "Success get top rated book");
    }

    @GetMapping("/most-active-user/{limit}")
    public ResponseEntity<?> getMostActiveUsers(
            @PathVariable Integer limit
    ){
        List<ActiveUserStatsRes> res = dashboardService.getMostActiveUser(limit);
        return ResponseUtil.responseSuccess(res, "Success get top rated book");
    }
}
