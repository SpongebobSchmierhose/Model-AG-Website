package de.api.backend.domain.ports;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.api.backend.domain.user.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);}
