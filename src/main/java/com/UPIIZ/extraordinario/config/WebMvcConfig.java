package com.UPIIZ.extraordinario.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.UPIIZ.extraordinario.interceptor.SessionInterceptor;

/**
 * Configuracion de Spring MVC.
 *
 * Registra:
 * - El interceptor de sesion aplicado a todas las rutas excepto /login y los
 *   recursos estaticos (css, js, imagenes).
 * - El manejador de recursos estaticos para servir los archivos de Sneat.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final SessionInterceptor sessionInterceptor;

    public WebMvcConfig(SessionInterceptor sessionInterceptor) {
        this.sessionInterceptor = sessionInterceptor;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                // Rutas protegidas: todo menos login, logout y recursos estaticos
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login",
                        "/logout",
                        "/assets/**",
                        "/error"
                );
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Sirve los archivos de Sneat desde src/main/resources/static/assets/
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");
    }
}
