// Receta.java
package Trabajo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Receta {
    private HashMap<Ingrediente, Integer> ingredientes;
    private HashMap<Envase, Integer> envases;

    public Receta() {
        this.ingredientes = new HashMap<>();
        this.envases = new HashMap<>();
    }

    public void agregarIngrediente(Ingrediente ingrediente, int cantidad) {
        if (ingrediente == null || cantidad <= 0) {
            throw new IllegalArgumentException("Ingrediente no puede ser nulo y la cantidad debe ser positiva.");
        }
        ingredientes.put(ingrediente, cantidad);
    }

    public void agregarEnvase(Envase envase, int cantidad) {
        if (envase == null || cantidad <= 0) {
            throw new IllegalArgumentException("Envase no puede ser nulo y la cantidad debe ser positiva.");
        }
        envases.put(envase, cantidad);
    }

    public void ajustarReceta() {
        ajustarIngredientes();
        ajustarEnvases();
    }

    private void ajustarIngredientes() {
        Iterator<Map.Entry<Ingrediente, Integer>> iterator = ingredientes.entrySet().iterator();
        ajustarIngredientesRecursivo(iterator);
    }

    private void ajustarIngredientesRecursivo(Iterator<Map.Entry<Ingrediente, Integer>> iterator) {
        if (!iterator.hasNext()) {
            return;
        }
        Map.Entry<Ingrediente, Integer> entry = iterator.next();
        Ingrediente ingrediente = entry.getKey();
        int cantidadRequerida = entry.getValue();
        int stockDisponible = ingrediente.obtenerCantidadTotal();

        if (cantidadRequerida > stockDisponible) {
            System.out.println("Stock insuficiente para ingrediente: " + ingrediente.getNombre());
            if (stockDisponible > 0) {
                System.out.println("Ajustando cantidad a " + stockDisponible);
                ingredientes.put(ingrediente, stockDisponible);
            } else {
                System.out.println("Eliminando ingrediente de la receta: " + ingrediente.getNombre());
                iterator.remove();
            }
        }
        ajustarIngredientesRecursivo(iterator);
    }

    private void ajustarEnvases() {
        Iterator<Map.Entry<Envase, Integer>> iterator = envases.entrySet().iterator();
        ajustarEnvasesRecursivo(iterator);
    }

    private void ajustarEnvasesRecursivo(Iterator<Map.Entry<Envase, Integer>> iterator) {
        if (!iterator.hasNext()) {
            return;
        }
        Map.Entry<Envase, Integer> entry = iterator.next();
        Envase envase = entry.getKey();
        int cantidadRequerida = entry.getValue();
        int stockDisponible = envase.obtenerCantidadTotal();

        if (cantidadRequerida > stockDisponible) {
            System.out.println("Stock insuficiente para envase: " + envase.getNombre());
            if (stockDisponible > 0) {
                System.out.println("Ajustando cantidad a " + stockDisponible);
                envases.put(envase, stockDisponible);
            } else {
                System.out.println("Eliminando envase de la receta: " + envase.getNombre());
                iterator.remove();
            }
        }
        ajustarEnvasesRecursivo(iterator);
    }

    public BigDecimal calcularCostoTotal() {
        BigDecimal costoTotal = BigDecimal.ZERO;
        for (Map.Entry<Ingrediente, Integer> entry : ingredientes.entrySet()) {
            Ingrediente ingrediente = entry.getKey();
            int cantidad = entry.getValue();
            BigDecimal costoUnitario = ingrediente.calcularCostoTotal().divide(new BigDecimal(ingrediente.obtenerCantidadTotal()), 4, RoundingMode.HALF_UP);
            costoTotal = costoTotal.add(costoUnitario.multiply(new BigDecimal(cantidad)));
        }
        for (Map.Entry<Envase, Integer> entry : envases.entrySet()) {
            Envase envase = entry.getKey();
            int cantidad = entry.getValue();
            BigDecimal costoUnitario = envase.calcularCostoTotal().divide(new BigDecimal(envase.obtenerCantidadTotal()), 4, RoundingMode.HALF_UP);
            costoTotal = costoTotal.add(costoUnitario.multiply(new BigDecimal(cantidad)));
        }
        return costoTotal.setScale(2, RoundingMode.HALF_UP);
    }

    public int calcularMaximaProduccion() {
        int maxProduccion = Integer.MAX_VALUE;
        for (Map.Entry<Ingrediente, Integer> entry : ingredientes.entrySet()) {
            Ingrediente ingrediente = entry.getKey();
            int cantidadRequerida = entry.getValue();
            int stockDisponible = ingrediente.obtenerCantidadTotal();
            int maxConIngrediente = stockDisponible / cantidadRequerida;
            if (maxConIngrediente < maxProduccion) {
                maxProduccion = maxConIngrediente;
            }
        }
        for (Map.Entry<Envase, Integer> entry : envases.entrySet()) {
            Envase envase = entry.getKey();
            int cantidadRequerida = entry.getValue();
            int stockDisponible = envase.obtenerCantidadTotal();
            int maxConEnvase = stockDisponible / cantidadRequerida;
            if (maxConEnvase < maxProduccion) {
                maxProduccion = maxConEnvase;
            }
        }
        return maxProduccion;
    }

    public void consumirIngredientes(int cantidadProduccion) throws StockBajoException {
        for (Map.Entry<Ingrediente, Integer> entry : ingredientes.entrySet()) {
            Ingrediente ingrediente = entry.getKey();
            int cantidadRequerida = entry.getValue() * cantidadProduccion;
            ingrediente.consumirCantidad(cantidadRequerida);
        }
    }

    public void consumirEnvases(int cantidadProduccion) throws StockBajoException {
        for (Map.Entry<Envase, Integer> entry : envases.entrySet()) {
            Envase envase = entry.getKey();
            int cantidadRequerida = entry.getValue() * cantidadProduccion;
            envase.consumirCantidad(cantidadRequerida);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ingredientes:\n");
        for (Map.Entry<Ingrediente, Integer> entry : ingredientes.entrySet()) {
            sb.append("- ").append(entry.getKey().getNombre())
                    .append(": ").append(entry.getValue()).append(" unidades\n");
        }
        sb.append("Envases:\n");
        for (Map.Entry<Envase, Integer> entry : envases.entrySet()) {
            sb.append("- ").append(entry.getKey().getNombre())
                    .append(": ").append(entry.getValue()).append(" unidades\n");
        }
        sb.append("Costo Total de la Receta: S/.").append(calcularCostoTotal().toPlainString()).append("\n");
        return sb.toString();
    }
}
