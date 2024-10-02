package Trabajo;

import java.util.Iterator;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Ingrediente {
    private final String nombre;
    private final String categoria;
    private final CircularLinkedList<Lote> lotes;

    public Ingrediente(String nombre, String categoria) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.lotes = new CircularLinkedList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void agregarLote(Lote lote) {
        lotes.addLast(lote);
    }

    public BigDecimal obtenerCantidadTotal() {
        return obtenerCantidadTotalRecursivo(lotes.iterator(), BigDecimal.ZERO);
    }

    private BigDecimal obtenerCantidadTotalRecursivo(Iterator<Lote> iterator, BigDecimal acumulador) {
        if (!iterator.hasNext()) {
            return acumulador;
        }
        Lote lote = iterator.next();
        acumulador = acumulador.add(lote.getCantidad());
        return obtenerCantidadTotalRecursivo(iterator, acumulador);
    }

    public BigDecimal calcularCostoTotal() {
        BigDecimal costoTotal = BigDecimal.ZERO;
        for (Lote lote : lotes) {
            costoTotal = costoTotal.add(lote.getCostoUnitario().multiply(lote.getCantidad()));
        }
        return costoTotal.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularCostoUnitarioTotal() {
        BigDecimal costoUnitarioTotal = BigDecimal.ZERO;
        for (Lote lote : lotes) {
            costoUnitarioTotal = costoUnitarioTotal.add(lote.getCostoUnitario());
        }
        return costoUnitarioTotal.setScale(2, RoundingMode.HALF_UP);
    }

    public void consumirCantidad(BigDecimal cantidad) throws StockBajoException {
        BigDecimal cantidadRestante = cantidad;
        Iterator<Lote> iterator = lotes.iterator();
        while (iterator.hasNext() && cantidadRestante.compareTo(BigDecimal.ZERO) > 0) {
            Lote lote = iterator.next();
            BigDecimal cantidadLote = lote.getCantidad();
            if (cantidadLote.compareTo(cantidadRestante) <= 0) {
                cantidadRestante = cantidadRestante.subtract(cantidadLote);
                iterator.remove();
            } else {
                lote.reducirCantidad(cantidadRestante);
                cantidadRestante = BigDecimal.ZERO;
            }
        }
        if (cantidadRestante.compareTo(BigDecimal.ZERO) > 0) {
            throw new StockBajoException("Stock insuficiente para el ingrediente: " + nombre);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ingrediente: ").append(nombre)
                .append("\nCategoría: ").append(categoria)
                .append("\nStock Total: ").append(obtenerCantidadTotal())
                .append("\nCosto Total: S/.").append(calcularCostoTotal().toPlainString())
                .append("\nCosto Unitario S/.").append(calcularCostoUnitarioTotal().toPlainString())
                .append("\nLotes:\n");

        for (Lote lote : lotes) {
            sb.append(lote).append("\n");
        }

        return sb.toString();
    }
}
