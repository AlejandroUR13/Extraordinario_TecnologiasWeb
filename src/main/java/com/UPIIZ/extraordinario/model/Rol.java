package com.UPIIZ.extraordinario.model;

/**
 * Roles disponibles en el sistema.
 *
 * ADMINISTRADOR: acceso total (alta, baja, edicion y consulta de productos).
 * CAPTURISTA: solo puede listar y consultar el detalle de productos.
 */
public enum Rol {
    ADMINISTRADOR,
    CAPTURISTA
}
