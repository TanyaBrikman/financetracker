package org.financetracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.financetracker.dto.request.TransactionFilterRequestDTO;
import org.financetracker.dto.request.TransactionRequestDto;
import org.financetracker.dto.response.TransactionResponseDto;
import org.financetracker.entity.CategoryType;
import org.financetracker.entity.Transaction;
import org.financetracker.entity.TransactionType;
import org.financetracker.entity.User;
import org.financetracker.exception.ResourceNotFoundException;
import org.financetracker.mapper.TransactionMapper;
import org.financetracker.repository.TransactionRepository;
import org.financetracker.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Transactional
    public TransactionResponseDto createTransaction(TransactionRequestDto transactionRequestDto) {
       User user = userRepository.findById(transactionRequestDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Transaction", transactionRequestDto.getUserId()));
        //Преобразуем DTO -> Entity
        Transaction transaction = transactionMapper.toEntity(transactionRequestDto,user);
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

    public Page<TransactionResponseDto> getAllTransactionWithFilters(
            TransactionFilterRequestDTO transactionFilterRequestDTO,
            Pageable pageable
    ) {
        LocalDate startDate = transactionFilterRequestDTO.getStartDateAsLocalDate();
        LocalDate endDate = transactionFilterRequestDTO.getEndDateAsLocalDate();
        CategoryType categoryType = transactionFilterRequestDTO.getCategoryType();
        TransactionType type = transactionFilterRequestDTO.getType();

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        Page<Transaction> entityPage = transactionRepository.findAllTransactionWithFilters(startDate, endDate, categoryType, type, pageable);
        return entityPage.map(transactionMapper::toResponseDto);
    }

    @Transactional
    public TransactionResponseDto getTransactionById(Long id) {
        return transactionRepository.findById(id).map(transactionMapper::toResponseDto).orElseThrow(() -> new ResourceNotFoundException("Transaction", id));
    }

    @Transactional
    public TransactionResponseDto updateTransaction(Long id, TransactionRequestDto transactionRequestDto) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction", id));
        transactionMapper.updateEntity(transaction, transactionRequestDto);
        return transactionMapper.toResponseDto(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction", id);
        }
        transactionRepository.deleteById(id);
    }
}