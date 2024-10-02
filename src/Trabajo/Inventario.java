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
            if (envase.getNombre().equalsIgnoreCase(nombre)) {
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

    // Nuevos métodos de búsqueda parcial
    public List<Ingrediente> buscarIngredientes(String nombre) {
        List<Ingrediente> resultados = new ArrayList<>();
        for (Ingrediente ingrediente : ingredientes) {
            if (ingrediente.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(ingrediente);
                if (resultados.size() == 3) {
                    break;
                }
            }
        }
        return resultados;
    }

    public List<Envase> buscarEnvases(String nombre) {
        List<Envase> resultados = new ArrayList<>();
        for (Envase envase : envases) {
            if (envase.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(envase);
                if (resultados.size() == 3) {
                    break;
                }
            }
        }
        return resultados;
    }

    public List<Producto> buscarProductos(String nombre) {
        List<Producto> resultados = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(producto);
                if (resultados.size() == 3) {
                    break;
                }
            }
        }
        return resultados;
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
        BigDecimal valorTotalIngredientes = BigDecimal.ZERO;
        BigDecimal valorTotalEnvases = BigDecimal.ZERO;

        for (Producto producto : productos) {
            valorTotalProductos = valorTotalProductos.add(producto.calcularCostoTotal());
        }
        for (Ingrediente ingrediente : ingredientes) {
            valorTotalIngredientes = valorTotalIngredientes.add(ingrediente.calcularCostoTotal());
        }
        for (Envase envase : envases) {
            valorTotalEnvases = valorTotalEnvases.add(envase.calcularCostoTotal());
        }

        BigDecimal valorTotal = valorTotalProductos.add(valorTotalIngredientes).add(valorTotalEnvases);

        System.out.println("Valor total de productos en almacén y en producción: S/." + valorTotalProductos);
        System.out.println("Valor total de ingredientes en almacén: S/." + valorTotalIngredientes);
        System.out.println("Valor total de envases en almacén: S/." + valorTotalEnvases);
        System.out.println("Valor total del inventario: S/." + valorTotal);
    }
}
