package com.minimarket.service.impl;

import com.minimarket.entity.Inventario;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioServiceImpl implements InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private ProductoRepository productoRepository; // Necesario para validar el producto

    @Override
    public List<Inventario> findAll() { return inventarioRepository.findAll(); }

    @Override
    public Inventario findById(Long id) { return inventarioRepository.findById(id).orElse(null); }

    @Override
    public void deleteById(Long id) { inventarioRepository.deleteById(id); }

    @Override
    public List<Inventario> findByProductoId(Long productoId) { return inventarioRepository.findByProductoId(productoId); }

    // --- AQUÍ ESTÁN LAS VALIDACIONES ---
    @Override
    public Inventario save(Inventario inventario) {
        // 1. Prueba de Información de Movimiento
        if (inventario.getTipoMovimiento() == null || inventario.getTipoMovimiento().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de movimiento no puede ser nulo o vacío");
        }
        if (inventario.getCantidad() == null || inventario.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad no puede ser nula o menor/igual a cero");
        }

        // 2. Prueba de Relación Producto-Inventario
        if (inventario.getProducto() == null || inventario.getProducto().getId() == null) {
            throw new IllegalArgumentException("El producto asociado es nulo o inválido");
        }
        
        productoRepository.findById(inventario.getProducto().getId())
                .orElseThrow(() -> new IllegalArgumentException("El producto asociado no existe en la base de datos"));

        return inventarioRepository.save(inventario);
    }
}