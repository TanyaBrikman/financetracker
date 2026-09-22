package org.financetracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.financetracker.dto.request.TransactionFilterRequestDto;
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
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final UserRepository userRepository;

    @Transactional
    public TransactionResponseDto createTransaction(TransactionRequestDto transactionRequestDto) {

        log.info("Create transaction for user with id: {}", transactionRequestDto.getUserId());

        User user = userRepository.findById(transactionRequestDto.getUserId())
                .orElseThrow(() -> {

                    log.warn("User with id: {} not found", transactionRequestDto.getUserId());

                    return new ResourceNotFoundException("User", transactionRequestDto.getUserId());
                });
        log.debug("User found with id: {}", transactionRequestDto.getUserId());
        //Преобразуем DTO -> Entity
        Transaction transaction = transactionMapper.toEntity(transactionRequestDto, user);
        //Сохраняем
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.debug("Saved transaction with id: {} created successfully", savedTransaction.getId());
        //Преобразуем обратно Entity -> DTO
        return transactionMapper.toResponseDto(savedTransaction);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getAllTransactions(Pageable pageable) {
        log.info("Get all transactions");
        if (pageable == null) {
            pageable = PageRequest.of(0, 10);
        }

        log.debug("Page request: page={}, size={}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<TransactionResponseDto> response = transactionRepository.findAll(pageable).map(transactionMapper::toResponseDto);
        log.debug("Found {} transactions out of {}", response.getNumberOfElements(), response.getTotalElements());

        return response;
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getAllTransactionWithFilters(
            TransactionFilterRequestDto transactionFilterRequestDTO,
            Pageable pageable
    ) {
        log.info("Get all transactions with filters");
        LocalDate startDate = transactionFilterRequestDTO.getStartDateAsLocalDate();
        LocalDate endDate = transactionFilterRequestDTO.getEndDateAsLocalDate();
        CategoryType categoryType = transactionFilterRequestDTO.getCategoryType();
        TransactionType type = transactionFilterRequestDTO.getType();
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {

            log.warn("Start date {} is after end date {}", startDate, endDate);

            throw new IllegalArgumentException(String.format("Start date '%s' cannot be after end date '%s'", startDate, endDate));
        }

        log.debug("Start date: {}", startDate);
        log.debug("End date: {}", endDate);
        Page<Transaction> entityPage = transactionRepository.findAllTransactionWithFilters(startDate, endDate, categoryType, type, pageable);
        log.debug("Found {} transactions with filters", entityPage.getNumberOfElements());
        return entityPage.map(transactionMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public TransactionResponseDto getTransactionById(Long id) {
        log.info("Get transaction by id: {}", id);
        TransactionResponseDto response = transactionRepository.findById(id).map(transactionMapper::toResponseDto)
                .orElseThrow(() -> {
                    log.warn("Transaction with id: {} not found", id);
                    return new ResourceNotFoundException("Transaction", id);
                });
        log.debug("Found transaction with id: {}", id);
        return response;
    }

    @Transactional
    public TransactionResponseDto updateTransactionById(Long id, TransactionRequestDto transactionRequestDto) {
        log.info("Update transaction with id: {}", id);

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() ->
                {
                    log.warn("Update failed. Transaction with id: {}", id);
                    return new ResourceNotFoundException("Transaction", id);
                });
        transactionMapper.updateEntity(transaction, transactionRequestDto);
        log.debug("Transaction with id: {} updated successfully", id);

        return transactionMapper.toResponseDto(transaction);
    }

    @Transactional
    public void deleteTransactionById(Long id) {
        log.info("Delete transaction with id: {}", id);
        if (!transactionRepository.existsById(id)) {
            log.warn("Transaction with id: {} not found for deletion", id);
            throw new ResourceNotFoundException("Transaction", id);
        }
        transactionRepository.deleteById(id);
        log.info("Transaction with id: {} deleted successfully", id);
    }
}