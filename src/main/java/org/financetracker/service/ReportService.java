package org.financetracker.service;

import lombok.RequiredArgsConstructor;
import org.financetracker.projection.BalanceResponseProjection;
import org.financetracker.projection.MonthlySummaryProjection;
import org.financetracker.projection.CategoryExpenseProjection;
import org.financetracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final TransactionRepository transactionRepository;

    public BalanceResponseProjection getBalance(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.of(2000, 1, 1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("StartDate cannot be after endDate");
        }
        return transactionRepository.getBalance(startDate, endDate);
    }

    public List<CategoryExpenseProjection> getExpensesByCategoryType(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.of(2000, 1, 1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("StartDate cannot be after endDate");
        }
        return transactionRepository.getCategoryList(startDate, endDate);
    }

    public List<MonthlySummaryProjection> getMonthlySummary(int year) {
        if(year < 2000 || year > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Year must be between 2000 and date now");
        }
        return transactionRepository.getMonthlySummary(year);
    }
}