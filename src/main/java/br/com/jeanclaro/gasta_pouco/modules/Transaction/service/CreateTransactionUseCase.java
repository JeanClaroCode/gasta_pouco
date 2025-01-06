package br.com.jeanclaro.gasta_pouco.modules.Transaction.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jeanclaro.gasta_pouco.exceptions.UserNotFoundException;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.entity.TransactionEntity;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;
import br.com.jeanclaro.gasta_pouco.modules.User.repository.UserRepository;

@Service
public class CreateTransactionUseCase {

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private UserRepository userRepository;

    public TransactionEntity execute(UUID id, TransactionEntity transactionEntity){
        System.out.println("Buscando usuário com ID: " + id);
        var user = userRepository.findById(id).orElseThrow(() -> {
            throw new UserNotFoundException();
        });
        transactionEntity.setAuthor(user.getId());

        var savedTransaction = this.transactionRepository.save(transactionEntity);
        System.out.println("Transação salva: " + savedTransaction);
        return savedTransaction;
    }
}
