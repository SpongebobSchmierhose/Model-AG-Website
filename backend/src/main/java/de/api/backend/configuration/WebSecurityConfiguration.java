package de.api.backend.configuration;

import de.api.backend.application.user.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static de.api.backend.domain.user.RoleEnum.*;


@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserServiceImpl userService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MyCustomDsl customDsl) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeHttpRequests().requestMatchers("/api/login/**", "/api/register/**", "/api/token/refresh/**", "/api/files/**").permitAll()
                .and().authorizeHttpRequests().requestMatchers(HttpMethod.GET, "/api/user/**", "/api/location/**", "/api/pushtoken/**").hasAnyAuthority(ROLE_USER.name(), ROLE_MODERATOR.name(), ROLE_ADMIN.name())
                .and().authorizeHttpRequests().requestMatchers(HttpMethod.POST, "/api/user/save/**").hasAnyAuthority(ROLE_ADMIN.name())
                .and().authorizeHttpRequests().anyRequest().authenticated()
                .and().apply(customDsl);
        return http.build();
    }
}

