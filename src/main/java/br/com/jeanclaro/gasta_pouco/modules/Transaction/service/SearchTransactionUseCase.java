package br.com.jeanclaro.gasta_pouco.modules.Transaction.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.entity.TransactionEntity;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;

@Service
public class SearchTransactionUseCase {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<TransactionEntity> execute(String filter){
        return this.transactionRepository.findByDescriptionContainingIgnoreCase(filter);
    }
}
