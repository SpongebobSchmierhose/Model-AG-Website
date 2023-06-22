package de.api.backend.ui;

import de.api.backend.domain.user.RoleEntity;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class UserDto {
    @NotNull
    @NotEmpty
    private String fistName;
    @NotNull
    @NotEmpty
    private String lastName;
    @NotNull
    @NotEmpty
    private String username;
    @NotNull
    @NotEmpty
    private String password;
    private Set<RoleEntity> roles;
}
