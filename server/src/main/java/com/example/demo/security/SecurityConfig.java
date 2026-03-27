package com.example.demo.security;

import com.example.demo.service.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Configuration Spring Security 6 complète.
 * [POURQUOI] Stateless (pas de sessions HTTP) car l'authentification
 * repose entièrement sur les JWT.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // [POURQUOI] Désactivation CSRF car stateless API (pas de cookies de session)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // [POURQUOI] Stateless — chaque requête est authentifiée par son JWT
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoints publics (auth)
                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/login",
                    "/api/auth/verify-otp",
                    "/api/auth/google",
                    "/api/auth/refresh",
                    "/api/auth/forgot-password",
                    "/api/auth/reset-password"
                ).permitAll()
                // Endpoints de monitoring Spring Boot Admin
                .requestMatchers("/actuator/**").permitAll()
                // Tout le reste nécessite une authentification
                .anyRequest().authenticated()
            )
            // [POURQUOI] Le filtre JWT est exécuté AVANT le filtre d'authentification par défaut
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // [POURQUOI] BCrypt avec strength 12 — 2^12 itérations de hachage
        // Bon compromis entre sécurité et temps de calcul (~250ms)
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*")); // [POURQUOI] Permissif en dev — restreindre en prod
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // ═══════════════════════════════════════════════════════════
    //  JWT Authentication Filter
    // ═══════════════════════════════════════════════════════════

    /**
     * Filtre JWT exécuté sur chaque requête HTTP.
     * [POURQUOI] OncePerRequestFilter garantit une seule exécution par requête
     * même en cas de forwarding interne.
     */
    @Component
    public static class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtService jwtService;
        private final TokenBlacklistService tokenBlacklistService;

        public JwtAuthenticationFilter(JwtService jwtService,
                                       TokenBlacklistService tokenBlacklistService) {
            this.jwtService = jwtService;
            this.tokenBlacklistService = tokenBlacklistService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain)
                throws ServletException, IOException {

            String authHeader = request.getHeader("Authorization");

            // [POURQUOI] Si pas de header Authorization ou pas de Bearer, on passe au filtre suivant
            // C'est normal pour les endpoints publics
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);

            try {
                // Vérifier si le token est blacklisté (logout)
                if (tokenBlacklistService.isBlacklisted(token)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\":\"Token révoqué\"}");
                    return;
                }

                // Valider et parser le token
                if (jwtService.validateAccessToken(token)) {
                    Claims claims = jwtService.parseAccessToken(token);
                    UUID userId = UUID.fromString(claims.getSubject());
                    String role = claims.get("role", String.class);

                    // [POURQUOI] On stocke le userId dans le principal pour le récupérer
                    // dans les controllers via SecurityContextHolder
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userId,
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority(role))
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // [POURQUOI] On ne propage pas l'exception — le endpoint protégé
                // renverra un 401 automatiquement grâce à Spring Security
                logger.warn("Erreur JWT: " + e.getMessage());
            }

            filterChain.doFilter(request, response);
        }
    }
}
