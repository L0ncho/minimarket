package com.minimarket.service.impl;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Producto;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository; // Agregado para validar stock

    @Override
    public List<Carrito> findAll() { return carritoRepository.findAll(); }

    @Override
    public Carrito findById(Long id) { return carritoRepository.findById(id).orElse(null); }

    @Override
    public void deleteById(Long id) { carritoRepository.deleteById(id); }

    @Override
    public List<Carrito> findByUsuarioId(Long usuarioId) { return carritoRepository.findByUsuarioId(usuarioId); }

    @Override
    public Carrito save(Carrito carrito) { return agregarProducto(carrito); }

   
    public Carrito agregarProducto(Carrito carrito) {
        // 1. Validación de relación Producto-Usuario
        if (carrito.getUsuario() == null || carrito.getUsuario().getId() == null) {
            throw new IllegalArgumentException("Usuario asociado al carrito es inválido o nulo");
        }

        if (carrito.getProducto() == null || carrito.getProducto().getId() == null) {
            throw new IllegalArgumentException("Producto inválido");
        }

        // 2. Prueba de disponibilidad de stock
        Producto productoBD = productoRepository.findById(carrito.getProducto().getId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado en BD"));

        if (productoBD.getStock() < carrito.getCantidad()) {
            throw new IllegalStateException("Stock insuficiente para el producto seleccionado");
        }

        carrito.setProducto(productoBD);
        return carritoRepository.save(carrito);
    }
}