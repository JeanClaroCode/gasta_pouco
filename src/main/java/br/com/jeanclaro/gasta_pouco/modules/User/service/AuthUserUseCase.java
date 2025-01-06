package br.com.jeanclaro.gasta_pouco.modules.User.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.jeanclaro.gasta_pouco.exceptions.AuthenticationException;
import br.com.jeanclaro.gasta_pouco.exceptions.UserNotFoundException;
import br.com.jeanclaro.gasta_pouco.modules.User.models.dto.AuthUserRequestDTO;
import br.com.jeanclaro.gasta_pouco.modules.User.models.dto.AuthUserResponseDTO;
import br.com.jeanclaro.gasta_pouco.modules.User.repository.UserRepository;

@Service
public class AuthUserUseCase {

    @Value("${security.token.secret.user}")
    private String secretKey;

    @Autowired
    private UserRepository  userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthUserResponseDTO execute(AuthUserRequestDTO authUserRequestDTO)throws AuthenticationException{
        var user = this.userRepository.findByEmail(authUserRequestDTO.email())
            .orElseThrow(() -> {
                throw new UserNotFoundException();
            });

        var passwordMatches = this.passwordEncoder.matches(authUserRequestDTO.password(), user.getPassword());

        if (!passwordMatches) {
            throw new AuthenticationException();
        }

        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalArgumentException("The secret key must not be null or blank");
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        var expiresIn = Instant.now().plus(Duration.ofHours(2));

        var token = JWT.create().withIssuer("gastapouco")
        .withExpiresAt(expiresIn)
        .withSubject(user.getId().toString())
        .withClaim("roles", List.of("USER"))
        .sign(algorithm);        

        // JWT.create()
        // .withIssuer("gastapouco")
        // .withSubject(user.getId().toString())
        // .withClaim("roles", Arrays.asList("USER"))
        // .withExpiresAt(expiresIn)
        // .sign(algorithm); 

        // var authUserResponseDTO = AuthUserResponseDTO.builder()
        // .email(user.getPassword())
        // .access_token(token)
        // .expires_in(expiresIn.toEpochMilli())
        // .build();

        var authUserResponseDTO = AuthUserResponseDTO.builder()
        .email(user.getEmail())
        .access_token(token.toString())
        .expires_in(expiresIn.toEpochMilli())
        .build();

        return authUserResponseDTO;
    }
}
