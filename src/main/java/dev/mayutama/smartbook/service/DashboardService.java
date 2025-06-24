package dev.mayutama.smartbook.service;

import dev.mayutama.smartbook.model.dto.response.book.BookRes;
import dev.mayutama.smartbook.model.dto.response.dashboard.ActiveUserStatsRes;
import dev.mayutama.smartbook.model.dto.response.dashboard.BookRatingSummaryRes;

import java.util.List;

public interface DashboardService {
    List<BookRatingSummaryRes> getTopRatedBooks(Integer limit);
    List<ActiveUserStatsRes> getMostActiveUser(Integer limit);
}
