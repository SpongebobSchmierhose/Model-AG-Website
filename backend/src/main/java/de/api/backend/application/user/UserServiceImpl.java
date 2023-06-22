package de.api.backend.application.user;

import de.api.backend.domain.ports.RoleRepository;
import de.api.backend.domain.ports.UserRepository;
import de.api.backend.domain.user.*;
import de.api.backend.ui.UserDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User not found in the database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.get().getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName().toString())));
        return new User(user.get().getUsername(), user.get().getPassword(), authorities);
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public RoleEntity saveRole(RoleEntity role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, RoleEnum rolename) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        Optional<RoleEntity> role = roleRepository.findByName(rolename);
        if(user.isPresent() && role.isPresent()) {
            user.get().getRoles().add(role.get());
        }
    }

    @Override
    public UserEntity getUser(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    @Override
    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void addPushTokenToUser(String username, String pushToken) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        user.ifPresent(userEntity -> userEntity.setExpoPushToken(pushToken));
    }

    @Override
    public UserEntity registerNewUser(UserDto userDto) throws Exception{
        Optional<UserEntity> existingUser = userRepository.findByUsername(userDto.getUsername());
        if(existingUser.isPresent()) {
            throw new Exception("There is already an account with that email address: "
                    + userDto.getUsername());
        }
        Optional<RoleEntity> role = roleRepository.findByName(RoleEnum.ROLE_USER);
        if(role.isEmpty()) {
            throw new Exception("Something went wrong");
        }
        Set<RoleEntity> roles = Collections.singleton(role.get());
        UserEntity user = UserEntityFactory.createUserEntity(userDto.getFistName(), userDto.getLastName(), userDto.getUsername(), userDto.getPassword(), roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
