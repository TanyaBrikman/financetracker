package org.financetracker.controller;

import lombok.RequiredArgsConstructor;
import org.financetracker.projection.BalanceResponseProjection;
import org.financetracker.projection.MonthlySummaryProjection;
import org.financetracker.projection.CategoryExpenseProjection;
import org.financetracker.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/report")
@RestController
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/balance")
    public ResponseEntity<BalanceResponseProjection> getBalance(@RequestParam(required = false) LocalDate startDate,
                                     @RequestParam(required = false) LocalDate endDate) {
        return ResponseEntity.status(HttpStatus.OK).body(reportService.getBalance(startDate, endDate));
    }

    @GetMapping("/expenses-by-category")
    public  ResponseEntity<List<CategoryExpenseProjection>> getExpensesByCategoryType(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(reportService.getExpensesByCategoryType(startDate, endDate));
    }

    @GetMapping("/monthly-summary")
    public ResponseEntity<List<MonthlySummaryProjection>> getMonthlySummary(
            @RequestParam int year
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(reportService.getMonthlySummary(year));
    }
}