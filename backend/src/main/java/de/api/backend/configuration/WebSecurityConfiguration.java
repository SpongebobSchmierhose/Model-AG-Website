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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

    //TODO DELETE IN PROD
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MyCustomDsl customDsl, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .csrf().disable().cors().configurationSource(corsConfigurationSource).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeHttpRequests().requestMatchers("/api/login/**", "/api/register/**", "/api/token/refresh/**", "/api/files/**").permitAll()
                .and().authorizeHttpRequests().requestMatchers(HttpMethod.GET, "/api/user/**", "/api/location/**", "/api/pushtoken/**").hasAnyAuthority(ROLE_USER.name(), ROLE_MODERATOR.name(), ROLE_ADMIN.name())
                .and().authorizeHttpRequests().requestMatchers(HttpMethod.POST, "/api/user/save/**").hasAnyAuthority(ROLE_ADMIN.name())
                .and().authorizeHttpRequests().anyRequest().authenticated()
                .and().apply(customDsl);
        return http.build();
    }
}

