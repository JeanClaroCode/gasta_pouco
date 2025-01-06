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
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.GetTotalIncomeUseCase;

class GetTotalIncomeUseCaseTest {

    @InjectMocks
    private GetTotalIncomeUseCase getTotalIncomeUseCase;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnTotalIncomeWhenAuthorIdIsValid() {
        // Arrange
        UUID authorId = UUID.randomUUID();
        BigDecimal expectedTotal = new BigDecimal("2500.50");
        when(transactionRepository.findTotalIncomeByAuthor(authorId)).thenReturn(expectedTotal);

        // Act
        BigDecimal actualTotal = getTotalIncomeUseCase.execute(authorId);

        // Assert
        assertEquals(expectedTotal, actualTotal);
        verify(transactionRepository, times(1)).findTotalIncomeByAuthor(authorId);
    }

    @Test
    void shouldReturnZeroWhenNoIncomeExistsForAuthor() {
        // Arrange
        UUID authorId = UUID.randomUUID();
        BigDecimal expectedTotal = BigDecimal.ZERO;
        when(transactionRepository.findTotalIncomeByAuthor(authorId)).thenReturn(expectedTotal);

        // Act
        BigDecimal actualTotal = getTotalIncomeUseCase.execute(authorId);

        // Assert
        assertEquals(expectedTotal, actualTotal);
        verify(transactionRepository, times(1)).findTotalIncomeByAuthor(authorId);
    }
}
