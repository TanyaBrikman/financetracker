package org.financetracker.projection;

import java.math.BigDecimal;

public interface BalanceResponseProjection {
    BigDecimal getTotalIncome();
    BigDecimal getTotalExpense();
    BigDecimal getBalance();
}