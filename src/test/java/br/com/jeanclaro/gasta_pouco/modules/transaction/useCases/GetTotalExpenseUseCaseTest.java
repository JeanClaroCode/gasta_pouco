package br.com.jeanclaro.gasta_pouco.modules.transaction.useCases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.GetTotalExpenseUseCase;

@ExtendWith(MockitoExtension.class)
public class GetTotalExpenseUseCaseTest {
    @InjectMocks
    private GetTotalExpenseUseCase getTotalExpenseUseCase;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnTotalExpenseWhenAuthorIdIsValid() {
        // Arrange
        UUID authorId = UUID.randomUUID();
        BigDecimal expectedTotal = new BigDecimal("1500.75");
        when(transactionRepository.findTotalExpenseByAuthor(authorId)).thenReturn(expectedTotal);

        // Act
        BigDecimal actualTotal = getTotalExpenseUseCase.execute(authorId);

        // Assert
        assertEquals(expectedTotal, actualTotal);
        verify(transactionRepository, times(1)).findTotalExpenseByAuthor(authorId);
    }

    @Test
    void shouldReturnZeroWhenNoExpensesExistForAuthor() {
        // Arrange
        UUID authorId = UUID.randomUUID();
        BigDecimal expectedTotal = BigDecimal.ZERO;
        when(transactionRepository.findTotalExpenseByAuthor(authorId)).thenReturn(expectedTotal);

        // Act
        BigDecimal actualTotal = getTotalExpenseUseCase.execute(authorId);

        // Assert
        assertEquals(expectedTotal, actualTotal);
        verify(transactionRepository, times(1)).findTotalExpenseByAuthor(authorId);
    }
}
