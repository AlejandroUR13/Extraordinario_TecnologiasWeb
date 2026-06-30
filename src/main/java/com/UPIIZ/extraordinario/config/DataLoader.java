package com.UPIIZ.extraordinario.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.UPIIZ.extraordinario.model.Producto;
import com.UPIIZ.extraordinario.model.Rol;
import com.UPIIZ.extraordinario.model.Usuario;
import com.UPIIZ.extraordinario.repository.ProductoRepository;
import com.UPIIZ.extraordinario.repository.UsuarioRepository;

/**
 * Siembra datos iniciales al arrancar la aplicacion:
 * - Los 2 usuarios predefinidos requeridos por el examen.
 * - Un par de productos de ejemplo para poder probar el listado de
 *   inmediato (puedes borrarlos o ampliarlos desde la propia app).
 *
 * Solo inserta datos si las tablas estan vacias, asi que es seguro
 * reiniciar la aplicacion varias veces sin duplicar informacion.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UsuarioRepository usuarioRepository, ProductoRepository productoRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        sembrarUsuarios();
        sembrarProductos();
    }

    private void sembrarUsuarios() {
        if (usuarioRepository.count() > 0) {
            return;
        }

        Usuario admin = new Usuario(
                "admin",
                passwordEncoder.encode("admin123"),
                "Administrador",
                Rol.ADMINISTRADOR);

        Usuario capturista = new Usuario(
                "capturista",
                passwordEncoder.encode("captura123"),
                "Capturista",
                Rol.CAPTURISTA);

        usuarioRepository.save(admin);
        usuarioRepository.save(capturista);
    }

    private void sembrarProductos() {
        if (productoRepository.count() > 0) {
            return;
        }

        productoRepository.save(new Producto("Laptop Lenovo ThinkPad E14", "Laptop Core i7",
                new BigDecimal("18500.00"), 10, "Computadoras", LocalDate.of(2024, 5, 12), null));

        productoRepository.save(new Producto("Mouse Inalambrico Logitech M185", "Mouse inalambrico 2.4 GHz",
                new BigDecimal("350.00"), 25, "Accesorios", LocalDate.of(2024, 5, 12), null));

        productoRepository.save(new Producto("Teclado Logitech K120", "Teclado alambrico USB",
                new BigDecimal("450.00"), 18, "Accesorios", LocalDate.of(2024, 5, 13), null));

        productoRepository.save(new Producto("Monitor Samsung 24\" FHD", "Monitor LED Full HD 24 pulgadas",
                new BigDecimal("2850.00"), 7, "Monitores", LocalDate.of(2024, 5, 13), null));

        productoRepository.save(new Producto("Audifonos HyperX Cloud Stinger", "Audifonos gamer con microfono",
                new BigDecimal("1250.00"), 15, "Accesorios", LocalDate.of(2024, 5, 14), null));
    }
}
