package com.minimarket.service;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.service.impl.CarritoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceImplTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    private Carrito carrito;
    private Producto producto;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);

        producto = new Producto();
        producto.setId(1L);
        producto.setStock(10); // Stock inicial

        carrito = new Carrito();
        carrito.setId(1L);
        carrito.setCantidad(5);
        carrito.setUsuario(usuario);
        carrito.setProducto(producto);
    }

    // --- PRUEBAS CRUD BÁSICAS (Para mantener Cobertura) ---

    @Test
    void testFindAll() {
        when(carritoRepository.findAll()).thenReturn(Arrays.asList(carrito));
        List<Carrito> resultados = carritoService.findAll();
        assertEquals(1, resultados.size());
        verify(carritoRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Encontrado() {
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        Carrito resultado = carritoService.findById(1L);
        assertNotNull(resultado);
        assertEquals(5, resultado.getCantidad());
    }

    @Test
    void testFindById_NoEncontrado() {
        when(carritoRepository.findById(2L)).thenReturn(Optional.empty());
        Carrito resultado = carritoService.findById(2L);
        assertNull(resultado);
    }

    @Test
    void testDeleteById() {
        doNothing().when(carritoRepository).deleteById(1L);
        carritoService.deleteById(1L);
        verify(carritoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByUsuarioId() {
        when(carritoRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(carrito));
        List<Carrito> resultados = carritoService.findByUsuarioId(1L);
        assertFalse(resultados.isEmpty());
        verify(carritoRepository, times(1)).findByUsuarioId(1L);
    }

    // --- PRUEBAS ESPECÍFICAS (Validaciones) ---

    @Test
    void testAgregarProducto_StockSuficiente_GuardaExitosamente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(carritoRepository.save(carrito)).thenReturn(carrito);

        // Prueba el guardado con las nuevas validaciones
        Carrito resultado = carritoService.agregarProducto(carrito);

        assertNotNull(resultado);
        verify(carritoRepository, times(1)).save(carrito);
    }

    @Test
    void testAgregarProducto_StockInsuficiente_LanzaExcepcion() {
        carrito.setCantidad(15); // Pide 15, hay 10
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            carritoService.agregarProducto(carrito);
        });

        assertEquals("Stock insuficiente para el producto seleccionado", exception.getMessage());
        verify(carritoRepository, never()).save(any());
    }

    @Test
    void testAgregarProducto_UsuarioInvalido_LanzaExcepcion() {
        carrito.setUsuario(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carritoService.agregarProducto(carrito);
        });

        assertEquals("Usuario asociado al carrito es inválido o nulo", exception.getMessage());
        verify(productoRepository, never()).findById(anyLong());
        verify(carritoRepository, never()).save(any());
    }
}