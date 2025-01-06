package br.com.jeanclaro.gasta_pouco.modules.transaction.useCases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.jeanclaro.gasta_pouco.exceptions.TransactionNotFoundException;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.TransactionType;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.dto.EditTransactionRequestDTO;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.entity.TransactionEntity;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.EditTransactionUseCase;
import br.com.jeanclaro.gasta_pouco.modules.User.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class EditTransactionUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EditTransactionUseCase editTransactionUseCase;

    private TransactionEntity transactionEntity; 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionEntity = new TransactionEntity();
        transactionEntity.setType(TransactionType.EXPENSE);
        transactionEntity.setAmount(10.00);
        transactionEntity.setCategory("Teste");
        transactionEntity.setDate(LocalDate.now());
        transactionEntity.setDescription("Descrição");
    }

    @Test
    void testeExecute_EditSuccess(){
        var editTransactionRequestDTO = new EditTransactionRequestDTO();
        editTransactionRequestDTO.setType(transactionEntity.getType().toString());
        editTransactionRequestDTO.setAmount(transactionEntity.getAmount().toString());
        editTransactionRequestDTO.setCategory(transactionEntity.getCategory());
        editTransactionRequestDTO.setDate(transactionEntity.getDate());
        editTransactionRequestDTO.setDescription(transactionEntity.getDescription());

        when(transactionRepository.findById(transactionEntity.getId())).thenReturn(Optional.of(transactionEntity));
        when(transactionRepository.save(transactionEntity)).thenReturn(transactionEntity);

        var result = editTransactionUseCase.execute(transactionEntity.getId(), editTransactionRequestDTO);

        assertNotNull(result, "O Resultado deve sere não-nulo");
        assertEquals("Deve ser igual id", transactionEntity.getId(),  result.getId());
    }

    @Test
    void shouldThrowTransactionNotFoundIfTransacationNotFound(){
        when(transactionRepository.findById(transactionEntity.getId())).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> editTransactionUseCase.execute(transactionEntity.getId(), new EditTransactionRequestDTO()));
    }
}
