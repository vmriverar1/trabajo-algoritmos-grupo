package Trabajo;

import java.util.Date;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Lote {
    private final String codigoLote;
    private BigDecimal cantidad;
    private final Date fechaIngreso;
    private Date fechaVencimiento;
    private BigDecimal costoUnitario;

    public Lote(String codigoLote, BigDecimal cantidad, Date fechaVencimiento, BigDecimal precioTotal) {
        if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        this.codigoLote = codigoLote;
        this.cantidad = cantidad;
        this.fechaIngreso = new Date(); 
        this.fechaVencimiento = fechaVencimiento;
        this.costoUnitario = precioTotal.divide(cantidad, 4, RoundingMode.HALF_UP);
    }

    // Constructor para envases sin fecha de vencimiento
    public Lote(String codigoLote, BigDecimal cantidad, BigDecimal precioTotal) {
        if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        this.codigoLote = codigoLote;
        this.cantidad = cantidad;
        this.fechaIngreso = new Date(); 
        this.fechaVencimiento = null;
        this.costoUnitario = precioTotal.divide(cantidad, 4, RoundingMode.HALF_UP);
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void reducirCantidad(BigDecimal cantidadReducir) {
        if (cantidadReducir.compareTo(cantidad) > 0) {
            throw new IllegalArgumentException("No se puede reducir más de lo disponible en el lote.");
        }
        cantidad = cantidad.subtract(cantidadReducir);
    }

    @Override
    public String toString() {
        return "Lote [Código: " + codigoLote + ", Cantidad: " + cantidad + ", Fecha Ingreso: " + fechaIngreso +
                ", Fecha Vencimiento: " + fechaVencimiento + ", Costo Unitario: S/." + costoUnitario + "]";
    }
}
