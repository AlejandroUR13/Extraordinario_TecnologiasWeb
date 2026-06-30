package com.UPIIZ.extraordinario.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.UPIIZ.extraordinario.model.Usuario;
import com.UPIIZ.extraordinario.service.AuthService;

import jakarta.servlet.http.HttpSession;

/**
 * Controlador de autenticacion.
 *
 * Maneja el inicio de sesion (GET muestra el formulario,
 * POST valida credenciales) y el cierre de sesion.
 *
 * La sesion se gestiona directamente con HttpSession;
 * no se usa Spring Security.
 */
@Controller
public class AuthController {

    /** Clave del atributo de sesion que guarda al usuario autenticado. */
    public static final String SESSION_USUARIO = "usuarioSesion";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /** Redirige la raiz al login si no hay sesion, o al listado si ya esta autenticado. */
    @GetMapping("/")
    public String raiz(HttpSession session) {
        if (session.getAttribute(SESSION_USUARIO) != null) {
            return "redirect:/productos";
        }
        return "redirect:/login";
    }

    /** Muestra el formulario de inicio de sesion. */
    @GetMapping("/login")
    public String loginForm(HttpSession session) {
        // Si ya tiene sesion activa, manda directo al listado
        if (session.getAttribute(SESSION_USUARIO) != null) {
            return "redirect:/productos";
        }
        return "login";
    }

    /**
     * Procesa el formulario de inicio de sesion.
     *
     * Si las credenciales son validas crea la sesion y redirige
     * al listado de productos. En caso contrario regresa al login
     * con un mensaje de error.
     */
    @PostMapping("/login")
    public String loginPost(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirect) {

        Optional<Usuario> resultado = authService.autenticar(username, password);

        if (resultado.isPresent()) {
            session.setAttribute(SESSION_USUARIO, resultado.get());
            return "redirect:/productos";
        }

        redirect.addFlashAttribute("error", "Usuario o contraseña incorrectos.");
        return "redirect:/login";
    }

    /** Invalida la sesion actual y redirige al login. */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
