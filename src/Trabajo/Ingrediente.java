package Trabajo;

import java.util.Iterator; // Se usa para iterar sobre los lotes.
import java.math.BigDecimal; // Para manejar valores numéricos precisos.
import java.math.RoundingMode; // Se usa para manejar el redondeo de valores decimales.

public class Ingrediente {
    // Atributos principales de la clase.
    private final String nombre; // Nombre del ingrediente.
    private final String categoria; // Categoría del ingrediente (ej: Vegetal, Cereal, etc.).
    private final CircularLinkedList<Lote> lotes; // Lista circular que contiene los lotes de este ingrediente.

    /**
     * Constructor que inicializa un nuevo ingrediente con su nombre y categoría.
     * También inicializa la lista de lotes como vacía.
     *
     * @param nombre    Nombre del ingrediente.
     * @param categoria Categoría a la que pertenece el ingrediente.
     */
    public Ingrediente(String nombre, String categoria) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.lotes = new CircularLinkedList<>(); // Inicializa la lista circular de lotes.
    }

    /**
     * Método para obtener el nombre del ingrediente.
     *
     * @return El nombre del ingrediente.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método para agregar un nuevo lote al ingrediente.
     *
     * @param lote El objeto `Lote` que se va a agregar al ingrediente.
     */
    public void agregarLote(Lote lote) {
        lotes.addLast(lote); // Añade el lote al final de la lista circular de lotes.
    }

    /**
     * Método para obtener la cantidad total de ingredientes disponibles sumando las cantidades de cada lote.
     *
     * @return La cantidad total de ingredientes.
     */
    public BigDecimal obtenerCantidadTotal() {
        // Usa un método recursivo que suma las cantidades de todos los lotes.
        return obtenerCantidadTotalRecursivo(lotes.iterator(), BigDecimal.ZERO);
    }

    /**
     * Método recursivo para sumar las cantidades de los lotes iterando sobre ellos.
     *
     * @param iterator   Iterador que recorre los lotes.
     * @param acumulador Cantidad acumulada hasta el momento.
     * @return La cantidad total de ingredientes.
     */
    private BigDecimal obtenerCantidadTotalRecursivo(Iterator<Lote> iterator, BigDecimal acumulador) {
        if (!iterator.hasNext()) {
            return acumulador; // Si no hay más lotes, retorna el acumulador.
        }
        Lote lote = iterator.next(); // Obtiene el siguiente lote.
        acumulador = acumulador.add(lote.getCantidad()); // Suma la cantidad de este lote al acumulador.
        return obtenerCantidadTotalRecursivo(iterator, acumulador); // Llamada recursiva.
    }

    /**
     * Método para calcular el costo total del ingrediente sumando el costo de todos los lotes.
     * El costo se calcula multiplicando el costo unitario por la cantidad de cada lote.
     *
     * @return El costo total del ingrediente.
     */
    public BigDecimal calcularCostoTotal() {
        BigDecimal costoTotal = BigDecimal.ZERO; // Inicializa el costo total en cero.
        for (Lote lote : lotes) {
            // Para cada lote, multiplica el costo unitario por la cantidad y lo suma al costo total.
            costoTotal = costoTotal.add(lote.getCostoUnitario().multiply(lote.getCantidad()));
        }
        // Redondea el resultado a dos decimales.
        return costoTotal.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Método para calcular el costo unitario total sumando los costos unitarios de todos los lotes.
     *
     * @return El costo unitario total.
     */
    public BigDecimal calcularCostoUnitarioTotal() {
        BigDecimal costoUnitarioTotal = BigDecimal.ZERO; // Inicializa el costo unitario total en cero.
        for (Lote lote : lotes) {
            // Suma el costo unitario de cada lote.
            costoUnitarioTotal = costoUnitarioTotal.add(lote.getCostoUnitario());
        }
        // Redondea el resultado a dos decimales.
        return costoUnitarioTotal.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Método para consumir una cantidad específica de ingredientes de los lotes disponibles.
     * Si la cantidad requerida es mayor al stock disponible, lanza una excepción.
     *
     * @param cantidad Cantidad de ingredientes que se desea consumir.
     * @throws StockBajoException Si no hay suficiente stock disponible.
     */
    public void consumirCantidad(BigDecimal cantidad) throws StockBajoException {
        BigDecimal cantidadRestante = cantidad; // Almacena la cantidad que aún se necesita consumir.
        Iterator<Lote> iterator = lotes.iterator(); // Crea un iterador para recorrer los lotes.
        while (iterator.hasNext() && cantidadRestante.compareTo(BigDecimal.ZERO) > 0) {
            Lote lote = iterator.next(); // Obtiene el siguiente lote.
            BigDecimal cantidadLote = lote.getCantidad(); // Obtiene la cantidad de este lote.
            if (cantidadLote.compareTo(cantidadRestante) <= 0) {
                // Si el lote tiene menos o igual cantidad que lo necesario, consume todo el lote.
                cantidadRestante = cantidadRestante.subtract(cantidadLote);
                iterator.remove(); // Elimina el lote ya que se consumió por completo.
            } else {
                // Si el lote tiene más cantidad de lo requerido, solo consume lo necesario.
                lote.reducirCantidad(cantidadRestante);
                cantidadRestante = BigDecimal.ZERO; // No se requiere más cantidad.
            }
        }
        // Si aún queda cantidad por consumir, significa que no había suficiente stock.
        if (cantidadRestante.compareTo(BigDecimal.ZERO) > 0) {
            throw new StockBajoException("Stock insuficiente para el ingrediente: " + nombre);
        }
    }

    /**
     * Método que devuelve una representación en cadena del objeto Ingrediente.
     * Incluye el nombre, la categoría, el stock total, el costo total y los detalles de los lotes.
     *
     * @return Una cadena con la información del ingrediente.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(); // Crea un objeto StringBuilder para construir la cadena.
        sb.append("Ingrediente: ").append(nombre) // Añade el nombre del ingrediente.
                .append("\nCategoría: ").append(categoria) // Añade la categoría del ingrediente.
                .append("\nStock Total: ").append(obtenerCantidadTotal()) // Muestra el stock total.
                .append("\nCosto Total: S/.").append(calcularCostoTotal().toPlainString()) // Muestra el costo total.
                .append("\nCosto Unitario S/.").append(calcularCostoUnitarioTotal().toPlainString()) // Muestra el costo unitario total.
                .append("\nLotes:\n");

        // Itera sobre todos los lotes y los añade al string.
        for (Lote lote : lotes) {
            sb.append(lote).append("\n"); // Añade la información del lote.
        }

        return sb.toString(); // Devuelve la cadena completa con todos los detalles.
    }
}
