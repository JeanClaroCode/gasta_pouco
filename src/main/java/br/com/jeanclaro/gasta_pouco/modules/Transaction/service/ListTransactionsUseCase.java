package br.com.jeanclaro.gasta_pouco.modules.Transaction.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jeanclaro.gasta_pouco.exceptions.TransactionNotFoundException;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.entity.TransactionEntity;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;

@Service
public class ListTransactionsUseCase {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<TransactionEntity> execute(UUID id) {
        List<TransactionEntity> transactions = this.transactionRepository.findByAuthor(id);
        if (transactions.isEmpty()){
            throw new TransactionNotFoundException();
        }
        return transactions;
    }
}