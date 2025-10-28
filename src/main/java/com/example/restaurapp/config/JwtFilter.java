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
        System.out.println("🚀 Filtro JWT ACTIVADO para ruta: " + path);
        System.out.println("🔹 Authorization header: " + request.getHeader("Authorization"));

        // Permitir rutas públicas
        if (path.startsWith("/auth") ||
                path.startsWith("/usuarios/registro") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.equals("/swagger-ui.html") ||
                path.startsWith("/swagger") ||
                path.equals("/v2/api-docs")) {

            filterChain.doFilter(request, response);
            return;
        }

        // Validar token JWT
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            System.out.println("🟡 Token recibido: " + token);

            try {
                if (jwtUtil.validateToken(token)) {
                    Claims claims = jwtUtil.extractClaims(token);
                    String correo = claims.getSubject();
                    String rol = (String) claims.get("rol");

                    System.out.println("✅ Token válido para usuario: " + correo + " con rol: " + rol);

                    UserDetails userDetails = usuarioDetailsService.loadUserByUsername(correo);
                    System.out.println("🟢 UserDetails Authorities: " + userDetails.getAuthorities());

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    System.out.println("🔐 Usuario autenticado en contexto de seguridad: " + correo);
                } else {
                    System.out.println("❌ Token inválido o expirado.");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error al procesar token: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ No se encontró token JWT en la cabecera Authorization.");
        }

        filterChain.doFilter(request, response);
    }
}