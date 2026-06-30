package com.UPIIZ.extraordinario.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.UPIIZ.extraordinario.model.Usuario;
import com.UPIIZ.extraordinario.repository.UsuarioRepository;

/**
 * Servicio de autenticacion.
 *
 * Valida credenciales buscando al usuario en la base de datos y
 * comparando la contrasena ingresada contra el hash BCrypt almacenado.
 * No usa Spring Security; la sesion se maneja directamente con HttpSession.
 */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Intenta autenticar al usuario.
     *
     * @param username nombre de usuario ingresado
     * @param rawPassword contrasena en texto plano ingresada
     * @return Optional con el Usuario si las credenciales son validas,
     *         Optional.empty() en caso contrario
     */
    public Optional<Usuario> autenticar(String username, String rawPassword) {
        Optional<Usuario> opt = usuarioRepository.findByUsername(username);
        if (opt.isPresent()) {
            Usuario u = opt.get();
            if (passwordEncoder.matches(rawPassword, u.getPassword())) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }
}
