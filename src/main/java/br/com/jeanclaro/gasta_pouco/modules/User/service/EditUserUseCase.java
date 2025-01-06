package br.com.jeanclaro.gasta_pouco.modules.User.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.jeanclaro.gasta_pouco.exceptions.AuthenticationException;
import br.com.jeanclaro.gasta_pouco.exceptions.UserFoundException;
import br.com.jeanclaro.gasta_pouco.modules.User.models.dto.EditUserRequestDTO;
import br.com.jeanclaro.gasta_pouco.modules.User.models.entity.UserEntity;
import br.com.jeanclaro.gasta_pouco.modules.User.repository.UserRepository;

@Service
public class EditUserUseCase {
    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private PasswordEncoder passwordEncoder;

    public UserEntity execute(UUID id, EditUserRequestDTO editUserRequestDTO){
        var user = this.userRepository.findById(id)
        .orElseThrow(()->{
            throw new UserFoundException();
        });
        if (editUserRequestDTO.getName() != null) {
            user.setName(editUserRequestDTO.getName());
        }
        if (editUserRequestDTO.getEmail() != null) {
            user.setEmail(editUserRequestDTO.getEmail());
        }
        if (editUserRequestDTO.getPassword() != null && editUserRequestDTO.getNewPassword() != null) {
            boolean isMatch = passwordEncoder.matches(editUserRequestDTO.getPassword(), user.getPassword());
            if (!isMatch) {
                throw new AuthenticationException();
            }
            var passwordEncoded = passwordEncoder.encode(editUserRequestDTO.getNewPassword());
            user.setPassword(passwordEncoded);
        }
        user.setUpdatedAt(LocalDate.now());
        return this.userRepository.save(user);
    }
    
}
