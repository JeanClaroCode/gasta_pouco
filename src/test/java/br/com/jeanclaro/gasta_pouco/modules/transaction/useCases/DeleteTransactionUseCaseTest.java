package br.com.jeanclaro.gasta_pouco.modules.transaction.useCases;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.DeleteTransactionUseCase;

@ExtendWith(MockitoExtension.class)
class DeleteTransactionUseCaseTest {

    @InjectMocks
    private DeleteTransactionUseCase deleteTransactionUseCase;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldDeleteTransactionWhenIdExists() {
        Integer transactionId = 1;
        deleteTransactionUseCase.execute(transactionId);
        verify(transactionRepository, times(1)).deleteById(transactionId);
    }

    @Test
    void shouldNotThrowErrorWhenIdDoesNotExist() {
        Integer transactionId = 999; 
        doNothing().when(transactionRepository).deleteById(transactionId);
        deleteTransactionUseCase.execute(transactionId);
        verify(transactionRepository, times(1)).deleteById(transactionId);
    }
}
