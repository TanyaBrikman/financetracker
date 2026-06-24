package org.financetracker.mapper;

import org.financetracker.dto.request.TransactionRequestDto;
import org.financetracker.dto.response.TransactionResponseDto;
import org.financetracker.entity.Transaction;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class TransactionMapper {

    public Transaction toEntity(TransactionRequestDto transactionRequestDto) {
        if(transactionRequestDto == null) {
            throw new IllegalArgumentException("Transaction");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequestDto.getAmount());
        transaction.setDescription(transactionRequestDto.getDescription());
        transaction.setCategoryType(transactionRequestDto.getCategoryType());
        transaction.setType(transactionRequestDto.getType());
        transaction.setTransactionDate(transactionRequestDto.getTransactionDate());
        transaction.setCreatedAt(LocalDate.now());
        transaction.setUpdatedAt(LocalDate.now());

        return transaction;
    }

    public TransactionResponseDto toResponseDto(Transaction transaction) {
        if(transaction == null) {
            throw new IllegalArgumentException("Transaction");
        }

        TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
        transactionResponseDto.setId(transaction.getId());
        transactionResponseDto.setAmount(transaction.getAmount());
        transactionResponseDto.setDescription(transaction.getDescription());
        transactionResponseDto.setCategoryType(transaction.getCategoryType());
        transactionResponseDto.setType(transaction.getType());
        transactionResponseDto.setTransactionDate(transaction.getTransactionDate());
        transactionResponseDto.setCreatedAt(transaction.getCreatedAt());
        transactionResponseDto.setUpdatedAt(transaction.getUpdatedAt());
        transactionResponseDto.setCreatedAt(transaction.getCreatedAt());
        transactionResponseDto.setUpdatedAt(transaction.getUpdatedAt());


        return transactionResponseDto;
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