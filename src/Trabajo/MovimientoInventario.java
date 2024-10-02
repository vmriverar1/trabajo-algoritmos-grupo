package Trabajo;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;

public class MovimientoInventario {
    private String tipoMovimiento;
    private Date fecha;
    private String descripcion;
    private BigDecimal cantidad;

    public MovimientoInventario(String tipoMovimiento, Date fecha, String descripcion, BigDecimal cantidad) {
        this.tipoMovimiento = tipoMovimiento;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return "Movimiento [" + tipoMovimiento + "] - Fecha: " + sdf.format(fecha) + ", Descripción: " + descripcion + ", Cantidad: " + cantidad + " unidades";
    }
}
