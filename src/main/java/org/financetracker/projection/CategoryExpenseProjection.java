package org.financetracker.projection;

import org.financetracker.entity.CategoryType;
import java.math.BigDecimal;

public interface CategoryExpenseProjection {
    CategoryType getCategoryType();
    BigDecimal getTotalAmount();
    Long getTransactionCount();
    BigDecimal getPercentage();
}