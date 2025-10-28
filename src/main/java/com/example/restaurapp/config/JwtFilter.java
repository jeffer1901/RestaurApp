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

        System.out.println("🚀 Filtro JWT ACTIVADO");
        String path = request.getServletPath();
        System.out.println("Ruta interceptada: " + path);

        // Mostrar encabezado Authorization
        String header = request.getHeader("Authorization");
        System.out.println("Authorization header: " + header);

        // 🔓 Permitir rutas públicas sin token
        if (path.startsWith("/auth")
                || path.startsWith("/usuarios/registro")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/swagger")
                || path.equals("/v2/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔐 Validar JWT
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7).trim();
            System.out.println("🔍 Token recibido (sin espacios): " + token);

            boolean valido = jwtUtil.validateToken(token);
            System.out.println("🟢 ¿Token válido?: " + valido);

            if (valido) {
                String correo = jwtUtil.extractUsername(token);
                String rol = jwtUtil.extractClaims(token).get("rol", String.class);
                System.out.println("✅ Token válido para usuario: " + correo + " con rol: " + rol);

                UserDetails userDetails = usuarioDetailsService.loadUserByUsername(correo);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                System.out.println("❌ Token inválido o expirado");
            }
        } else {
            System.out.println("⚠️ No se envió encabezado Authorization o no empieza con 'Bearer '");
        }

        filterChain.doFilter(request, response);
    }
}