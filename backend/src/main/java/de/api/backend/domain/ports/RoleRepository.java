package de.api.backend.domain.ports;

import de.api.backend.domain.user.RoleEntity;
import de.api.backend.domain.user.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(RoleEnum role);
}
