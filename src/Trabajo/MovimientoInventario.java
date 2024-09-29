// MovimientoInventario.java
package Trabajo;

import java.util.Date;
import java.text.SimpleDateFormat;

public class MovimientoInventario {
    private String tipoMovimiento;
    private Date fecha;
    private String descripcion;
    private int cantidad;

    public MovimientoInventario(String tipoMovimiento, Date fecha, String descripcion, int cantidad) {
        this.tipoMovimiento = tipoMovimiento;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return "Movimiento [" + tipoMovimiento + "] - Fecha: " + sdf.format(fecha) + ", Descripción: " + descripcion + ", Cantidad: " + cantidad;
    }
}
