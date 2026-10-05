package com.thiru.portfoliobackend.config;

import com.thiru.portfoliobackend.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Value("${frontend.url:${FRONTEND_URL:}}")
    private String frontendUrl;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public: CORS pre-flight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Public: admin login
                .requestMatchers(HttpMethod.POST, "/api/admin/login").permitAll()
                // Public: visitor submits contact form
                .requestMatchers(HttpMethod.POST, "/api/contact").permitAll()
                // Public: portfolio project reads
                .requestMatchers(HttpMethod.GET, "/api/projects").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/projects/**").permitAll()
                // Protected: admin-only contact message management
                .requestMatchers(HttpMethod.GET,    "/api/contact").authenticated()
                .requestMatchers(HttpMethod.GET,    "/api/contact/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/contact/**").authenticated()
                // Everything else is protected by default
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"message\":\"Unauthorized\"}");
                })
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        List<String> allowedOrigins = new ArrayList<>();
        allowedOrigins.add("http://localhost:3000");

        if (frontendUrl != null && !frontendUrl.trim().isEmpty()) {
            String[] origins = frontendUrl.split(",");
            for (String origin : origins) {
                String trimmed = origin.trim().replaceAll("/+$", "");
                if (!trimmed.isEmpty() && !allowedOrigins.contains(trimmed)) {
                    allowedOrigins.add(trimmed);
                }
            }
        }

        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
