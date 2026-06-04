package org.financetracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.financetracker.dto.request.TransactionFilterRequestDTO;
import org.financetracker.dto.request.TransactionRequestDto;
import org.financetracker.dto.response.TransactionResponseDto;
import org.financetracker.entity.CategoryType;
import org.financetracker.entity.TransactionType;
import org.financetracker.service.TransactionServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionServiceImpl transactionServiceImpl;

    @PostMapping
    public TransactionResponseDto createTransaction(
            @RequestBody
            @Valid TransactionRequestDto transactionRequestDto
    ) {
        return transactionServiceImpl.createTransaction(transactionRequestDto);
    }

    @GetMapping
    public Page<TransactionResponseDto> getAllTransactions(
            @PageableDefault Pageable pageable
    ) {
        return transactionServiceImpl.getAllTransactions(pageable);
    }

    @GetMapping("/api/transactions")
    public Page<TransactionResponseDto> findAllTransactionWithFilters(
            @Valid TransactionFilterRequestDTO transactionFilterRequestDTO,
            @PageableDefault Pageable pageable
            ) {

        if(transactionFilterRequestDTO.getStartDate() != null && transactionFilterRequestDTO.getEndDate() != null){
            LocalDate start = transactionFilterRequestDTO.getStartDateAsLocalDate();
            LocalDate end = transactionFilterRequestDTO.getEndDateAsLocalDate();

            if(start.isAfter(end)) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }
        }

        return transactionServiceImpl.findAllTransactionWithFilters(
                transactionFilterRequestDTO.getStartDateAsLocalDate(),
                transactionFilterRequestDTO.getEndDateAsLocalDate(),
                transactionFilterRequestDTO.getCategoryTypeAsEnum(),
                transactionFilterRequestDTO.getTransactionTypeAsEnum(),
                pageable
        );
    }

    @GetMapping("/{id}")
    public TransactionResponseDto findByIdTransaction(
            @PathVariable Long id
    ) {
        return transactionServiceImpl.findByIdTransaction(id);
    }

    @PutMapping("/{id}")
    public TransactionResponseDto updateTransaction(
            @PathVariable Long id,
            @RequestBody
            @Valid TransactionRequestDto transactionRequestDto
    ) {
        return transactionServiceImpl.updateTransaction(id, transactionRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(
            @PathVariable Long id
    ) {
        transactionServiceImpl.deleteTransaction(id);
    }
}