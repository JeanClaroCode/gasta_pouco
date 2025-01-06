package br.com.jeanclaro.gasta_pouco.modules.User.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jeanclaro.gasta_pouco.modules.User.repository.UserRepository;

@Service
public class DeleteUserUseCase {

    @Autowired
    private UserRepository userRepository;

    public void execute(UUID id){
        userRepository.deleteById(id);
    }    
}
