package org.financetracker.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionRequestUserIdDto {
    @NotNull(message = "User id is required")
    private Long userId;
}
