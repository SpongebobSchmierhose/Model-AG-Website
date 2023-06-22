package de.api.backend.application.utils;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;
@Component
public class AuthorityUtils {

    public Collection<SimpleGrantedAuthority> convertRolesToSimpleGrantedAuthoritys(String[] roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        return authorities;
    }
}
