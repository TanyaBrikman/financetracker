package org.financetracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.financetracker.dto.request.TransactionFilterRequestDTO;
import org.financetracker.dto.request.TransactionRequestDto;
import org.financetracker.dto.response.TransactionResponseDto;
import org.financetracker.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public TransactionResponseDto createTransaction(
            @RequestBody
            @Valid TransactionRequestDto transactionRequestDto
    ) {
        return transactionService.createTransaction(transactionRequestDto);
    }

    @GetMapping
    public Page<TransactionResponseDto> getAllTransactions(
            @PageableDefault Pageable pageable
    ) {
        return transactionService.getAllTransactions(pageable);
    }

    @GetMapping("/filter")
    public Page<TransactionResponseDto> findAllTransactionWithFilters(
            @Valid TransactionFilterRequestDTO transactionFilterRequestDTO,
            @PageableDefault Pageable pageable
    ) {

        if (transactionFilterRequestDTO.getStartDate() != null && transactionFilterRequestDTO.getEndDate() != null) {
            LocalDate start = transactionFilterRequestDTO.getStartDateAsLocalDate();
            LocalDate end = transactionFilterRequestDTO.getEndDateAsLocalDate();

            if (start.isAfter(end)) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }
        }

        return transactionService.findAllTransactionWithFilters(
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
        return transactionService.findByIdTransaction(id);
    }

    @PutMapping("/{id}")
    public TransactionResponseDto updateTransaction(
            @PathVariable Long id,
            @RequestBody
            @Valid TransactionRequestDto transactionRequestDto
    ) {
        return transactionService.updateTransaction(id, transactionRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(
            @PathVariable Long id
    ) {
        transactionService.deleteTransaction(id);
    }
}