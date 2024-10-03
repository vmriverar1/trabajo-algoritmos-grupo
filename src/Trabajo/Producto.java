package Trabajo;

import java.util.Iterator; // Para recorrer los elementos de la lista de lotes.
import java.math.BigDecimal; // Para manejar cantidades numéricas con alta precisión decimal.
import java.math.RoundingMode; // Para controlar cómo se redondean los números decimales.

public class Producto {
    // Atributos de la clase Producto
    private final String nombre; // El nombre del producto.
    private final String categoria; // La categoría a la que pertenece el producto.
    private final Receta receta; // La receta del producto que incluye los ingredientes y envases necesarios.
    private final CircularLinkedList<Lote> lotes; // Lista circular de lotes del producto.

    /**
     * Constructor de Producto.
     * 
     * @param nombre    El nombre del producto.
     * @param categoria La categoría del producto.
     * @param receta    La receta asociada al producto.
     */
    public Producto(String nombre, String categoria, Receta receta) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.receta = receta;
        this.lotes = new CircularLinkedList<>(); // Inicializa la lista circular de lotes vacía.
    }

    // Método que devuelve el nombre del producto.
    public String getNombre() {
        return nombre;
    }

    // Método que devuelve la receta del producto.
    public Receta getReceta() {
        return receta;
    }

    // Método para agregar un lote de producto a la lista de lotes.
    public void agregarLote(Lote lote) {
        lotes.addLast(lote); // Agrega el lote al final de la lista circular.
    }

    // Método que calcula la cantidad total de productos disponibles sumando todos los lotes.
    public BigDecimal obtenerCantidadTotal() {
        return obtenerCantidadTotalRecursivo(lotes.iterator(), BigDecimal.ZERO); 
    }

    // Método auxiliar recursivo para calcular la cantidad total de productos en todos los lotes.
    private BigDecimal obtenerCantidadTotalRecursivo(Iterator<Lote> iterator, BigDecimal acumulador) {
        if (!iterator.hasNext()) { // Si no hay más lotes, devuelve el acumulador.
            return acumulador;
        }
        Lote lote = iterator.next(); // Toma el siguiente lote.
        acumulador = acumulador.add(lote.getCantidad()); // Suma la cantidad del lote actual al acumulador.
        return obtenerCantidadTotalRecursivo(iterator, acumulador); // Llama al método recursivo para el siguiente lote.
    }

    // Método que calcula el costo total de todos los lotes del producto.
    public BigDecimal calcularCostoTotal() {
        BigDecimal costoTotal = BigDecimal.ZERO;
        // Recorre cada lote de la lista y suma su costo (costo unitario * cantidad).
        for (Lote lote : lotes) {
            costoTotal = costoTotal.add(lote.getCostoUnitario().multiply(lote.getCantidad()));
        }
        return costoTotal.setScale(2, RoundingMode.HALF_UP); // Redondea el resultado a 2 decimales.
    }

    // Método que calcula el costo unitario total de todos los lotes.
    public BigDecimal calcularCostoUnitarioTotal() {
        BigDecimal costoUnitarioTotal = BigDecimal.ZERO;
        // Suma los costos unitarios de cada lote.
        for (Lote lote : lotes) {
            costoUnitarioTotal = costoUnitarioTotal.add(lote.getCostoUnitario());
        }
        return costoUnitarioTotal.setScale(2, RoundingMode.HALF_UP); // Redondea el costo unitario total a 2 decimales.
    }

    // Método que devuelve una cadena de texto con la información del producto.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Se construye una representación del producto con su nombre, categoría, stock, costos y lotes.
        sb.append("Producto: ").append(nombre)
          .append("\nCategoría: ").append(categoria)
          .append("\nStock Total: ").append(obtenerCantidadTotal()) // Incluye la cantidad total de todos los lotes.
          .append("\nCosto Total: S/.").append(calcularCostoTotal()) // Incluye el costo total calculado.
          .append("\nCosto Unitario Total: S/.").append(calcularCostoUnitarioTotal()) // Incluye el costo unitario total.
          .append("\nReceta:\n").append(receta) // Incluye la receta asociada.
          .append("\nLotes:\n");

        // Añade la información de cada lote.
        for (Lote lote : lotes) {
            sb.append(lote).append("\n"); // Llama al método toString() de cada Lote.
        }

        return sb.toString(); // Devuelve la representación del producto.
    }
}
