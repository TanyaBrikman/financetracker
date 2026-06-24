package org.financetracker.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.financetracker.entity.CategoryType;
import org.financetracker.entity.TransactionType;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionFilterRequestDTO {
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String startDate;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String endDate;
    @NotNull(message = "CategoryType is required")
    private CategoryType categoryType;
    @NotNull(message = "Type is required")
    private TransactionType type;

    public LocalDate getStartDateAsLocalDate() {
        return startDate != null ? LocalDate.parse(startDate) : null;
    }

    public LocalDate getEndDateAsLocalDate() {
        return endDate != null ? LocalDate.parse(endDate) : null;
    }
}
