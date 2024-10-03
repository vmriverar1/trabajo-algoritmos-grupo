package Trabajo;

import java.util.ArrayList; // Utilizado para almacenar listas de ingredientes, envases y productos.
import java.util.List; // Para devolver listas en los métodos de búsqueda.
import java.math.BigDecimal; // Para realizar cálculos precisos con cantidades y costos.

public class Inventario {
    // Listas que almacenan los ingredientes, envases y productos.
    private final ArrayList<Ingrediente> ingredientes; // Lista de ingredientes.
    private final ArrayList<Envase> envases; // Lista de envases.
    private final ArrayList<Producto> productos; // Lista de productos.

    /**
     * Constructor que inicializa listas vacías para los ingredientes, envases y productos.
     */
    public Inventario() {
        this.ingredientes = new ArrayList<>(); // Inicializa la lista de ingredientes.
        this.envases = new ArrayList<>(); // Inicializa la lista de envases.
        this.productos = new ArrayList<>(); // Inicializa la lista de productos.
    }

    /**
     * Agrega un ingrediente al inventario.
     *
     * @param ingrediente El ingrediente que se va a agregar.
     */
    public void agregarIngrediente(Ingrediente ingrediente) {
        ingredientes.add(ingrediente); // Añade el ingrediente a la lista.
    }

    /**
     * Agrega un envase al inventario.
     *
     * @param envase El envase que se va a agregar.
     */
    public void agregarEnvase(Envase envase) {
        envases.add(envase); // Añade el envase a la lista.
    }

    /**
     * Agrega un producto al inventario.
     *
     * @param producto El producto que se va a agregar.
     */
    public void agregarProducto(Producto producto) {
        productos.add(producto); // Añade el producto a la lista.
    }

    /**
     * Busca un ingrediente en el inventario por su nombre.
     *
     * @param nombre El nombre del ingrediente a buscar.
     * @return El ingrediente encontrado o null si no se encuentra.
     */
    public Ingrediente buscarIngrediente(String nombre) {
        // Recorre la lista de ingredientes buscando uno cuyo nombre coincida (sin diferenciar mayúsculas).
        for (Ingrediente ingrediente : ingredientes) {
            if (ingrediente.getNombre().equalsIgnoreCase(nombre)) {
                return ingrediente;
            }
        }
        return null; // Retorna null si no se encuentra el ingrediente.
    }

    /**
     * Busca un envase en el inventario por su nombre.
     *
     * @param nombre El nombre del envase a buscar.
     * @return El envase encontrado o null si no se encuentra.
     */
    public Envase buscarEnvase(String nombre) {
        // Recorre la lista de envases buscando uno cuyo nombre coincida (sin diferenciar mayúsculas).
        for (Envase envase : envases) {
            if (envase.getNombre().equalsIgnoreCase(nombre)) {
                return envase;
            }
        }
        return null; // Retorna null si no se encuentra el envase.
    }

