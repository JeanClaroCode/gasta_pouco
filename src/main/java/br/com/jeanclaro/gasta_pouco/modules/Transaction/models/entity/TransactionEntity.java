package br.com.jeanclaro.gasta_pouco.modules.Transaction.models.entity;

import java.time.LocalDate;
import java.util.UUID;

import br.com.jeanclaro.gasta_pouco.modules.Transaction.models.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tab_transaction")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O tipo da transação é obrigatório")
    private TransactionType type; 
    
    @NotNull(message = "O valor da transação é obrigatório")
    @Positive(message = "O valor da transação deve ser positivo")
    private Double amount;

    @NotNull(message = "A categoria é obrigatória")
    private String category;

    @NotNull(message = "A data é obrigatória")
    private LocalDate date;

    @NotNull(message = "A descrição é obrigatória")
    private String description;

    @Column(name = "user_id", nullable = false)
    private UUID author;
}
