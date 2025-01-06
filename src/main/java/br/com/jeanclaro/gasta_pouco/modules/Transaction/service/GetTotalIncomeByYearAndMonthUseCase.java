package br.com.jeanclaro.gasta_pouco.modules.Transaction.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;

@Service
public class GetTotalIncomeByYearAndMonthUseCase {

    @Autowired
    private TransactionRepository transactionRepository;
    
    public BigDecimal execute(Integer year, Integer month, UUID  authorId){
        LocalDate starDate = LocalDate.of(year, month, 1);
        LocalDate enDate = starDate.plusMonths(1);

        return transactionRepository.findTotalIncomeByMonthAndYear(starDate, enDate, authorId);
    }
}
