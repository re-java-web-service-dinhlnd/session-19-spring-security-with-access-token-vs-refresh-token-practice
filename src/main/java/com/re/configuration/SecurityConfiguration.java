package com.re.configuration;

import com.re.security.AuthenticationFilter;
import com.re.security.ForbiddenEntryPoint;
import com.re.security.UnauthorizedEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UnauthorizedEntryPoint unauthorizedEntryPoint;
    private final ForbiddenEntryPoint forbiddenEntryPoint;
    private final UserDetailsService userDetailsService;
    private final AuthenticationFilter authenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(unauthorizedEntryPoint) // 401
                        .accessDeniedHandler(forbiddenEntryPoint) // 403
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. Cho phép truy cập công khai các API login/register
                        .requestMatchers("/api/auth/**").permitAll()
                        // 2. Các API liên quan đến Admin (nếu bạn có prefix riêng)
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // 3. Với các API còn lại (/api/users/**, /api/roles/**...)
                        // Hãy để nó ở mức "Any authenticated user"
                        // Việc phân quyền chi tiết (Admin hay User) sẽ do @PreAuthorize ở Controller quyết định
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * AuthenticationProvider is where userDetailsService is actually called
     * to verify user information from the database.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Expose AuthenticationManager as a Bean to perform manual authentication in the Controller/Service
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
