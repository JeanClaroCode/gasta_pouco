package br.com.jeanclaro.gasta_pouco.modules.Transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;

@Service
public class DeleteTransactionUseCase {

    @Autowired
    private TransactionRepository transactionRepository;

    public void execute(Integer id){
        transactionRepository.deleteById(id);
    }
}
