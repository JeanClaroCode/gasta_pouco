package br.com.jeanclaro.gasta_pouco.modules.User.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserResponseDTO {
    private String email;
    private String access_token; 
    private Long expires_in;
}
