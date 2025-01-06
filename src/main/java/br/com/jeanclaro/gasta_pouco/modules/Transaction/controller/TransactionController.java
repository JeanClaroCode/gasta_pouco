package br.com.jeanclaro.gasta_pouco.modules.Transaction.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.dto.EditTransactionRequestDTO;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.entity.TransactionEntity;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.CreateTransactionUseCase;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.DeleteTransactionUseCase;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.EditTransactionUseCase;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.GetTotalByYearAndMonthUseCase;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.GetTotalUseCase;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.GetTransactionByYearAndMonth;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.ListTransactionsUseCase;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.SearchTransactionUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/transaction")
@Tag(name = "Transações", description = "Gerenciamento de transações")
public class TransactionController {

    @Autowired
    private CreateTransactionUseCase createTransactionUseCase;

    @Autowired
    private SearchTransactionUseCase searchTransactionUseCase;

    @Autowired
    private EditTransactionUseCase editTransactionUseCase;

    @Autowired
    private ListTransactionsUseCase listTransactionsUseCase;

    @Autowired
    private GetTransactionByYearAndMonth getTransactionByYearAndMonth;

    @Autowired
    private GetTotalUseCase getTotalUseCase;

    @Autowired
    private GetTotalByYearAndMonthUseCase getTotalByYearAndMonthUseCase;

    @Autowired
    private DeleteTransactionUseCase deleteTransactionUseCase;

    @PostMapping("/")
    @PreAuthorize("hasRole('USER')")

    @Operation(summary = "Criar uma nova transação", description = "Cria uma nova transação financeira associada ao usuário autenticado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Criado com sucesso", content = {
            @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionEntity.class)))
        }),
        @ApiResponse(responseCode = "400", description = "Erro ao criar transação")
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> create(@Valid @RequestBody TransactionEntity transactionEntity, HttpServletRequest request) {
        var idCandidate = request.getAttribute("user_id");
        var idConverted = UUID.fromString(idCandidate.toString());

        try {
            var result = this.createTransactionUseCase.execute(idConverted, transactionEntity);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")

    @Operation(summary = "Buscar transações por descrição", description = "Busca transações financeiras que correspondem a uma descrição fornecida pelo usuário.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transação encontrada", content = {
            @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionEntity.class)))
        }),
        @ApiResponse(responseCode = "400", description = "Erro ao procurar transação")
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> searchTransaction(@RequestParam String query){
        try {
            List<TransactionEntity> transactions = searchTransactionUseCase.execute(query);
            return ResponseEntity.ok().body(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")

    @Operation(summary = "Editar uma transação existente", description = "Edita os detalhes de uma transação financeira identificada pelo ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transação editada", content = {
            @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionEntity.class)))
        }),
        @ApiResponse(responseCode = "400", description = "Erro ao editar transação")
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> editTransaction(@PathVariable ("id") Integer id, @RequestBody EditTransactionRequestDTO editTransactionRequestDTO){
        try {
            var result = editTransactionUseCase.execute(id, editTransactionRequestDTO);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('USER')")

    @Operation(summary = "Listar todas as transações do usuário", description = "Lista todas as transações financeiras associadas ao usuário autenticado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(
                array = @ArraySchema(schema = @Schema(implementation = TransactionEntity.class))
            )
        })
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> listTransactions(HttpServletRequest request){
        var idCandidate = request.getAttribute("user_id");
        var idConverted = UUID.fromString(idCandidate.toString());
        try {
            var result = listTransactionsUseCase.execute(idConverted);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{year}/{month}")
    @PreAuthorize("hasRole('USER')")

    @Operation(summary = "Buscar transações por ano e mês", description = "Lista as transações financeiras do usuário filtradas por um ano e mês específicos.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transação encontrada", content = {
            @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionEntity.class)))
        }),
        @ApiResponse(responseCode = "400", description = "Erro ao procurar transação")
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> getTransactionByYearAndMonth(@PathVariable("year") Integer year, @PathVariable("month") Integer month, HttpServletRequest request){
        var idCandidate = request.getAttribute("user_id");
        var idConverted = UUID.fromString(idCandidate.toString());
        try {
            var result = getTransactionByYearAndMonth.execute(year, month, idConverted);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/total")
    @PreAuthorize("hasRole('USER')")

    @Operation(summary = "Obter o valor total das transações", description = "Retorna a soma total dos valores de todas as transações do usuário autenticado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Valor Total das transações encontrado", content = {
            @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionEntity.class)))
        }),
        @ApiResponse(responseCode = "400", description = "Erro ao encontrar o Valor Total")
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> getTotal(HttpServletRequest request){
        var idCandidate = request.getAttribute("user_id");
        var idConverted = UUID.fromString(idCandidate.toString());
        try {
            var result = getTotalUseCase.execute(idConverted);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/total/{year}/{month}")
    @PreAuthorize("hasRole('USER')")

    @Operation(summary = "Obter o valor total por ano e mês", description = "Retorna a soma total dos valores das transações filtradas por um ano e mês específicos.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Valor Total das transações encontrado por mês e ano", content = {
            @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionEntity.class)))
        }),
        @ApiResponse(responseCode = "400", description = "Erro ao encontrar o Valor Total")
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> getTotalByYearAndMonth(@PathVariable("year") Integer year, @PathVariable("month") Integer month, HttpServletRequest request){
        var idCandidate = request.getAttribute("user_id");
        var idConverted = UUID.fromString(idCandidate.toString());
        try {
            var result = getTotalByYearAndMonthUseCase.execute(year, month, idConverted);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")

@Operation(summary = "Deletar uma transação", description = "Remove uma transação financeira identificada pelo ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Valor Total das transações encontrado por mês e ano", content = {
            @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionEntity.class)))
        }),
        @ApiResponse(responseCode = "400", description = "Erro ao encontrar o Valor Total")
    })
    @SecurityRequirement(name = "jwt_auth")
    public void deleteTransaction(@PathVariable ("id") Integer id){
        this.deleteTransactionUseCase.execute(id);
    }
}
