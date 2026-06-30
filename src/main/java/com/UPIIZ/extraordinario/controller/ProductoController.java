package com.UPIIZ.extraordinario.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.UPIIZ.extraordinario.model.Producto;
import com.UPIIZ.extraordinario.service.ProductoService;

import jakarta.validation.Valid;

/**
 * Controlador para la administracion de productos.
 *
 * Rutas:
 *   GET  /productos           → listado de todos los productos
 *   GET  /productos/nuevo     → formulario de alta (solo ADMINISTRADOR)
 *   POST /productos/guardar   → guardar nuevo producto (solo ADMINISTRADOR)
 *   GET  /productos/editar/{id}   → formulario de edicion (solo ADMINISTRADOR)
 *   POST /productos/actualizar    → guardar cambios (solo ADMINISTRADOR)
 *   GET  /productos/eliminar/{id} → eliminar con confirmacion previa (solo ADMINISTRADOR)
 *   GET  /productos/detalle/{id}  → responde JSON via AJAX (ambos roles)
 *
 * El control de acceso por rol lo realiza SessionInterceptor antes de llegar aqui.
 */
@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // ------------------------------------------------------------------
    // LISTADO
    // ------------------------------------------------------------------

    @GetMapping
    public String listado(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "productos/listado";
    }

    // ------------------------------------------------------------------
    // ALTA
    // ------------------------------------------------------------------

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("modoEdicion", false);
        return "productos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid Producto producto,
            BindingResult result,
            Model model,
            RedirectAttributes redirect) {

        if (result.hasErrors()) {
            model.addAttribute("modoEdicion", false);
            return "productos/formulario";
        }
        productoService.guardar(producto);
        redirect.addFlashAttribute("mensaje", "Producto registrado correctamente.");
        return "redirect:/productos";
    }

    // ------------------------------------------------------------------
    // EDICION
    // ------------------------------------------------------------------

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Integer id, Model model, RedirectAttributes redirect) {
        Optional<Producto> opt = productoService.buscarPorId(id);
        if (opt.isEmpty()) {
            redirect.addFlashAttribute("error", "Producto no encontrado.");
            return "redirect:/productos";
        }
        model.addAttribute("producto", opt.get());
        model.addAttribute("modoEdicion", true);
        return "productos/formulario";
    }

    @PostMapping("/actualizar")
    public String actualizar(
            @Valid Producto producto,
            BindingResult result,
            Model model,
            RedirectAttributes redirect) {

        if (result.hasErrors()) {
            model.addAttribute("modoEdicion", true);
            return "productos/formulario";
        }
        productoService.guardar(producto);
        redirect.addFlashAttribute("mensaje", "Producto actualizado correctamente.");
        return "redirect:/productos";
    }

    // ------------------------------------------------------------------
    // ELIMINACION
    // ------------------------------------------------------------------

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirect) {
        if (productoService.buscarPorId(id).isEmpty()) {
            redirect.addFlashAttribute("error", "Producto no encontrado.");
        } else {
            productoService.eliminar(id);
            redirect.addFlashAttribute("mensaje", "Producto eliminado correctamente.");
        }
        return "redirect:/productos";
    }

    // ------------------------------------------------------------------
    // CONSULTA VIA AJAX → responde JSON
    // ------------------------------------------------------------------

    /**
     * Retorna los datos de un producto en formato JSON.
     * El listado llama a este endpoint con AJAX y muestra
     * el resultado en la ventana modal de detalle.
     */
    @GetMapping(value = "/detalle/{id}", produces = "application/json")
    @ResponseBody
    public Object detalleJson(@PathVariable Integer id) {
        Optional<Producto> opt = productoService.buscarPorId(id);
        if (opt.isEmpty()) {
            return java.util.Map.of("error", "Producto no encontrado");
        }
        Producto p = opt.get();
        return java.util.Map.of(
                "id",            p.getId(),
                "nombre",        p.getNombre(),
                "descripcion",   p.getDescripcion(),
                "precio",        p.getPrecio(),
                "existencia",    p.getExistencia(),
                "categoria",     p.getCategoria(),
                "fechaRegistro", p.getFechaRegistro().toString()
        );
    }
}
