package org.financetracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.financetracker.projection.BalanceResponseProjection;
import org.financetracker.projection.CategoryExpenseProjection;
import org.financetracker.projection.MonthlySummaryProjection;
import org.financetracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportService {

    private final TransactionRepository transactionRepository;

    private LocalDate validateAndGetStartDate(LocalDate startDate) {
        return startDate != null ? startDate : LocalDate.of(2000, 1, 1);
    }

    private LocalDate validateAndGetEndDate(LocalDate endDate) {
        return endDate != null ? endDate : LocalDate.now();
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(String.format("Start date '%s' cannot be after end date '%s'", startDate, endDate));
        }
    }

    private void validateYear(int year) {
        int currentYear = LocalDate.now().getYear();
        if (year < 2000 || year > currentYear) {
            throw new IllegalArgumentException(
                    String.format("Year must be between 2000 and %d", currentYear)
            );
        }
    }

    @Transactional(readOnly = true)
    public BalanceResponseProjection getBalance(Long userId, LocalDate startDate, LocalDate endDate) {
        log.info("Get balance for user {}", userId);
        LocalDate validatedStartDate = validateAndGetStartDate(startDate);
        LocalDate validatedEndDate = validateAndGetEndDate(endDate);
        validateDateRange(validatedStartDate, validatedEndDate);
        BalanceResponseProjection response = transactionRepository.getBalance(userId, validatedStartDate, validatedEndDate);
        log.debug("Get balance for user {}", userId);
        return response;
    }

    @Transactional(readOnly = true)
    public List<CategoryExpenseProjection> getExpensesByCategoryType(Long userId, LocalDate startDate, LocalDate endDate) {
        log.info("Get expenses by category for user {}", userId);
        LocalDate validatedStartDate = validateAndGetStartDate(startDate);
        LocalDate validatedEndDate = validateAndGetEndDate(endDate);
        validateDateRange(validatedStartDate, validatedEndDate);
        List<CategoryExpenseProjection> response = transactionRepository.getCategoryList(userId, validatedStartDate, validatedEndDate);
        log.debug("Get expenses by category for user {}", userId);
        return response;
    }

    @Transactional(readOnly = true)
    public List<MonthlySummaryProjection> getMonthlySummary(Long userId, int year) {
        log.info("Get monthly summary for user {}", userId);
        validateYear(year);
        List<MonthlySummaryProjection> response = transactionRepository.getMonthlySummary(userId, year);
        log.debug("Found {} monthly summaries for user {}", response.size(), userId);
        return response;
    }
}