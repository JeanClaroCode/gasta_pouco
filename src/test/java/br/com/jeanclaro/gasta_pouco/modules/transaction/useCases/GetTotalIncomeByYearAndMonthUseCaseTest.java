package br.com.jeanclaro.gasta_pouco.modules.transaction.useCases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.GetTotalIncomeByYearAndMonthUseCase;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetTotalIncomeByYearAndMonthUseCaseTest {
    
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private GetTotalIncomeByYearAndMonthUseCase getTotalIncomeByYearAndMonthUseCase;

    private UUID id;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
    }

    @Test
    void should_be_list_income_by_year_and_month(){
        Integer year = 2024;
        Integer month = 05;
        BigDecimal expectedTotal = BigDecimal.valueOf(500.00);

        LocalDate starDate = LocalDate.of(year, month, 1);
        LocalDate endDate = starDate.plusMonths(1);

        when(transactionRepository.findTotalIncomeByMonthAndYear(starDate, endDate, id)).thenReturn(expectedTotal);

        BigDecimal actualTotal = getTotalIncomeByYearAndMonthUseCase.execute(year, month, id);
        
        assertEquals(expectedTotal, actualTotal);
        assertNotNull("Assegurar que o valor é não-nulo", actualTotal);
        verify(transactionRepository, times(1)).findTotalIncomeByMonthAndYear(starDate, endDate, id);
    }

    @Test
    void should_return_zero_when_no_income_found() {
        // Arrange: Quando não houver despesas para o mês
        Integer year = 2024;
        Integer month = 5;
        BigDecimal expectedTotal = BigDecimal.ZERO;

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1);

        // Mock do método do repositório retornando zero
        when(transactionRepository.findTotalIncomeByMonthAndYear(startDate, endDate, id))
                .thenReturn(expectedTotal);

        // Act: Chama o caso de uso
        BigDecimal actualTotal = getTotalIncomeByYearAndMonthUseCase.execute(year, month, id);

        // Assert: Verifica se o valor retornado é zero
        assertEquals(expectedTotal, actualTotal);

        // Verifica se o método do repositório foi chamado uma vez
        verify(transactionRepository, times(1))
                .findTotalIncomeByMonthAndYear(startDate, endDate, id);
    }
}
