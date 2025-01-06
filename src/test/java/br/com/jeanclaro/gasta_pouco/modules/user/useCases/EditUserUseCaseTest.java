package br.com.jeanclaro.gasta_pouco.modules.user.useCases;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.jeanclaro.gasta_pouco.exceptions.AuthenticationException;
import br.com.jeanclaro.gasta_pouco.exceptions.UserFoundException;
import br.com.jeanclaro.gasta_pouco.modules.User.models.dto.EditUserRequestDTO;
import br.com.jeanclaro.gasta_pouco.modules.User.models.entity.UserEntity;
import br.com.jeanclaro.gasta_pouco.modules.User.repository.UserRepository;
import br.com.jeanclaro.gasta_pouco.modules.User.service.EditUserUseCase;

@ExtendWith(MockitoExtension.class)
class EditUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EditUserUseCase editUserUseCase;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setName("teste");
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("encodedPassword"); 
    }

    @Test
    void testeExecute_EditSuccess() {
        var editUserRequestDTO = new EditUserRequestDTO();
        editUserRequestDTO.setName("Jean Updated");
        editUserRequestDTO.setEmail("jean.updated@example.com");

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        var updatedUser = editUserUseCase.execute(userEntity.getId(), editUserRequestDTO);

        assertEquals("Jean Updated", updatedUser.getName());
        assertEquals("jean.updated@example.com", updatedUser.getEmail());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void shouldThrowUserFoundExceptionIfUserNotFound(){
        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.empty());

        assertThrows(UserFoundException.class, () -> editUserUseCase.execute(userEntity.getId(), new EditUserRequestDTO()));
    }

    @Test
    void shouldUpdatePasswordIfOldPasswordMatches(){
        var editUserRequestDTO = new EditUserRequestDTO();
        editUserRequestDTO.setPassword("oldPassword");
        editUserRequestDTO.setNewPassword("newPassword");

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("oldPassword", userEntity.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        
        var updatedUser = editUserUseCase.execute(userEntity.getId(), editUserRequestDTO);

        assertEquals("encodedNewPassword", updatedUser.getPassword());
        verify(passwordEncoder, times(1)).matches("oldPassword", "encodedPassword");
        verify(passwordEncoder, times(1)).encode("newPassword");
    }

    @Test
    void shouldThrowAuthenticationExceptionIfOldPasswordDoesNotMatch(){
        var editUserRequestDTO = new EditUserRequestDTO();
        editUserRequestDTO.setPassword("wrongPassword");
        editUserRequestDTO.setNewPassword("newPassword");

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("wrongPassword", userEntity.getPassword())).thenReturn(false);


        assertThrows(AuthenticationException.class, () -> editUserUseCase.execute(userEntity.getId(), editUserRequestDTO));
    }
}
