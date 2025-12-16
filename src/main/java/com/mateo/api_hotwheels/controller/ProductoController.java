package com.mateo.api_hotwheels.controller;

import com.mateo.api_hotwheels.model.Producto;
import com.mateo.api_hotwheels.service.ProductoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Producto obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }

    @PostMapping
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoService.crear(producto);
    }

    @PutMapping("/{id}")
    public Producto actualizarProducto(@PathVariable Long id,
                                       @RequestBody Producto producto) {
        return productoService.actualizar(id, producto);
    }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
    }

    @GetMapping("/categoria/{categoria}")
    public List<Producto> listarPorCategoria(@PathVariable String categoria) {
        return productoService.listarPorCategoria(categoria);
    }

    @PatchMapping("/{id}/descontar/{cantidad}")
    public Producto descontarStock(@PathVariable Long id, @PathVariable int cantidad) {
        return productoService.descontarStock(id, cantidad);
    }

    @PatchMapping("/{id}/sumar/{cantidad}")
    public Producto sumarStock(@PathVariable Long id, @PathVariable int cantidad) {
        return productoService.sumarStock(id, cantidad);
    }
}
