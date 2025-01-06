package br.com.jeanclaro.gasta_pouco.modules.Transaction.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;

@Service
public class GetTotalIncomeUseCase {

    @Autowired
    private TransactionRepository transactionRepository;
    
    public BigDecimal execute(UUID authorId){
        return transactionRepository.findTotalIncomeByAuthor(authorId);
    }
}
