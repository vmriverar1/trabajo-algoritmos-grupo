package Trabajo;

import java.util.Date; // Para manejar fechas.
import java.math.BigDecimal; // Para manejar cantidades numéricas grandes con precisión decimal.
import java.math.RoundingMode; // Para establecer el modo de redondeo en operaciones matemáticas.

public class Lote {
    // Atributos de la clase Lote
    private final String codigoLote; // Código único para identificar el lote.
    private BigDecimal cantidad; // Cantidad total del lote.
    private final Date fechaIngreso; // Fecha en que el lote ingresó al inventario.
    private Date fechaVencimiento; // Fecha de vencimiento del lote (puede ser nula para algunos tipos de productos).
    private BigDecimal costoUnitario; // Costo por unidad del lote, calculado automáticamente.

    /**
     * Constructor para crear un Lote con fecha de vencimiento.
     *
     * @param codigoLote       Código identificador del lote.
     * @param cantidad         Cantidad de elementos en el lote.
     * @param fechaVencimiento Fecha de vencimiento del lote.
     * @param precioTotal      Precio total del lote, que se usará para calcular el costo unitario.
     */
    public Lote(String codigoLote, BigDecimal cantidad, Date fechaVencimiento, BigDecimal precioTotal) {
        // Validación para asegurarse de que la cantidad sea mayor que cero.
        if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        this.codigoLote = codigoLote;
        this.cantidad = cantidad;
        this.fechaIngreso = new Date(); // Fecha actual en el momento de la creación del lote.
        this.fechaVencimiento = fechaVencimiento;
        // Cálculo del costo unitario dividiendo el precio total entre la cantidad, redondeando a 4 decimales.
        this.costoUnitario = precioTotal.divide(cantidad, 4, RoundingMode.HALF_UP);
    }

    /**
     * Constructor para crear un Lote sin fecha de vencimiento (para productos que no caducan, como envases).
     *
     * @param codigoLote   Código identificador del lote.
     * @param cantidad     Cantidad de elementos en el lote.
     * @param precioTotal  Precio total del lote, que se usará para calcular el costo unitario.
     */
    public Lote(String codigoLote, BigDecimal cantidad, BigDecimal precioTotal) {
        // Validación para asegurarse de que la cantidad sea mayor que cero.
        if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        this.codigoLote = codigoLote;
        this.cantidad = cantidad;
        this.fechaIngreso = new Date(); // Fecha actual en el momento de la creación del lote.
        this.fechaVencimiento = null; // No hay fecha de vencimiento para este tipo de lote.
        // Cálculo del costo unitario dividiendo el precio total entre la cantidad, redondeando a 4 decimales.
        this.costoUnitario = precioTotal.divide(cantidad, 4, RoundingMode.HALF_UP);
    }

    /**
     * Método para obtener la cantidad actual del lote.
     *
     * @return La cantidad actual del lote.
     */
    public BigDecimal getCantidad() {
        return cantidad;
    }

    /**
     * Método para obtener el costo unitario de los productos en el lote.
     *
     * @return El costo unitario de cada producto en el lote.
     */
    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    /**
     * Método para reducir la cantidad del lote.
     * 
     * @param cantidadReducir La cantidad a reducir.
     * @throws IllegalArgumentException Si se intenta reducir una cantidad mayor a la disponible.
     */
    public void reducirCantidad(BigDecimal cantidadReducir) {
        // Verifica si la cantidad a reducir es mayor que la cantidad disponible en el lote.
        if (cantidadReducir.compareTo(cantidad) > 0) {
            throw new IllegalArgumentException("No se puede reducir más de lo disponible en el lote.");
        }
        // Resta la cantidad a reducir de la cantidad actual del lote.
        cantidad = cantidad.subtract(cantidadReducir);
    }

    /**
     * Método para convertir la información del lote a una cadena legible.
     *
     * @return Una representación en texto del lote.
     */
    @Override
    public String toString() {
        return "Lote [Código: " + codigoLote + ", Cantidad: " + cantidad + ", Fecha Ingreso: " + fechaIngreso +
                ", Fecha Vencimiento: " + fechaVencimiento + ", Costo Unitario: S/." + costoUnitario + "]";
    }
}
