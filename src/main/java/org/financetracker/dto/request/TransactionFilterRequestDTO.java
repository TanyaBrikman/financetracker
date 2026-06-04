package org.financetracker.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.financetracker.entity.CategoryType;
import org.financetracker.entity.TransactionType;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionFilterRequestDTO {
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String startDate;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String endDate;
    private String categoryType;
    private String type;

    public CategoryType getCategoryTypeAsEnum() {
        if(categoryType == null || categoryType.isBlank()) {
            return null;
        }
        try {
            return CategoryType.valueOf(categoryType.toUpperCase());
        }catch (Exception e) {
            throw new IllegalArgumentException("Invalid category type: " + categoryType);
        }
    }

    public TransactionType getTransactionTypeAsEnum() {
        if(type == null || type.isBlank()) {
            return null;
        }
        try {
            return TransactionType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid transaction type: " + type);
        }
    }

    public LocalDate getStartDateAsLocalDate() {
        return startDate != null ? LocalDate.parse(startDate) : null;
    }

    public LocalDate getEndDateAsLocalDate() {
        return endDate != null ? LocalDate.parse(endDate) : null;
    }
}
