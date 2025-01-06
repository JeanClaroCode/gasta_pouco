package br.com.jeanclaro.gasta_pouco.modules.Transaction.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.GetTotalExpenseByYearAndMonthUseCase;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.GetTotalExpenseUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transaction/expense")
@Tag(name = "Despesas", description = "Gerenciamento de despesas")
public class TransactionExpenseController {
    @Autowired
    private GetTotalExpenseByYearAndMonthUseCase getTotalExpenseByYearAndMonthUseCase;

    @Autowired
    private GetTotalExpenseUseCase getTotalExpenseUseCase;

    @GetMapping("/total")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Obter total de despesas", description = "Retorna o total de despesas do usuário.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Total de despesas obtido com sucesso", content = {
            @Content(schema = @Schema(type = "number"))
        }),
        @ApiResponse(responseCode = "400", description = "Erro ao obter total de despesas", content = {
            @Content(schema = @Schema(type = "string"))
        })
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> getTotalExpense(HttpServletRequest request){
        var idCandidate = request.getAttribute("user_id");
        var idConverted = UUID.fromString(idCandidate.toString());
        try {
            var result = this.getTotalExpenseUseCase.execute(idConverted);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/total/{year}/{month}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Obter total de despesas por ano e mês", description = "Retorna o total de despesas do usuário para um ano e mês específicos.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Total de despesas obtido com sucesso", content = {
            @Content(schema = @Schema(type = "number"))
        }),
        @ApiResponse(responseCode = "400", description = "Erro ao obter total de despesas por ano e mês", content = {
            @Content(schema = @Schema(type = "string"))
        })
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> getTotalExpenseByYearAndMonth(@PathVariable("year") Integer year, @PathVariable("month") Integer month, HttpServletRequest request){
        var idCandidate = request.getAttribute("user_id");
        var idConverted = UUID.fromString(idCandidate.toString());
        try {
            var result = this.getTotalExpenseByYearAndMonthUseCase.execute(year, month, idConverted);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
