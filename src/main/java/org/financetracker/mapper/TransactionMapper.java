package org.financetracker.mapper;

import org.financetracker.dto.request.TransactionRequestDto;
import org.financetracker.dto.response.TransactionResponseDto;
import org.financetracker.entity.Transaction;
import org.financetracker.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TransactionMapper {

    public Transaction toEntity(TransactionRequestDto transactionRequestDto, User user) {
        if (transactionRequestDto == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        return Transaction.builder()
                .amount(transactionRequestDto.getAmount())
                .description(transactionRequestDto.getDescription())
                .categoryType(transactionRequestDto.getCategoryType())
                .type(transactionRequestDto.getType())
                .transactionDate(transactionRequestDto.getTransactionDate())
                .user(user)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
    }

    public TransactionResponseDto toResponseDto(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }

        return TransactionResponseDto.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .categoryType(transaction.getCategoryType())
                .type(transaction.getType())
                .transactionDate(transaction.getTransactionDate())
                .userId(transaction.getUser().getId())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }

    public void updateEntity(Transaction entity, TransactionRequestDto transactionRequestDto) {
        entity.setAmount(transactionRequestDto.getAmount());
        entity.setDescription(transactionRequestDto.getDescription());
        entity.setCategoryType(transactionRequestDto.getCategoryType());
        entity.setType(transactionRequestDto.getType());
        entity.setTransactionDate(transactionRequestDto.getTransactionDate());
        entity.setUpdatedAt(LocalDate.now());
    }
}