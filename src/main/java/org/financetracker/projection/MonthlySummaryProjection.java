package org.financetracker.projection;

import java.math.BigDecimal;

public interface MonthlySummaryProjection {
    int getYear();
    int getMonth();
    String getMonthName();
    BigDecimal totalIncome();
    BigDecimal totalExpense();
    BigDecimal balance();
}