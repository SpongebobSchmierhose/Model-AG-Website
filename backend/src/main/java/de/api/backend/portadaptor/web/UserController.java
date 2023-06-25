package de.api.backend.portadaptor.web;

import de.api.backend.application.user.UserService;
import de.api.backend.application.utils.JwtUtils;
import de.api.backend.application.utils.ResponseUtils;
import de.api.backend.domain.user.RoleEntity;
import de.api.backend.domain.user.RoleEntityFactory;
import de.api.backend.domain.user.UserEntity;
import de.api.backend.domain.user.UserEntityFactory;
import de.api.backend.ui.RoleDto;
import de.api.backend.ui.RoleToUserDto;
import de.api.backend.ui.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final JwtUtils jwtUtils;

    private final ResponseUtils responseUtils;

    @PostMapping("/register")
    public void register(HttpServletRequest request, HttpServletResponse response, @Valid UserDto userDto) throws Exception {
        try {
            UserEntity user = userService.registerNewUser(userDto);
            String access_token = jwtUtils.createAccessToken(user, request);
            String refresh_token = jwtUtils.createRefreshToken(user, request);
            responseUtils.sendTokens(access_token, refresh_token, response);
        }
        catch (Exception exception) {
            responseUtils.sendErrorResponse(exception, response, BAD_REQUEST);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<UserEntity>getUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String refresh_token = authorizationHeader.substring("Bearer ".length());
        String username = jwtUtils.getUsernameFromToken(refresh_token);
        UserEntity user = userService.getUser(username);
        return ResponseEntity.ok().body(user);
}

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>>getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<UserEntity>saveUser(@RequestBody UserDto userDto) {
        UserEntity user = UserEntityFactory.createUserEntity(userDto.getFistName(), userDto.getLastName(), userDto.getUsername(), userDto.getPassword(), userDto.getRoles());
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<RoleEntity>saveRole(@RequestBody RoleDto roleDto) {
        RoleEntity role = RoleEntityFactory.createRoleEntity(roleDto.getName());
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?>addRoleToUser(@RequestBody RoleToUserDto roleToUserDto) {
        userService.addRoleToUser(roleToUserDto.getUsername(), roleToUserDto.getRoleNames());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                String username = jwtUtils.getUsernameFromToken(refresh_token);
                UserEntity user = userService.getUser(username);
                String access_token = jwtUtils.createAccessToken(user, request);
                responseUtils.sendTokens(access_token, refresh_token, response);
            } catch (Exception exception) {
                responseUtils.sendErrorResponse(exception, response, FORBIDDEN);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
