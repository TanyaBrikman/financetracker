package org.financetracker.service;

import org.financetracker.dto.request.TransactionRequestDto;
import org.financetracker.dto.response.TransactionResponseDto;
import org.financetracker.entity.CategoryType;
import org.financetracker.entity.Transaction;
import org.financetracker.entity.TransactionType;
import org.financetracker.mapper.TransactionMapper;
import org.financetracker.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;  //  Реальный сервис с фальшивыми зависимостями

    @Test
    void shouldCreateTransaction_Success() {

        TransactionRequestDto requestDto = TransactionRequestDto.builder()
                .amount(new BigDecimal("15000"))
                .description("Куртка")
                .categoryType(CategoryType.SHOPPING)
                .type(TransactionType.EXPENSE)
                .transactionDate(LocalDate.now())
                .build();

        Transaction transactionEntity = Transaction.builder()
                .id(1L)
                .amount(new BigDecimal("15000"))
                .description("Куртка")
                .categoryType(CategoryType.SHOPPING)
                .type(TransactionType.EXPENSE)
                .transactionDate(LocalDate.now())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();

        TransactionResponseDto responseDto = TransactionResponseDto.builder()
                .id(1L)
                .amount(new BigDecimal("15000"))
                .description("Куртка")
                .categoryType(CategoryType.SHOPPING)
                .type(TransactionType.EXPENSE)
                .transactionDate(LocalDate.now())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();

        when(transactionMapper.toEntity(any(TransactionRequestDto.class)))
                .thenReturn(transactionEntity);

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transactionEntity);

        when(transactionMapper.toResponseDto(any(Transaction.class)))
                .thenReturn(responseDto);
        TransactionResponseDto result = transactionService.createTransaction(requestDto);
        assertNotNull(result);

        verify(transactionRepository, times(1)).save(any(Transaction.class));

    }



}
