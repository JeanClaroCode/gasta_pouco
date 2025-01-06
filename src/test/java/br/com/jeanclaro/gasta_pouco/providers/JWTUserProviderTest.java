package br.com.jeanclaro.gasta_pouco.providers;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTUserProviderTest {
    private JWTUserProvider jwtProvider;

    @BeforeEach
    void setup() {
        jwtProvider = new JWTUserProvider();
        ReflectionTestUtils.setField(jwtProvider, "secretKey", "my-secret-key");
    }

    @Test
    void should_return_decoded_jwt_when_token_is_valid() {
        String secretKey = "my-secret-key";
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String validToken = "Bearer " + JWT.create()
                .withSubject("user-id")
                .sign(algorithm);

        DecodedJWT decodedJWT = jwtProvider.validateToken(validToken);

        assertNotNull(decodedJWT);
        assertEquals("user-id", decodedJWT.getSubject());
    }

    @Test
    void should_return_null_when_token_is_invalid() {
        String invalidToken = "Bearer invalid-token";
        DecodedJWT decodedJWT = jwtProvider.validateToken(invalidToken);
        assertNull(decodedJWT);
    }

    @Test
    void should_return_null_when_token_does_not_start_with_bearer() {
        String invalidToken = "invalid-token";
        DecodedJWT decodedJWT = jwtProvider.validateToken(invalidToken);
        assertNull(decodedJWT);
    }
}
