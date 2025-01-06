package br.com.jeanclaro.gasta_pouco.providers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

class JWTProviderTest {

    private JWTProvider jwtProvider;

    @BeforeEach
    void setup() {
        jwtProvider = new JWTProvider();
        // Simula a injeção do valor da propriedade secretKey
        ReflectionTestUtils.setField(jwtProvider, "secretKey", "my-secret-key");
    }

    @Test
    void should_return_decoded_jwt_when_token_is_valid() {
        // Arrange: Criar um token válido usando a mesma chave secreta
        String secretKey = "my-secret-key";
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String validToken = "Bearer " + JWT.create()
                .withSubject("user-id")
                .sign(algorithm);

        // Act: Validar o token
        DecodedJWT decodedJWT = jwtProvider.validateToken(validToken);

        // Assert: Verificar que o token foi decodificado corretamente
        assertNotNull(decodedJWT);
        assertEquals("user-id", decodedJWT.getSubject());
    }

    @Test
    void should_return_null_when_token_is_invalid() {
        // Arrange: Criar um token inválido
        String invalidToken = "Bearer invalid-token";

        // Act: Validar o token
        DecodedJWT decodedJWT = jwtProvider.validateToken(invalidToken);

        // Assert: Verificar que a validação retornou nulo
        assertNull(decodedJWT);
    }

    @Test
    void should_return_null_when_token_does_not_start_with_bearer() {
        // Arrange: Criar um token sem o prefixo "Bearer"
        String invalidToken = "invalid-token";

        // Act: Validar o token
        DecodedJWT decodedJWT = jwtProvider.validateToken(invalidToken);

        // Assert: Verificar que a validação retornou nulo
        assertNull(decodedJWT);
    }
}



