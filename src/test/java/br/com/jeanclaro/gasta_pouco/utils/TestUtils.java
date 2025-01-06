package br.com.jeanclaro.gasta_pouco.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TestUtils {

    public static String objectToJson(Object obj){
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String generatetoken(UUID idTransaction, String secret){
        Algorithm algorithm = Algorithm.HMAC256(secret);

        var expiresIn = Instant.now().plus(Duration.ofHours(2));

        var token  = JWT.create().withIssuer("gastapouco")
        .withExpiresAt(expiresIn)
        .withSubject(idTransaction.toString())
        .withClaim("roles", List.of("USER"))
        .sign(algorithm);
        return token;
    }   

    

}
