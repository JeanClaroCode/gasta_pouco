package br.com.jeanclaro.gasta_pouco.modules.Transaction.service;

import java.math.BigDecimal;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GetTotalByYearAndMonthUseCase {

    
    @Autowired
    private GetTotalIncomeByYearAndMonthUseCase getTotalIncomeByYearAndMonthUseCase;

    @Autowired
    private GetTotalExpenseByYearAndMonthUseCase getTotalExpenseByYearAndMonthUseCase;

    public Object execute(Integer year, Integer month, UUID  authorId){

        var income = this.getTotalIncomeByYearAndMonthUseCase.execute(year, month, authorId);
        System.out.println("INCOME: " + income);
        var expense = this.getTotalExpenseByYearAndMonthUseCase.execute(year, month, authorId); 
        System.out.println("EXPENSE: " + expense);

        return total(income, expense);
    }

    private BigDecimal total(BigDecimal income, BigDecimal expense) {
        return expense.subtract(income);
    }
}
