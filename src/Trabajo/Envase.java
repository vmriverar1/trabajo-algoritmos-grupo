package Trabajo;

import java.util.Iterator; // Interfaz utilizada para iterar sobre los lotes.
import java.math.BigDecimal; // Clase utilizada para manejar números de gran precisión.
import java.math.RoundingMode; // Clase utilizada para manejar el redondeo de números decimales.

public class Envase {
    // Atributos principales de la clase.
    private final String nombre; // Nombre del envase.
    private final String tipo; // Tipo del envase (ej: vidrio, plástico, etc.).
    private final CircularLinkedList<Lote> lotes; // Lista circular de los lotes de este envase.

    /**
     * Constructor que inicializa un envase con un nombre y un tipo.
     * También crea una lista circular de lotes vacía.
     *
     * @param nombre Nombre del envase.
     * @param tipo   Tipo del envase.
     */
    public Envase(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.lotes = new CircularLinkedList<>(); // Inicializa la lista de lotes como vacía.
    }

    /**
     * Método para obtener el nombre del envase.
     *
     * @return El nombre del envase.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método para agregar un nuevo lote a la lista de lotes del envase.
     *
     * @param lote El objeto `Lote` que se va a agregar al envase.
     */
    public void agregarLote(Lote lote) {
        lotes.addLast(lote); // Agrega el lote al final de la lista circular.
    }

    /**
     * Método para obtener la cantidad total de envases disponibles sumando las cantidades de cada lote.
     *
     * @return La cantidad total de envases en todos los lotes.
     */
    public BigDecimal obtenerCantidadTotal() {
        // Llama al método recursivo que suma las cantidades de todos los lotes.
        return obtenerCantidadTotalRecursivo(lotes.iterator(), BigDecimal.ZERO);
    }

    /**
     * Método recursivo para calcular la cantidad total de envases iterando sobre los lotes.
     *
     * @param iterator   Iterador que recorre los lotes.
     * @param acumulador Cantidad acumulada de envases hasta el momento.
     * @return La cantidad total de envases.
     */
    private BigDecimal obtenerCantidadTotalRecursivo(Iterator<Lote> iterator, BigDecimal acumulador) {
        if (!iterator.hasNext()) {
            return acumulador; // Si no hay más lotes, retorna el acumulador.
        }
        Lote lote = iterator.next(); // Obtiene el siguiente lote.
        acumulador = acumulador.add(lote.getCantidad()); // Suma la cantidad del lote al acumulador.
        return obtenerCantidadTotalRecursivo(iterator, acumulador); // Llama recursivamente hasta recorrer todos los lotes.
    }

    /**
     * Método para calcular el costo total de todos los envases en los lotes.
     * Se multiplica el costo unitario de cada lote por la cantidad de envases en ese lote.
     *
     * @return El costo total de los envases.
     */
    public BigDecimal calcularCostoTotal() {
        BigDecimal costoTotal = BigDecimal.ZERO; // Inicializa el costo total.
        for (Lote lote : lotes) {
            // Para cada lote, multiplica el costo unitario por la cantidad de envases en ese lote y lo suma al total.
            costoTotal = costoTotal.add(lote.getCostoUnitario().multiply(lote.getCantidad()));
        }
        // Redondea el resultado a dos decimales usando el modo de redondeo HALF_UP.
        return costoTotal.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Método para calcular el costo unitario total de todos los lotes.
     * Este método suma los costos unitarios de cada lote.
     *
     * @return El costo unitario total.
     */
    public BigDecimal calcularCostoUnitarioTotal() {
        BigDecimal costoUnitarioTotal = BigDecimal.ZERO; // Inicializa el costo unitario total.
        for (Lote lote : lotes) {
            // Suma el costo unitario de cada lote.
            costoUnitarioTotal = costoUnitarioTotal.add(lote.getCostoUnitario());
        }
        // Redondea el resultado a dos decimales usando el modo de redondeo HALF_UP.
        return costoUnitarioTotal.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Método para consumir una cantidad específica de envases de los lotes disponibles.
     * Si la cantidad requerida es mayor al stock disponible, se lanza una excepción.
     *
     * @param cantidad Cantidad de envases que se desea consumir.
     * @throws StockBajoException Si no hay suficiente stock disponible para cubrir la cantidad solicitada.
     */
    public void consumirCantidad(BigDecimal cantidad) throws StockBajoException {
        BigDecimal cantidadRestante = cantidad; // Almacena la cantidad que aún se necesita consumir.
        Iterator<Lote> iterator = lotes.iterator(); // Crea un iterador para recorrer los lotes.
        while (iterator.hasNext() && cantidadRestante.compareTo(BigDecimal.ZERO) > 0) {
            Lote lote = iterator.next(); // Obtiene el siguiente lote.
            BigDecimal cantidadLote = lote.getCantidad(); // Obtiene la cantidad disponible en el lote.
            // Si la cantidad del lote es menor o igual a la cantidad restante por consumir.
            if (cantidadLote.compareTo(cantidadRestante) <= 0) {
                cantidadRestante = cantidadRestante.subtract(cantidadLote); // Restar la cantidad del lote.
                iterator.remove(); // Elimina el lote ya que se consumió toda su cantidad.
            } else {
                // Si el lote tiene más cantidad que lo requerido, simplemente reduce la cantidad del lote.
                lote.reducirCantidad(cantidadRestante);
                cantidadRestante = BigDecimal.ZERO; // Ya no se requiere más.
            }
        }
        // Si aún queda cantidad por consumir, significa que no había suficiente stock.
        if (cantidadRestante.compareTo(BigDecimal.ZERO) > 0) {
            throw new StockBajoException("Stock insuficiente para el envase: " + nombre);
        }
    }

    /**
     * Método que devuelve una representación en cadena del objeto Envase.
     *
     * @return Una cadena con el estado actual del envase, incluyendo nombre, tipo, stock total, costo total, y lotes.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(); // Crea un objeto StringBuilder para construir la cadena.
        sb.append("Envase: ").append(nombre)
                .append("\nTipo: ").append(tipo)
                .append("\nStock Total: ").append(obtenerCantidadTotal()) // Agrega el stock total al string.
                .append("\nCosto Total: S/.").append(calcularCostoTotal().toPlainString()) // Agrega el costo total.
                .append("\nCosto Unitario: S/.").append(calcularCostoUnitarioTotal().toPlainString()) // Agrega el costo unitario.
                .append("\nLotes:\n");

        // Itera sobre todos los lotes y los añade al string.
        for (Lote lote : lotes) {
            sb.append(lote).append("\n"); // Agrega la información del lote.
        }

        return sb.toString(); // Devuelve la cadena completa.
    }
}
