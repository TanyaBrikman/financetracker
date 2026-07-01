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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(
            @RequestBody
            @Valid TransactionRequestDto transactionRequestDto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionService.createTransaction(transactionRequestDto));
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDto>> getAllTransactions(
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.getAllTransactions(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<TransactionResponseDto>> getAllTransactionWithFilters(
            @Valid TransactionFilterRequestDTO transactionFilterRequestDTO,
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.getAllTransactionWithFilters(
                        transactionFilterRequestDTO,
                        pageable
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(
            @PathVariable Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.getTransactionById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(
            @PathVariable Long id,
            @RequestBody
            @Valid TransactionRequestDto transactionRequestDto
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.updateTransaction(id, transactionRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long id
    ) {
        transactionService.deleteTransaction(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}