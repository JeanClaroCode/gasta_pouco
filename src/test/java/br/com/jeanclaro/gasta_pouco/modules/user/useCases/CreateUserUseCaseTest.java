package br.com.jeanclaro.gasta_pouco.modules.user.useCases;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.jeanclaro.gasta_pouco.exceptions.UserFoundException;
import br.com.jeanclaro.gasta_pouco.modules.User.models.entity.UserEntity;
import br.com.jeanclaro.gasta_pouco.modules.User.repository.UserRepository;
import br.com.jeanclaro.gasta_pouco.modules.User.service.CreateUserUseCase;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("password123");
    }

    /**
     * 
     */
    @Test
    void testExecute_UserNotFound() {
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userEntity.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserEntity result = createUserUseCase.execute(userEntity);

        verify(userRepository, times(1)).save(userEntity);
        assertEquals("encodedPassword", result.getPassword());
        assertNotNull(result, "Retornado com sucesso");
    }

    @Test
    void testExecute_UserAlreadyExists() {
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(new UserEntity()));

        assertThrows(UserFoundException.class, () -> createUserUseCase.execute(userEntity));
        verify(userRepository, times(0)).save(any());
    }
}
