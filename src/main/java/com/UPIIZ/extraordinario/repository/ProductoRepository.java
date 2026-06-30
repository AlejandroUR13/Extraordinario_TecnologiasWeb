package com.UPIIZ.extraordinario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.UPIIZ.extraordinario.model.Producto;

/**
 * Repositorio de acceso a datos para Producto.
 * Spring Data JPA genera la implementacion automaticamente.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
