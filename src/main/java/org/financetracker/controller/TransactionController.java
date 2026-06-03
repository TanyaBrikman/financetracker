package org.financetracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.financetracker.dto.request.TransactionRequestDto;
import org.financetracker.dto.response.TransactionResponseDto;
import org.financetracker.service.TransactionServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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