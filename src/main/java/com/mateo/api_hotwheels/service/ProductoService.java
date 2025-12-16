package com.mateo.api_hotwheels.service;

import com.mateo.api_hotwheels.model.Producto;
import com.mateo.api_hotwheels.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Producto crear(Producto producto) {
        // Si no viene stock desde Android, evitar null pointer
        if (producto.getStock() == null) {
            producto.setStock(0);
        }
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto producto) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no existe");
        }

        producto.setId(id);
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no existe");
        }
        productoRepository.deleteById(id);
    }

    public List<Producto> listarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    // 🔥 NUEVO: DESCONTAR STOCK
    public Producto descontarStock(Long id, int cantidad) {
        Producto producto = obtenerPorId(id);

        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        producto.setStock(producto.getStock() - cantidad);
        return productoRepository.save(producto);
    }

    // 🔥 NUEVO: SUMAR STOCK (útil para admin)
    public Producto sumarStock(Long id, int cantidad) {
        Producto producto = obtenerPorId(id);

        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }

        producto.setStock(producto.getStock() + cantidad);
        return productoRepository.save(producto);
    }
}
