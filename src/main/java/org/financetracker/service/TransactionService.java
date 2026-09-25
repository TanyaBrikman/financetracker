package org.financetracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.financetracker.dto.request.TransactionFilterRequestDto;
import org.financetracker.dto.request.TransactionRequestDto;
import org.financetracker.dto.request.TransactionRequestUserIdDto;
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
    public Page<TransactionResponseDto> getAllTransactions(TransactionRequestUserIdDto transactionRequestUserIdDto, Pageable pageable) {

        Long userId = transactionRequestUserIdDto.getUserId();

        log.info("Get all transactions: userId={}, page={}, size={}",
                userId, pageable.getPageNumber(), pageable.getPageSize());

        Page<Transaction> entityPage = transactionRepository.findAllTransactions(userId, pageable);
        logPage(entityPage);
        return entityPage.map(transactionMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getAllTransactionWithFilters(
            TransactionFilterRequestDto transactionFilterRequestDto,
            Pageable pageable
    ) {
        Long userId = transactionFilterRequestDto.getUserId();
        LocalDate startDate = transactionFilterRequestDto.getStartDateAsLocalDate();
        LocalDate endDate = transactionFilterRequestDto.getEndDateAsLocalDate();
        CategoryType categoryType = transactionFilterRequestDto.getCategoryType();
        TransactionType type = transactionFilterRequestDto.getType();

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            log.debug("Start date {} is after end date {}", startDate, endDate);
            throw new IllegalArgumentException(String.format("Start date '%s' cannot be after end date '%s'", startDate, endDate));
        }

        log.info("Get transactions with filters: userId={}, startDate={}, endDate={}, categoryType={}, type={}, page={}, size={}",
                userId,
                startDate,
                endDate,
                categoryType,
                type,
                pageable.getPageNumber(),
                pageable.getPageSize());
        Page<Transaction> entityPage = transactionRepository.findAllTransactionWithFilters(
                userId,
                startDate,
                endDate,
                categoryType,
                type,
                pageable);
        logPage(entityPage);
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
        Transaction saved = transactionRepository.saveAndFlush(transaction);
        log.debug("Transaction with id: {} updated successfully", id);
        return transactionMapper.toResponseDto(saved);
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

    private void logPage(Page<?> page) {
        log.debug("Page {} of {} returned {} items (total={})",
                page.getNumber() + 1,
                page.getTotalPages(),
                page.getNumberOfElements(),
                page.getTotalElements());
    }
}