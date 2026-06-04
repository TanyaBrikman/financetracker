package org.financetracker.service;

import org.financetracker.dto.request.TransactionRequestDto;
import org.financetracker.dto.response.TransactionResponseDto;
import org.financetracker.entity.CategoryType;
import org.financetracker.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TransactionService {

    public TransactionResponseDto createTransaction(TransactionRequestDto transactionRequestDto);

    public Page<TransactionResponseDto> getAllTransactions(Pageable pageable);

    public Page<TransactionResponseDto> findAllTransactionWithFilters(
            LocalDate startDate,
            LocalDate endDate,
            CategoryType category,
            TransactionType type,
            Pageable pageable
    );

    public TransactionResponseDto findByIdTransaction(Long id);

    public TransactionResponseDto updateTransaction(
            Long id,
            TransactionRequestDto transactionRequestDto
    );

    public void deleteTransaction(Long id);
}