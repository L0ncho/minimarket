package com.minimarket.controller;

import com.minimarket.dto.StockDisponibleResponse;
import com.minimarket.entity.Producto;
import com.minimarket.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Productos", description = "Catálogo de productos")
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Operation(
            summary = "Listar productos",
            description = "Público. Incluye stockDisponible calculado desde inventario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos")
    })
    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.findAll();
    }

    @Operation(
            summary = "Consultar stock disponible de un producto",
            description = "Público. Devuelve el stock calculado desde movimientos de inventario (Entrada - Salida).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock disponible",
                    content = @Content(schema = @Schema(implementation = StockDisponibleResponse.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}/stock")
    public ResponseEntity<StockDisponibleResponse> consultarStockDisponible(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        int stockDisponible = productoService.consultarStock(id);
        return ResponseEntity.ok(new StockDisponibleResponse(id, stockDisponible));
    }

    @Operation(
            summary = "Obtener producto por ID",
            description = "Público. Incluye stockDisponible calculado desde inventario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        return (producto != null) ? ResponseEntity.ok(producto) : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Crear producto",
            description = "Roles: GERENTE, ADMIN. El stock se gestiona vía inventario (POST /api/inventario).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto creado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public Producto guardarProducto(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(
                            name = "Nuevo producto",
                            value = """
                                    {"nombre":"Leche entera 1L","precio":990.0,\
                                    "categoria":{"id":1}}\
                                    """)))
            @RequestBody Producto producto) {
        return productoService.save(producto);
    }

    @Operation(
            summary = "Actualizar producto",
            description = "Roles: GERENTE, ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = Producto.class)))
            @RequestBody Producto producto) {
        Producto productoExistente = productoService.findById(id);
        if (productoExistente != null) {
            producto.setId(id);
            return ResponseEntity.ok(productoService.save(producto));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Eliminar producto",
            description = "Roles: GERENTE, ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        if (producto != null) {
            productoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
