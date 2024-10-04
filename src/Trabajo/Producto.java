package Trabajo;

import java.util.Iterator;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Producto {
    private final String nombre;
    private final String categoria;
    private final Receta receta;
    private final CircularLinkedList<Lote> lotes;

    public Producto(String nombre, String categoria, Receta receta) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.receta = receta;
        this.lotes = new CircularLinkedList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public Receta getReceta() {
        return receta;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Producto: ").append(nombre)
                .append("\nCategoría: ").append(categoria)
                .append("\nStock Total: ").append(obtenerCantidadTotal())
                .append("\nReceta:\n").append(receta)
                .append("\nLotes:\n");

        for (Lote lote : lotes) {
            sb.append(lote).append("\n");
        }

        return sb.toString();
    }
}
