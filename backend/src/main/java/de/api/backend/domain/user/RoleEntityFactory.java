package de.api.backend.domain.user;

public class RoleEntityFactory {

    public static RoleEntity createRoleEntity(String name) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(RoleEnum.valueOf(name));
        return roleEntity;
    }
}
