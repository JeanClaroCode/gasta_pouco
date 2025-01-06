package br.com.jeanclaro.gasta_pouco.modules.transaction.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.jeanclaro.gasta_pouco.modules.Transaction.controller.TransactionController;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.TransactionType;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.entity.TransactionEntity;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.CreateTransactionUseCase;
import br.com.jeanclaro.gasta_pouco.modules.User.models.entity.UserEntity;
import br.com.jeanclaro.gasta_pouco.utils.TestUtils;
import org.springframework.http.MediaType;


@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

        private MockMvc mvc;

        @Mock
        private CreateTransactionUseCase createTransactionUseCase;

        @InjectMocks
        private TransactionController transactionController;


        @BeforeEach
        void setup() {
                mvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        }

        @Test
        void should_be_able_to_create_a_new_transaction() throws Exception {
        var user = UserEntity.builder()
                .name("USER_NAME")
                .email("user@gmail.com")
                .password("1234567890")
                .build();

        //user = userRepository.saveAndFlush(user);
        
        TransactionEntity transaction = TransactionEntity.builder()
                .type(TransactionType.INCOME)
                .amount(100.0)
                .category("Salário")
                .date(LocalDate.now())
                .description("Recebimento de salário")
                .build();

                when(createTransactionUseCase.execute(any(), any())).thenReturn(transaction);

        mvc.perform(MockMvcRequestBuilders.post("/transaction/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(transaction))
                .header("Authorization", "Bearer " + TestUtils.generatetoken(user.getId(), "GASTAPOUCO_@123")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("Salário"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(TransactionType.INCOME.toString()));
        }

        @Test
        void should_not_be_able_to_create_a_new_transaction_if_user_not_found() throws Exception {
        TransactionEntity transaction = TransactionEntity.builder()
                .type(TransactionType.INCOME)
                .amount(100.0)
                .category("Salário")
                .date(LocalDate.now())
                .description("Recebimento de salário")
                .build();

        mvc.perform(MockMvcRequestBuilders.post("/transaction/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(transaction))
                .header("Authorization", "Bearer " + TestUtils.generatetoken(UUID.randomUUID(), "GASTAPOUCO_@123")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }





}
