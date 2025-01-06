package br.com.jeanclaro.gasta_pouco.modules.User.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jeanclaro.gasta_pouco.modules.User.models.entity.UserEntity;


public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findById(UUID id);
    Optional<UserEntity> findByEmail(String email);
}
