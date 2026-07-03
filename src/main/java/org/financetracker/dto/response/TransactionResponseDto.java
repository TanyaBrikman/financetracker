package org.financetracker.dto.response;

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
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDto {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private CategoryType categoryType;
    private String description;
    private LocalDate transactionDate;
    private Long userId;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}