    /**
     * Busca un producto en el inventario por su nombre.
     *
     * @param nombre El nombre del producto a buscar.
     * @return El producto encontrado o null si no se encuentra.
     */
    public Producto buscarProducto(String nombre) {
        // Recorre la lista de productos buscando uno cuyo nombre coincida (sin diferenciar mayúsculas).
        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }
        return null; // Retorna null si no se encuentra el producto.
    }

    /**
     * Busca hasta tres ingredientes cuyo nombre contenga la cadena especificada.
     *
     * @param nombre El nombre (parcial) del ingrediente a buscar.
     * @return Una lista con los ingredientes encontrados (máximo tres).
     */
    public List<Ingrediente> buscarIngredientes(String nombre) {
        List<Ingrediente> resultados = new ArrayList<>();
        // Recorre la lista de ingredientes buscando coincidencias parciales (sin diferenciar mayúsculas).
        for (Ingrediente ingrediente : ingredientes) {
            if (ingrediente.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(ingrediente); // Añade el ingrediente a los resultados si coincide.
                if (resultados.size() == 3) {
                    break; // Si encuentra tres coincidencias, se detiene la búsqueda.
                }
            }
        }
        return resultados; // Retorna los ingredientes encontrados.
    }

    /**
     * Busca hasta tres envases cuyo nombre contenga la cadena especificada.
     *
     * @param nombre El nombre (parcial) del envase a buscar.
     * @return Una lista con los envases encontrados (máximo tres).
     */
    public List<Envase> buscarEnvases(String nombre) {
        List<Envase> resultados = new ArrayList<>();
        // Recorre la lista de envases buscando coincidencias parciales (sin diferenciar mayúsculas).
        for (Envase envase : envases) {
            if (envase.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(envase); // Añade el envase a los resultados si coincide.
                if (resultados.size() == 3) {
                    break; // Si encuentra tres coincidencias, se detiene la búsqueda.
                }
            }
        }
        return resultados; // Retorna los envases encontrados.
    }

    /**
     * Busca hasta tres productos cuyo nombre contenga la cadena especificada.
     *
     * @param nombre El nombre (parcial) del producto a buscar.
     * @return Una lista con los productos encontrados (máximo tres).
     */
    public List<Producto> buscarProductos(String nombre) {
        List<Producto> resultados = new ArrayList<>();
        // Recorre la lista de productos buscando coincidencias parciales (sin diferenciar mayúsculas).
        for (Producto producto : productos) {
            if (producto.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(producto); // Añade el producto a los resultados si coincide.
                if (resultados.size() == 3) {
                    break; // Si encuentra tres coincidencias, se detiene la búsqueda.
                }
            }
        }
        return resultados; // Retorna los productos encontrados.
    }

    /**
     * Devuelve la lista de ingredientes en el inventario.
     *
     * @return La lista de ingredientes.
     */
    public List<Ingrediente> getIngredientes() {
        return ingredientes; // Retorna la lista completa de ingredientes.
    }

    /**
     * Devuelve la lista de envases en el inventario.
     *
     * @return La lista de envases.
     */
    public List<Envase> getEnvases() {
        return envases; // Retorna la lista completa de envases.
    }

    /**
     * Devuelve la lista de productos en el inventario.
     *
     * @return La lista de productos.
     */
    public List<Producto> getProductos() {
        return productos; // Retorna la lista completa de productos.
    }

    /**
     * Genera un reporte de todos los productos en el inventario, mostrando su información.
     */
    public void generarReporteProductos() {
        System.out.println("=== Reporte de Productos ===");
        // Recorre la lista de productos y muestra la información de cada uno.
        for (Producto producto : productos) {
            System.out.println(producto); // Imprime los detalles del producto.
        }
    }

    /**
     * Genera un reporte de todos los ingredientes en el inventario, mostrando su información.
     */
    public void generarReporteIngredientes() {
        System.out.println("=== Reporte de Ingredientes ===");
        // Recorre la lista de ingredientes y muestra la información de cada uno.
        for (Ingrediente ingrediente : ingredientes) {
            System.out.println(ingrediente); // Imprime los detalles del ingrediente.
        }
    }

    /**
     * Genera un reporte de todos los envases en el inventario, mostrando su información.
     */
    public void generarReporteEnvases() {
        System.out.println("=== Reporte de Envases ===");
        // Recorre la lista de envases y muestra la información de cada uno.
        for (Envase envase : envases) {
            System.out.println(envase); // Imprime los detalles del envase.
        }
    }

    /**
     * Genera un reporte general del inventario, mostrando el valor total de productos, ingredientes y envases.
     */
    public void generarReporteGeneral() {
        System.out.println("=== Reporte General ===");
        BigDecimal valorTotalProductos = BigDecimal.ZERO; // Inicializa el valor total de productos.
        BigDecimal valorTotalIngredientes = BigDecimal.ZERO; // Inicializa el valor total de ingredientes.
        BigDecimal valorTotalEnvases = BigDecimal.ZERO; // Inicializa el valor total de envases.

        // Suma el valor total de todos los productos.
        for (Producto producto : productos) {
            valorTotalProductos = valorTotalProductos.add(producto.calcularCostoTotal());
        }
        // Suma el valor total de todos los ingredientes.
        for (Ingrediente ingrediente : ingredientes) {
            valorTotalIngredientes = valorTotalIngredientes.add(ingrediente.calcularCostoTotal());
        }
        // Suma el valor total de todos los envases.
        for (Envase envase : envases) {
            valorTotalEnvases = valorTotalEnvases.add(envase.calcularCostoTotal());
        }

        // Calcula el valor total del inventario sumando productos, ingredientes y envases.
        BigDecimal valorTotal = valorTotalProductos.add(valorTotalIngredientes).add(valorTotalEnvases);

        // Muestra los valores totales en el reporte.
        System.out.println("Valor total de productos en almacén y en producción: S/." + valorTotalProductos);
        System.out.println("Valor total de ingredientes en almacén: S/." + valorTotalIngredientes);
        System.out.println("Valor total de envases en almacén: S/." + valorTotalEnvases);
        System.out.println("Valor total del inventario: S/." + valorTotal);
    }
}
