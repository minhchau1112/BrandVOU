package com.example.accountservice.config;

import com.example.accountservice.filter.AuthTokenFilter;
import com.example.accountservice.security.AuthEntryPointJwt;
import jakarta.ws.rs.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration // Indicates this class is a configuration class
@EnableWebSecurity // Enables Spring Security’s web security support
@RequiredArgsConstructor
@EnableMethodSecurity // Enables method-level security annotations such as @PreAuthorize
public class SecurityConfig {
    // Registers a session authentication strategy for managing sessions, using SessionRegistryImpl to track user sessions.
    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }
    // Define security filter chain configuration
    @Bean
    public SecurityFilterChain filterChain(
            final HttpSecurity httpSecurity,
            final AuthTokenFilter authTokenFilter,
            final AuthEntryPointJwt authEntryPointJwt
    ) throws Exception {
        // Configure security features
        httpSecurity
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(authEntryPointJwt)) // Handle authentication errors
//                .cors(customizer -> customizer.configurationSource(corsConfigurationSource())) // Set CORS configuration
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection for stateless authentication
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(HttpMethod.POST, "/api/v1/accounts/**").permitAll() // Allow POST requests to specific URL without authentication
                        .requestMatchers(HttpMethod.GET, "/api/v1/accounts/brands/**").hasAnyAuthority("ADMIN", "BRAND")
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Make sessions stateless for JWT usage
                .addFilterBefore(authTokenFilter, BearerTokenAuthenticationFilter.class); // Add custom JWT filter before default BearerToken filter

        return httpSecurity.build(); // Build the configured security filter chain
    }
    //Define CORS configuration source to allow all origins, methods, and headers
//    private CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
////        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:1110"));
//        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
//        configuration.setAllowedMethods(List.of("*"));
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
