package br.com.jeanclaro.gasta_pouco.modules.Transaction.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jeanclaro.gasta_pouco.exceptions.InvalidParamException;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.entity.TransactionEntity;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;

@Service
public class GetTransactionByYearAndMonth {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<TransactionEntity> execute(Integer year, Integer month, UUID authorId){
        if(month < 1 || month > 12){
            throw new InvalidParamException();
        }

        LocalDate startDate = LocalDate.of(year, month, 1);

        LocalDate endDate = startDate.plusMonths(1);

        return transactionRepository.findByDateBetweenAndAuthor(startDate, endDate, authorId);

    }
}
