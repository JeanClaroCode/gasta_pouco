package br.com.jeanclaro.gasta_pouco.modules.Transaction.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;

@Service
public class GetTotalExpenseByYearAndMonthUseCase {

    @Autowired
    private TransactionRepository transactionRepository;

    public BigDecimal execute(Integer year, Integer month, UUID  authorId){
        
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1);
        
        return transactionRepository.findTotalExpenseByMonthAndYear(startDate,endDate, authorId);
    }
}
