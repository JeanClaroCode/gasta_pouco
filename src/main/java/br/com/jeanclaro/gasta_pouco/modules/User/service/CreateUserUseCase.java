package br.com.jeanclaro.gasta_pouco.modules.User.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.jeanclaro.gasta_pouco.exceptions.UserFoundException;
import br.com.jeanclaro.gasta_pouco.modules.User.models.entity.UserEntity;
import br.com.jeanclaro.gasta_pouco.modules.User.repository.UserRepository;

@Service
public class CreateUserUseCase {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity execute(UserEntity userEntity){
        this.userRepository.findByEmail(userEntity.getEmail())
        .ifPresent((user) -> {
            throw new UserFoundException();
        });
        var password = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(password);
        return this.userRepository.save(userEntity);
    }
}
