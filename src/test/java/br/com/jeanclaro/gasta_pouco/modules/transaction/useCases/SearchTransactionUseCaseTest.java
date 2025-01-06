package br.com.jeanclaro.gasta_pouco.modules.transaction.useCases;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.TransactionType;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.entity.TransactionEntity;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.SearchTransactionUseCase;

class SearchTransactionUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private SearchTransactionUseCase searchTransactionUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicializa os mocks
    }

    @Test
    void shouldReturnFilteredTransactions() {
        // Arrange
        String filter = "Salary";
        UUID authorId = UUID.randomUUID();
        
        TransactionEntity transaction1 = TransactionEntity.builder()
            .description("Salary for June")
            .author(authorId)
            .amount(2000.0)
            .category("Income")
            .type(TransactionType.INCOME)
            .build();

        TransactionEntity transaction2 = TransactionEntity.builder()
            .description("Salary for July")
            .author(authorId)
            .amount(2500.0)
            .category("Income")
            .type(TransactionType.INCOME)
            .build();

        List<TransactionEntity> expectedTransactions = List.of(transaction1, transaction2);

        when(transactionRepository.findByDescriptionContainingIgnoreCase(filter)).thenReturn(expectedTransactions);

        // Act
        List<TransactionEntity> actualTransactions = searchTransactionUseCase.execute(filter);

        // Assert
        assertEquals(expectedTransactions, actualTransactions);
        verify(transactionRepository, times(1)).findByDescriptionContainingIgnoreCase(filter);
    }

    @Test
    void shouldReturnEmptyListWhenNoTransactionsMatchFilter() {
        // Arrange
        String filter = "Nonexistent";
        when(transactionRepository.findByDescriptionContainingIgnoreCase(filter)).thenReturn(List.of());

        // Act
        List<TransactionEntity> actualTransactions = searchTransactionUseCase.execute(filter);

        // Assert
        assertTrue(actualTransactions.isEmpty());
        verify(transactionRepository, times(1)).findByDescriptionContainingIgnoreCase(filter);
    }
}

