package Trabajo;

import java.util.Iterator;
import java.math.BigDecimal;

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

    public int obtenerCantidadTotal() {
        return obtenerCantidadTotalRecursivo(lotes.iterator(), 0);
    }

    private int obtenerCantidadTotalRecursivo(Iterator<Lote> iterator, int acumulador) {
        if (!iterator.hasNext()) {
            return acumulador; 
        }
        Lote lote = iterator.next();
        acumulador += lote.getCantidad(); 
        return obtenerCantidadTotalRecursivo(iterator, acumulador); 
    }

    public BigDecimal calcularCostoTotal() {
        BigDecimal costoTotal = BigDecimal.ZERO;
        for (Lote lote : lotes) {
            BigDecimal cantidad = new BigDecimal(lote.getCantidad());
            costoTotal = costoTotal.add(lote.getCostoUnitario().multiply(cantidad));
        }
        return costoTotal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Producto: ").append(nombre)
                .append("\nCategoría: ").append(categoria)
                .append("\nStock Total: ").append(obtenerCantidadTotal())
                .append("\nCosto Total: S/.").append(calcularCostoTotal())
                .append("\nReceta:\n").append(receta)
                .append("\nLotes:\n");

        for (Lote lote : lotes) {
            sb.append(lote).append("\n");
        }

        return sb.toString();
    }
}
