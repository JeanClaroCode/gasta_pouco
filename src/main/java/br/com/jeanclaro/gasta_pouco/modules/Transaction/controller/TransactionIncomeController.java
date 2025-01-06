package br.com.jeanclaro.gasta_pouco.modules.Transaction.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.GetTotalIncomeByYearAndMonthUseCase;
import br.com.jeanclaro.gasta_pouco.modules.Transaction.service.GetTotalIncomeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transaction/income")
@Tag(name = "Ganhos", description = "Gerenciamento de ganhos")
public class TransactionIncomeController {

    @Autowired
    private GetTotalIncomeByYearAndMonthUseCase getTotalIncomeByYearAndMonthUseCase;

    @Autowired
    private GetTotalIncomeUseCase getTotalIncomeUseCase;

    @GetMapping("/total")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Obter total de ganhos", description = "Retorna o total de ganhos do usuário.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Total de ganhos obtido com sucesso", content = {
            @Content(schema = @Schema(type = "number"))
        }),
        @ApiResponse(responseCode = "400", description = "Erro ao obter total de ganhos", content = {
            @Content(schema = @Schema(type = "string"))
        })
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> getTotalIncome(HttpServletRequest request){
        var idCandidate = request.getAttribute("user_id");
        var idConverted = UUID.fromString(idCandidate.toString());
        try {
            var result = this.getTotalIncomeUseCase.execute(idConverted);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/total/{year}/{month}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Obter total de ganhos por ano e mês", description = "Retorna o total de ganhos do usuário para um ano e mês específicos.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Total de ganhos obtido com sucesso", content = {
            @Content(schema = @Schema(type = "number"))
        }),
        @ApiResponse(responseCode = "400", description = "Erro ao obter total de ganhos por ano e mês", content = {
            @Content(schema = @Schema(type = "string"))
        })
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> getTotalIncomeByYearAndMonth(@PathVariable("year") Integer year, @PathVariable("month") Integer month, HttpServletRequest request){
        var idCandidate = request.getAttribute("user_id");
        var idConverted = UUID.fromString(idCandidate.toString());
        try {
            var result = this.getTotalIncomeByYearAndMonthUseCase.execute(year, month, idConverted);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
