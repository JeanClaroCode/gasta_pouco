package br.com.jeanclaro.gasta_pouco.modules.transaction.useCases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.jeanclaro.gasta_pouco.exceptions.UserNotFoundException;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.TransactionType;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.entity.TransactionEntity;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.CreateTransactionUseCase;
import br.com.jeanclaro.gasta_pouco.modules.User.models.entity.UserEntity;
import br.com.jeanclaro.gasta_pouco.modules.User.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CreateTransactionUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateTransactionUseCase createTransactionUseCase;

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
    public void testExecute_CreateTransactionSuccess(){
        var user = new UserEntity();
        user.setId(UUID.randomUUID());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        transactionEntity.setAuthor(user.getId());
        when(transactionRepository.save(transactionEntity)).thenReturn(transactionEntity);

        var response = createTransactionUseCase.execute(user.getId(), transactionEntity);

        verify(userRepository, times(1)).findById(user.getId());
        verify(transactionRepository, times(1)).save(transactionEntity); 

        assertNotNull("Resposta deve ser não-nula", response);
        assertEquals("Id do usuário e do Author",user.getId(), response.getAuthor());    
    }

    @Test
    public void shouldThrowUserNotFoundExceptionifUserNotFound(){
        var user = new UserEntity();
        user.setId(UUID.randomUUID());
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        transactionEntity.setAuthor(user.getId());

        assertThrows(UserNotFoundException.class, () -> createTransactionUseCase.execute(user.getId(), transactionEntity));
    }

}
