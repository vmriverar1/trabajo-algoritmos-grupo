package Trabajo;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class Inventario {
    private final ArrayList<Ingrediente> ingredientes;
    private final ArrayList<Envase> envases;
    private final ArrayList<Producto> productos;

    public Inventario() {
        this.ingredientes = new ArrayList<>();
        this.envases = new ArrayList<>();
        this.productos = new ArrayList<>();
    }

    public void agregarIngrediente(Ingrediente ingrediente) {
        ingredientes.add(ingrediente);
    }

    public void agregarEnvase(Envase envase) {
        envases.add(envase);
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public Ingrediente buscarIngrediente(String nombre) {
        for (Ingrediente ingrediente : ingredientes) {
            if (ingrediente.getNombre().equalsIgnoreCase(nombre)) {
                return ingrediente;
            }
        }
        return null;
    }

    public Envase buscarEnvase(String nombre) {
        for (Envase envase : envases) {
            if (envase.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                return envase;
            }
        }
        return null;
    }

    public Producto buscarProducto(String nombre) {
        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }
        return null;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public List<Envase> getEnvases() {
        return envases;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void generarReporteProductos() {
        System.out.println("=== Reporte de Productos ===");
        for (Producto producto : productos) {
            System.out.println(producto);
        }
    }

    public void generarReporteIngredientes() {
        System.out.println("=== Reporte de Ingredientes ===");
        for (Ingrediente ingrediente : ingredientes) {
            System.out.println(ingrediente);
        }
    }

    public void generarReporteEnvases() {
        System.out.println("=== Reporte de Envases ===");
        for (Envase envase : envases) {
            System.out.println(envase);
        }
    }

    public void generarReporteGeneral() {
        System.out.println("=== Reporte General ===");
        BigDecimal valorTotalProductos = BigDecimal.ZERO;
        for (Producto producto : productos) {
            valorTotalProductos = valorTotalProductos.add(producto.calcularCostoTotal());
        }
        System.out.println("Valor total de productos en almacén y en producción: S/." + valorTotalProductos);
    }
}
