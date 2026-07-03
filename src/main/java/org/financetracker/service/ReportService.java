package org.financetracker.service;

import lombok.RequiredArgsConstructor;
import org.financetracker.exception.ResourceNotFoundException;
import org.financetracker.projection.BalanceResponseProjection;
import org.financetracker.projection.CategoryExpenseProjection;
import org.financetracker.projection.MonthlySummaryProjection;
import org.financetracker.repository.TransactionRepository;
import org.financetracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    private void isUserExist(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
    }

    private LocalDate validateAndGetStartDate(LocalDate startDate) {
        return startDate != null ? startDate : LocalDate.of(2000, 1, 1);
    }

    private LocalDate validateAndGetEndDate(LocalDate endDate) {
        return endDate != null ? endDate : LocalDate.now();
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
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

    public BalanceResponseProjection getBalance(Long userId, LocalDate startDate, LocalDate endDate) {
        isUserExist(userId);
        startDate = validateAndGetStartDate(startDate);
        endDate = validateAndGetEndDate(endDate);
        validateDateRange(startDate, endDate);
        return transactionRepository.getBalance(userId, startDate, endDate);
    }

    public List<CategoryExpenseProjection> getExpensesByCategoryType(Long userId, LocalDate startDate, LocalDate endDate) {
        isUserExist(userId);
        startDate = validateAndGetStartDate(startDate);
        endDate = validateAndGetEndDate(endDate);
        validateDateRange(startDate, endDate);
        return transactionRepository.getCategoryList(userId, startDate, endDate);
    }

    public List<MonthlySummaryProjection> getMonthlySummary(Long userId, int year) {
        isUserExist(userId);
        validateYear(year);
        return transactionRepository.getMonthlySummary(userId, year);
    }
}