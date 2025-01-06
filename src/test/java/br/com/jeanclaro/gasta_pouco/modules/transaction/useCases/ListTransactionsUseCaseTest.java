package br.com.jeanclaro.gasta_pouco.modules.transaction.useCases;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.jeanclaro.gasta_pouco.exceptions.TransactionNotFoundException;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.TransactionType;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.entity.TransactionEntity;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.ListTransactionsUseCase;

class ListTransactionsUseCaseTest {

    @InjectMocks
    private ListTransactionsUseCase listTransactionsUseCase;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnTransactionsWhenAuthorHasTransactions() {
        // Arrange
        UUID authorId = UUID.randomUUID();
        List<TransactionEntity> expectedTransactions = List.of(
            TransactionEntity.builder()
                .type(TransactionType.INCOME)
                .amount(2000.0)
                .category("Salary")
                .date(LocalDate.now())
                .description("Salary for June")
                .author(authorId)
                .build(),
            TransactionEntity.builder()
                .type(TransactionType.EXPENSE)
                .amount(1000.0)
                .category("Rent")
                .date(LocalDate.now())
                .description("Monthly rent")
                .author(authorId)
                .build()
        );
        when(transactionRepository.findByAuthor(authorId)).thenReturn(expectedTransactions);

        // Act
        List<TransactionEntity> actualTransactions = listTransactionsUseCase.execute(authorId);

        // Assert
        assertEquals(expectedTransactions, actualTransactions);
        verify(transactionRepository, times(1)).findByAuthor(authorId);
    }


    @Test
    void shouldThrowTransactionNotFoundExceptionWhenNoTransactionsExist() {
        // Arrange
        UUID authorId = UUID.randomUUID();
        when(transactionRepository.findByAuthor(authorId)).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(TransactionNotFoundException.class, () -> listTransactionsUseCase.execute(authorId));
        verify(transactionRepository, times(1)).findByAuthor(authorId);
    }
}
