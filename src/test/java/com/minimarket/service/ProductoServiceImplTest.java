package com.minimarket.service;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.exception.InvalidRequestException;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.service.impl.ProductoServiceImpl;
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
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Arroz");
        producto.setPrecio(1500.0);
        producto.setStock(100);
        producto.setCategoria(categoria);
    }

    @Test
    void testFindAll() {
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto));
        List<Producto> productos = productoService.findAll();
        assertFalse(productos.isEmpty());
        assertEquals(1, productos.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Encontrado() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        Producto resultado = productoService.findById(1L);
        assertNotNull(resultado);
        assertEquals("Arroz", resultado.getNombre());
    }

    @Test
    void testFindById_NoEncontrado() {
        when(productoRepository.findById(2L)).thenReturn(Optional.empty());
        Producto resultado = productoService.findById(2L);
        assertNull(resultado);
    }

    @Test
    void testSave_Exito() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto resultado = productoService.save(producto);

        assertNotNull(resultado);
        verify(categoriaRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testSave_SinCategoria_LanzaExcepcion() {
        producto.setCategoria(null);

        assertThrows(InvalidRequestException.class, () -> {
            productoService.save(producto);
        });

        // Verificamos que la BD nunca se toca si falla
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void testSave_CategoriaSinId_LanzaExcepcion() {
        categoria.setId(null);
        producto.setCategoria(categoria);

        assertThrows(InvalidRequestException.class, () -> {
            productoService.save(producto);
        });

        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void testSave_CategoriaNoExisteEnBD_LanzaExcepcion() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidRequestException.class, () -> {
            productoService.save(producto);
        });

        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void testDeleteById() {
        doNothing().when(productoRepository).deleteById(1L);
        productoService.deleteById(1L);
        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByCategoriaId() {
        when(productoRepository.findByCategoriaId(1L)).thenReturn(Arrays.asList(producto));
        List<Producto> resultados = productoService.findByCategoriaId(1L);
        assertFalse(resultados.isEmpty());
        verify(productoRepository, times(1)).findByCategoriaId(1L);
    }
}