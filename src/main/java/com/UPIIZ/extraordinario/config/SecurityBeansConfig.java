package com.UPIIZ.extraordinario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Provee el codificador de contrasenas usado al validar el login y al
 * sembrar los usuarios predefinidos. Se usa unicamente la utilidad de
 * hashing de Spring Security (spring-security-crypto); NO se activa
 * Spring Security ni sus filtros, la autenticacion la maneja la
 * aplicacion mediante HttpSession.
 */
@Configuration
public class SecurityBeansConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
