package br.com.jeanclaro.gasta_pouco.modules.Transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jeanclaro.gasta_pouco.exceptions.TransactionNotFoundException;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.TransactionType;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.dto.EditTransactionRequestDTO;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.entity.TransactionEntity;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;

@Service
public class EditTransactionUseCase {

    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionEntity execute(Integer id, EditTransactionRequestDTO editTransactionRequestDTO) {
        var transaction = this.transactionRepository.findById(id)
                .orElseThrow(() -> {
                    throw new TransactionNotFoundException();
                });

            TransactionType transactionType = TransactionType.valueOf(editTransactionRequestDTO.getType());

            Double amount = Double.parseDouble(editTransactionRequestDTO.getAmount());

                transaction.setType(transactionType);
                transaction.setAmount(amount);
                transaction.setCategory(editTransactionRequestDTO.getCategory());
                transaction.setDate(editTransactionRequestDTO.getDate());
                transaction.setDescription(editTransactionRequestDTO.getDescription());

        TransactionEntity updatedTransaction = this.transactionRepository.save(transaction);

        System.out.println("Transação atualizada: " + updatedTransaction);

        return updatedTransaction;
    }
}
