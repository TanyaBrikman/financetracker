package org.financetracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.financetracker.dto.request.TransactionRequestDto;
import org.financetracker.dto.response.TransactionResponseDto;
import org.financetracker.entity.CategoryType;
import org.financetracker.entity.Transaction;
import org.financetracker.entity.TransactionType;
import org.financetracker.exception.TransactionNotFoundException;
import org.financetracker.mapper.TransactionMapper;
import org.financetracker.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionResponseDto createTransaction(TransactionRequestDto transactionRequestDto) {
        //Преобразуем DTO -> Entity
        Transaction transaction = transactionMapper.toEntity(transactionRequestDto);
        //Сохраняем
        Transaction savedTransaction = transactionRepository.save(transaction);
        //Преобразуем обратно Entity -> DTO
        return transactionMapper.toResponseDto(savedTransaction);
    }

    public Page<TransactionResponseDto> getAllTransactions(Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 10);
        }
        return transactionRepository.findAll(pageable).map(transactionMapper::toResponseDto);
    }

    public Page<TransactionResponseDto> findAllTransactionWithFilters(
            LocalDate startDate,
            LocalDate endDate,
            CategoryType category,
            TransactionType type,
            Pageable pageable
    ) {
        Page<Transaction> entityPage = transactionRepository.findAllTransactionWithFilters(startDate,endDate,category,type,pageable);
        return entityPage.map(transactionMapper::toResponseDto);
    }

    @Transactional
    public TransactionResponseDto findByIdTransaction(Long id) {
        return transactionRepository.findById(id).map(transactionMapper::toResponseDto).orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @Transactional
    public TransactionResponseDto updateTransaction(Long id, TransactionRequestDto transactionRequestDto) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
        transactionMapper.updateEntity(transaction, transactionRequestDto);
        return transactionMapper.toResponseDto(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
        transactionRepository.deleteById(id);
    }
}