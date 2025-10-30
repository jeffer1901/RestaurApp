package com.example.restaurapp.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ✅ Endpoints públicos
                        .requestMatchers(
                                "/auth/**",
                                "/usuarios/registro",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs.yaml"
                        ).permitAll()

                        // ✅ MESERO
                        // Solo puede ver y liberar mesas, ver productos y manejar sus pedidos
                        .requestMatchers(HttpMethod.GET, "/mesas/**").hasAnyRole("MESERO","ADMIN")
                        .requestMatchers("/mesas/liberar/**").hasAnyRole("MESERO","ADMIN")
                        .requestMatchers(HttpMethod.GET, "/productos/**").hasAnyRole("MESERO","ADMIN")
                        .requestMatchers("/pedidos/**").hasAnyRole("MESERO","ADMIN","COCINERO")

                        // ✅ COCINERO
                        .requestMatchers("/pedidos/estado/**").hasAnyRole("COCINERO","ADMIN")

                        // ✅ ADMIN (tiene acceso total)
                        .requestMatchers("/productos/**", "/mesas/**", "/usuarios/**", "/pedidos/**").hasRole("ADMIN")

                        // 🔒 Cualquier otra ruta autenticada
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
