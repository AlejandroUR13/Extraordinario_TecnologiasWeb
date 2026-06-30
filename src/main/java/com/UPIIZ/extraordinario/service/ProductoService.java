package com.UPIIZ.extraordinario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.UPIIZ.extraordinario.model.Producto;
import com.UPIIZ.extraordinario.repository.ProductoRepository;

/**
 * Servicio de negocio para la gestion de productos.
 * Separa la logica de negocio del acceso a datos (Repository)
 * y de la capa de presentacion (Controller).
 */
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(Integer id) {
        return productoRepository.findById(id);
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminar(Integer id) {
        productoRepository.deleteById(id);
    }
}
