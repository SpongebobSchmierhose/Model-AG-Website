package de.api.backend.ui;

import de.api.backend.domain.user.RoleEnum;
import lombok.Data;

@Data
public class RoleToUserDto {
    private String username;
    private RoleEnum roleNames;
}
