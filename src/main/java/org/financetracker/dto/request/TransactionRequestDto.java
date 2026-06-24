package org.financetracker.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.financetracker.entity.CategoryType;
import org.financetracker.entity.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDto {
    @Positive
    @NotNull(message = "Amount is required")
    private BigDecimal amount;
    @NotNull(message = "Type is required")
    private TransactionType type;
    @NotNull(message = "CategoryType is required")
    private CategoryType categoryType;
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    @NotBlank(message ="Description is required")
    private String description;
    @NotNull(message = "TransactionDate is required")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    private LocalDate transactionDate;
}