package de.api.backend.configuration;

import de.api.backend.application.utils.AuthorityUtils;
import de.api.backend.application.utils.JwtUtils;
import de.api.backend.configuration.filter.CustomAuthenticationFilter;
import de.api.backend.configuration.filter.CustomAuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component @AllArgsConstructor
public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {

    private final JwtUtils jwtUtils;
    private final AuthorityUtils authorityUtils;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager, jwtUtils);
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(jwtUtils, authorityUtils), UsernamePasswordAuthenticationFilter.class);
    }
}
