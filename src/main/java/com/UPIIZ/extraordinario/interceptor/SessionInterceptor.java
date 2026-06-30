package com.UPIIZ.extraordinario.interceptor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.UPIIZ.extraordinario.controller.AuthController;
import com.UPIIZ.extraordinario.model.Rol;
import com.UPIIZ.extraordinario.model.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Interceptor de sesion.
 *
 * Ejecuta antes de cada peticion que coincida con las rutas
 * registradas en WebMvcConfig y realiza dos verificaciones:
 *
 * 1. Autenticacion: si no hay sesion activa redirige al login.
 * 2. Autorizacion por rol: si un Capturista intenta acceder a rutas
 *    exclusivas del Administrador (/productos/nuevo, /productos/editar,
 *    /productos/eliminar) recibe un 403 Forbidden.
 *
 * El examen exige que esta restriccion aplique incluso si el usuario
 * intenta acceder directamente mediante la URL correspondiente.
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {

    /** Rutas que solo puede acceder el Administrador. */
    private static final String[] RUTAS_ADMIN = {
        "/productos/nuevo",
        "/productos/guardar",
        "/productos/editar",
        "/productos/actualizar",
        "/productos/eliminar"
    };

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null)
                ? (Usuario) session.getAttribute(AuthController.SESSION_USUARIO)
                : null;

        // 1. Sin sesion → redirigir al login
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        // 2. Verificar permisos para rutas de administrador
        String uri = request.getRequestURI();
        if (esRutaAdmin(uri) && usuario.getRol() != Rol.ADMINISTRADOR) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "No tienes permisos para acceder a esta pagina.");
            return false;
        }

        return true;
    }

    private boolean esRutaAdmin(String uri) {
        for (String ruta : RUTAS_ADMIN) {
            if (uri.startsWith(ruta)) {
                return true;
            }
        }
        return false;
    }
}
