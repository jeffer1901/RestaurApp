package com.example.restaurapp.config;

import com.example.restaurapp.usuario.UsuarioDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jfr.Category;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UsuarioDetailsService usuarioDetailsService;

    public JwtFilter(JwtUtil jwtUtil, UsuarioDetailsService usuarioDetailsService) {
        this.jwtUtil = jwtUtil;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String header = request.getHeader("Authorization");

        // 🔹 Mostrar información útil en consola
        System.out.println("\n------------------------------------");
        System.out.println("🔹 Ruta interceptada: " + path);
        System.out.println("🔹 Header Authorization: " + header);
        System.out.println("------------------------------------");

        // 🔓 Permitir rutas públicas
        if (path.startsWith("/auth")
                || path.startsWith("/usuarios/registro")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/swagger")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔐 Validar token
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                if (jwtUtil.validateToken(token)) {
                    Claims claims = jwtUtil.extractClaims(token);
                    String correo = claims.getSubject();
                    String rol = (String) claims.get("rol");

                    System.out.println("✅ Token válido para: " + correo);
                    System.out.println("🧩 Rol extraído del token: " + rol);

                    UserDetails userDetails = usuarioDetailsService.loadUserByUsername(correo);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    System.out.println("🔐 Autenticado en contexto con roles: " + userDetails.getAuthorities());
                } else {
                    System.out.println("❌ Token inválido o expirado.");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error procesando el token: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ No se encontró header Authorization válido.");
        }

        filterChain.doFilter(request, response);
    }
}