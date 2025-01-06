package br.com.jeanclaro.gasta_pouco.modules.transaction.useCases;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.entity.TransactionEntity;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.repository.TransactionRepository;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.GetTransactionByYearAndMonth;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetTransactionByYearAndMonthTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private GetTransactionByYearAndMonth getTransactionByYearAndMonth;

    @Mock
    private TransactionEntity transactionEntity;

    private UUID id;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
    }

    @Test
    void should_be_list_transaction_by_year_and_month(){
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAuthor(id);
        transactionEntity.setDate(LocalDate.of(2024, 5, 10));

        List<TransactionEntity> transactions = new ArrayList<>();
        transactions.add(transactionEntity);

        Integer year = 2024;
        Integer month = 05;

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1);

    when(transactionRepository.findByDateBetweenAndAuthor(startDate, endDate, id)).thenReturn(transactions);

    List<TransactionEntity> result = getTransactionByYearAndMonth.execute(year, month, id);

    assertEquals(1, result.size());
    assertEquals(id, result.get(0).getAuthor());    
    }

}
