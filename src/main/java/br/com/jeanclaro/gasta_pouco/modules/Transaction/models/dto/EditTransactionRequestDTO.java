package br.com.jeanclaro.gasta_pouco.modules.Transaction.models.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditTransactionRequestDTO {
    private String type;
    private String amount;
    private String category;
    private LocalDate date;
    private String description;
}
