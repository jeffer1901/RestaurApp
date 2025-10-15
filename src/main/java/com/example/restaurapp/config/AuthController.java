package com.example.restaurapp.config;

import com.example.restaurapp.usuario.Usuario;
import com.example.restaurapp.usuario.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String correo = request.get("correo");
        String contrasena = request.get("contrasena");

        try {
            System.out.println("🔹 Intentando autenticar usuario: " + correo);

            Usuario usuario = usuarioRepository.findByCorreo(correo)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // ✅ Verificación flexible (acepta hash o texto plano)
            boolean coincide;
            if (usuario.getPassword().startsWith("$2a$")) { // Es un hash de BCrypt
                coincide = passwordEncoder.matches(contrasena, usuario.getPassword());
            } else {
                coincide = usuario.getPassword().equals(contrasena);
            }

            if (!coincide) {
                System.out.println("Contraseña incorrecta para: " + correo);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Credenciales inválidas"));
            }

            // 🔑 Generar token
            String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getRol().name());
            System.out.println("✅ Usuario autenticado correctamente: " + usuario.getCorreo());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "rol", usuario.getRol().name()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno en el login: " + e.getMessage()));
        }
    }
}
