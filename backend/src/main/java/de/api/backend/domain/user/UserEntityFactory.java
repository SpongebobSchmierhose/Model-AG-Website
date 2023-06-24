package de.api.backend.domain.user;

import java.util.Set;

public class UserEntityFactory {

    public static UserEntity createUserEntity(String fistName, String lastName, String username, String password, Set<RoleEntity> roles) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(fistName);
        userEntity.setLastName(lastName);
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setRoles(roles);
        return userEntity;
    }
}
