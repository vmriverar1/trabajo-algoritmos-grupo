package Trabajo;

import java.util.Date; // Para manejar fechas.
import java.text.SimpleDateFormat; // Para formatear las fechas de manera legible.
import java.math.BigDecimal; // Para manejar cantidades numéricas con precisión decimal.

public class MovimientoInventario {
    // Atributos de la clase MovimientoInventario
    private String tipoMovimiento; // Tipo de movimiento (ej. "Entrada", "Salida", "Producción", etc.).
    private Date fecha; // Fecha en la que se realiza el movimiento.
    private String descripcion; // Descripción del movimiento (ej. qué producto o lote se está moviendo).
    private BigDecimal cantidad; // Cantidad involucrada en el movimiento (ej. cuántas unidades se están moviendo).

    /**
     * Constructor que inicializa un Movimiento de Inventario con los valores proporcionados.
     *
     * @param tipoMovimiento El tipo de movimiento (ej. "Entrada" o "Salida").
     * @param fecha          La fecha en la que ocurrió el movimiento.
     * @param descripcion    Una breve descripción del movimiento.
     * @param cantidad       La cantidad de elementos involucrados en el movimiento.
     */
    public MovimientoInventario(String tipoMovimiento, Date fecha, String descripcion, BigDecimal cantidad) {
        this.tipoMovimiento = tipoMovimiento;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }

    /**
     * Método toString que proporciona una representación legible del movimiento de inventario.
     * Devuelve una cadena que incluye el tipo de movimiento, la fecha, la descripción y la cantidad involucrada.
     *
     * @return Una cadena que describe el movimiento.
     */
    @Override
    public String toString() {
        // Creamos un formateador de fecha para mostrar la fecha en formato dd/MM/yyyy.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        // Retornamos la descripción del movimiento de manera estructurada.
        return "Movimiento [" + tipoMovimiento + "] - Fecha: " + sdf.format(fecha) + ", Descripción: " + descripcion + ", Cantidad: " + cantidad + " unidades";
    }
}
