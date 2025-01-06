package br.com.jeanclaro.gasta_pouco.modules.user.useCases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.jeanclaro.gasta_pouco.exceptions.AuthenticationException;
import br.com.jeanclaro.gasta_pouco.exceptions.UserNotFoundException;
import br.com.jeanclaro.gasta_pouco.modules.User.models.dto.AuthUserRequestDTO;
import br.com.jeanclaro.gasta_pouco.modules.User.models.dto.AuthUserResponseDTO;
import br.com.jeanclaro.gasta_pouco.modules.User.models.entity.UserEntity;
import br.com.jeanclaro.gasta_pouco.modules.User.repository.UserRepository;
import br.com.jeanclaro.gasta_pouco.modules.User.service.AuthUserUseCase;
import br.com.jeanclaro.gasta_pouco.utils.TestUtils;


@TestPropertySource(properties = {
    "jwt.secret=myTestSecretKey"
})

@ExtendWith(MockitoExtension.class)
class AuthUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthUserUseCase authUserUseCase;

    @Mock
    private UserEntity userEntity;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Garante que os mocks sejam injetados
        userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("encodedPassword");
        ReflectionTestUtils.setField(authUserUseCase, "secretKey", "myTestSecretKey");

    }

    @Test
    @DisplayName("Deve autenticar com sucesso quando o usuário é encontrado e a senha está correta")
    void testExecute_Success() {
        var email = userEntity.getEmail();
        var password = "password123";
        var request = new AuthUserRequestDTO(email, password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(password, userEntity.getPassword())).thenReturn(true);

        AuthUserResponseDTO response = authUserUseCase.execute(request);

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, userEntity.getPassword());

        assertNotNull(response, "Resposta deve ser não-nula");
        assertEquals("O e-mail deve coincidir", email, response.getEmail());
        assertNotNull(response.getAccess_token(), "O token de acesso deve ser não-nulo");
    }

    @Test
    void testExecute_UserNotFound(){
        var email = "nonexistent@example.com";
        var password = "password123";
        var request = new AuthUserRequestDTO(email, password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundException.class,() -> authUserUseCase.execute(request));
        
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test   
    void testExecute_InvalidPassword(){
        var email = "test@example.com";
        var password = "password123";
        var wrongPassword = "wrongPassword";
        var user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setEmail(email);
        user.setPassword("encodedPassword");

        var request = new AuthUserRequestDTO(email, wrongPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(wrongPassword, user.getPassword())).thenReturn(false);

        assertThrows(AuthenticationException.class,() ->  authUserUseCase.execute(request));

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(wrongPassword, user.getPassword());
    }
}
