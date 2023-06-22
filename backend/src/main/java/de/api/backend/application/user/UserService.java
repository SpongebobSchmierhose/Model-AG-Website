package de.api.backend.application.user;

import de.api.backend.domain.user.RoleEntity;
import de.api.backend.domain.user.RoleEnum;
import de.api.backend.domain.user.UserEntity;
import de.api.backend.ui.UserDto;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserEntity saveUser(UserEntity user);
    RoleEntity saveRole(RoleEntity role);
    void addRoleToUser(String username, RoleEnum rolename);
    UserEntity getUser(String username);
    List<UserEntity>getUsers();
    void addPushTokenToUser(String username, String pushToken);
    UserEntity registerNewUser(UserDto userDto) throws Exception;
}
