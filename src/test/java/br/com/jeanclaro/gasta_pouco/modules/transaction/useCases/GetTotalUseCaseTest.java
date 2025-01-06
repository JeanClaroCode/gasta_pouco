package br.com.jeanclaro.gasta_pouco.modules.transaction.useCases;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.GetTotalUseCase;

class GetTotalUseCaseTest {

    @InjectMocks
    private GetTotalUseCase getTotalUseCase;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnTotalWhenAuthorIdIsValid() {
        // Arrange
        UUID authorId = UUID.randomUUID();
        BigDecimal expectedTotal = new BigDecimal("3000.75");
        when(transactionRepository.findTotalByAuthor(authorId)).thenReturn(expectedTotal);

        // Act
        BigDecimal actualTotal = getTotalUseCase.execute(authorId);

        // Assert
        assertEquals(expectedTotal, actualTotal);
        verify(transactionRepository, times(1)).findTotalByAuthor(authorId);
    }

    @Test
    void shouldReturnZeroWhenNoTransactionsExistForAuthor() {
        // Arrange
        UUID authorId = UUID.randomUUID();
        BigDecimal expectedTotal = BigDecimal.ZERO;
        when(transactionRepository.findTotalByAuthor(authorId)).thenReturn(expectedTotal);

        // Act
        BigDecimal actualTotal = getTotalUseCase.execute(authorId);

        // Assert
        assertEquals(expectedTotal, actualTotal);
        verify(transactionRepository, times(1)).findTotalByAuthor(authorId);
    }
}