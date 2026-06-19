package com.minimarket.service;

import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.service.impl.InventarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceImplTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    private Inventario inventario;
    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);

        inventario = new Inventario();
        inventario.setId(1L);
        inventario.setCantidad(50);
        inventario.setTipoMovimiento("Entrada");
        inventario.setFechaMovimiento(new Date());
        inventario.setProducto(producto);
    }

    // --- PRUEBAS CRUD BÁSICAS (Para mantener Cobertura) ---

    @Test
    void testFindAll() {
        when(inventarioRepository.findAll()).thenReturn(Arrays.asList(inventario));
        List<Inventario> resultados = inventarioService.findAll();
        assertEquals(1, resultados.size());
        verify(inventarioRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Encontrado() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));
        Inventario resultado = inventarioService.findById(1L);
        assertNotNull(resultado);
        assertEquals(50, resultado.getCantidad());
    }

    @Test
    void testFindById_NoEncontrado() {
        when(inventarioRepository.findById(2L)).thenReturn(Optional.empty());
        Inventario resultado = inventarioService.findById(2L);
        assertNull(resultado);
    }

    @Test
    void testDeleteById() {
        doNothing().when(inventarioRepository).deleteById(1L);
        inventarioService.deleteById(1L);
        verify(inventarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByProductoId() {
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Arrays.asList(inventario));
        List<Inventario> resultados = inventarioService.findByProductoId(1L);
        assertFalse(resultados.isEmpty());
        verify(inventarioRepository, times(1)).findByProductoId(1L);
    }

    // --- PRUEBAS ESPECÍFICAS (Validaciones) ---

    @Test
    void testSave_Exito() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);
        
        Inventario resultado = inventarioService.save(inventario);
        
        assertNotNull(resultado);
        verify(inventarioRepository, times(1)).save(inventario);
    }

    @Test
    void testSave_TipoMovimientoNulo_LanzaExcepcion() {
        inventario.setTipoMovimiento(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventarioService.save(inventario);
        });
        assertEquals("El tipo de movimiento no puede ser nulo o vacío", exception.getMessage());
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    void testSave_CantidadNula_LanzaExcepcion() {
        inventario.setCantidad(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventarioService.save(inventario);
        });
        assertEquals("La cantidad no puede ser nula o menor/igual a cero", exception.getMessage());
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    void testSave_ProductoAsociadoInvalido_LanzaExcepcion() {
        inventario.setProducto(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventarioService.save(inventario);
        });
        assertEquals("El producto asociado es nulo o inválido", exception.getMessage());
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    void testSave_ProductoNoExisteEnBD_LanzaExcepcion() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventarioService.save(inventario);
        });
        assertEquals("El producto asociado no existe en la base de datos", exception.getMessage());
        verify(inventarioRepository, never()).save(any());
    }
}