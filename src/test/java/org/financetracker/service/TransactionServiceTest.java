package org.financetracker.service;

import org.financetracker.dto.request.TransactionRequestDto;
import org.financetracker.dto.response.TransactionResponseDto;
import org.financetracker.entity.CategoryType;
import org.financetracker.entity.Transaction;
import org.financetracker.entity.TransactionType;
import org.financetracker.entity.User;
import org.financetracker.mapper.TransactionMapper;
import org.financetracker.repository.TransactionRepository;
import org.financetracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransactionService transactionService;  //  Реальный сервис с фальшивыми зависимостями

    private User user;
    private Transaction transaction;
    private TransactionRequestDto requestDto;
    private TransactionResponseDto responseDto;
    Long transactionId = 1L;
    Long userId = 1L;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .userName("test_user")
                .age(30)
                .email("test@test.com")
                .build();

        transaction = Transaction.builder()
                .id(1L)
                .amount(new BigDecimal("30000"))
                .description("Куртка")
                .categoryType(CategoryType.SHOPPING)
                .type(TransactionType.EXPENSE)
                .user(user)
                .build();

        requestDto = TransactionRequestDto.builder()
                .amount(new BigDecimal("30000"))
                .description("Куртка")
                .categoryType(CategoryType.SHOPPING)
                .type(TransactionType.EXPENSE)
                .userId(1L)
                .build();

        responseDto = TransactionResponseDto.builder()
                .id(1L)
                .amount(new BigDecimal("30000"))
                .description("Куртка")
                .categoryType(CategoryType.SHOPPING)
                .type(TransactionType.EXPENSE)
                .userId(1L)
                .build();
    }

    @Test
    void shouldCreateTransaction_Success() {

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(transactionMapper.toEntity(any(TransactionRequestDto.class), any(User.class)))
                .thenReturn(transaction);

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction);

        when(transactionMapper.toResponseDto(any(Transaction.class)))
                .thenReturn(responseDto);
        TransactionResponseDto result = transactionService.createTransaction(requestDto);
        assertNotNull(result);
        assertEquals(responseDto, result);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void ShouldUpdateAndReturnDto_WhenTransactionExists() {
        TransactionRequestDto updateRequest = TransactionRequestDto.builder()
                .amount(new BigDecimal("50000"))
                .build();

        TransactionResponseDto updateResponse = TransactionResponseDto.builder()
                .id(transactionId)
                .amount(new BigDecimal("50000"))  // ИЗМЕНИЛИ
                .description("Куртка")
                .categoryType(CategoryType.SHOPPING)
                .type(TransactionType.EXPENSE)
                .userId(userId)
                .build();

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        doNothing().when(transactionMapper).updateEntity(transaction, updateRequest);

        when(transactionMapper.toResponseDto(transaction)).thenReturn(updateResponse);

        TransactionResponseDto result = transactionService.updateTransaction(transactionId, updateRequest);

        assertNotNull(result);

        assertEquals(updateResponse, result);

        assertEquals(new BigDecimal("50000"), result.getAmount());

        verify(transactionRepository, times(1)).findById(transactionId);
    }

    @Test
    void ShouldDeleteTransaction_WhenTransactionExists() {

        when(transactionRepository.existsById(transactionId)).thenReturn(true);

        transactionService.deleteTransaction(transactionId);

        verify(transactionRepository, times(1)).existsById(transactionId);

        verify(transactionRepository, times(1)).deleteById(transactionId);
    }

    @Test
    void shouldFindById_WhenTransactionExists() {
        when(transactionMapper.toResponseDto(transaction)).thenReturn(responseDto);
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        transactionService.getTransactionById(transactionId);

        assertNotNull(transaction);
        verify(transactionRepository, times(1)).findById(transactionId);
    }
}