package br.com.jeanclaro.gasta_pouco.modules.transaction.useCases;

import static org.junit.Assert.assertNotNull;
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

import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.GetTotalByYearAndMonthUseCase;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.GetTotalExpenseByYearAndMonthUseCase;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.GetTotalIncomeByYearAndMonthUseCase;

@ExtendWith(MockitoExtension.class)
class GetTotalByYearAndMonthUseCaseTest {
    
    @Mock
    private GetTotalExpenseByYearAndMonthUseCase getTotalExpenseByYearAndMonthUseCase;

    @Mock
    private GetTotalIncomeByYearAndMonthUseCase getTotalIncomeByYearAndMonthUseCase;

    @InjectMocks
    private GetTotalByYearAndMonthUseCase getTotalByYearAndMonthUseCase;

    private UUID id;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
    }

    @Test
    void should_be_able_to_list_total_by_year_and_mont(){
        Integer year = 2024;
        Integer month = 05;
        BigDecimal expectedIncome = BigDecimal.valueOf(500.00);
        BigDecimal expectedExpense = BigDecimal.valueOf(500.00);

        when(getTotalIncomeByYearAndMonthUseCase.execute(year, month, id)).thenReturn(expectedIncome);
        when(getTotalExpenseByYearAndMonthUseCase.execute(year, month, id)).thenReturn(expectedExpense);

        var actualTotal = getTotalByYearAndMonthUseCase.execute(year, month, id);

        assertNotNull("Assegurar que o valor é não-nulo", actualTotal);

    }
}
