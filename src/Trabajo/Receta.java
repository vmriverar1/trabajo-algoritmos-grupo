package Trabajo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Receta {
    private HashMap<Ingrediente, BigDecimal> ingredientes;
    private HashMap<Envase, BigDecimal> envases;

    public Receta() {
        this.ingredientes = new HashMap<>();
        this.envases = new HashMap<>();
    }

    public void agregarIngrediente(Ingrediente ingrediente, BigDecimal cantidad) {
        if (ingrediente == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Ingrediente no puede ser nulo y la cantidad debe ser positiva.");
        }
        ingredientes.put(ingrediente, cantidad);
    }

    public void agregarEnvase(Envase envase, BigDecimal cantidad) {
        if (envase == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Envase no puede ser nulo y la cantidad debe ser positiva.");
        }
        envases.put(envase, cantidad);
    }

    public void ajustarReceta() {
        ajustarIngredientes();
        ajustarEnvases();
    }

    private void ajustarIngredientes() {
        Iterator<Map.Entry<Ingrediente, BigDecimal>> iterator = ingredientes.entrySet().iterator();
        ajustarIngredientesRecursivo(iterator);
    }

    private void ajustarIngredientesRecursivo(Iterator<Map.Entry<Ingrediente, BigDecimal>> iterator) {
        if (!iterator.hasNext()) {
            return;
        }
        Map.Entry<Ingrediente, BigDecimal> entry = iterator.next();
        Ingrediente ingrediente = entry.getKey();
        BigDecimal cantidadRequerida = entry.getValue();
        BigDecimal stockDisponible = ingrediente.obtenerCantidadTotal();

        if (cantidadRequerida.compareTo(stockDisponible) > 0) {
            System.out.println("Stock insuficiente para ingrediente: " + ingrediente.getNombre());
            if (stockDisponible.compareTo(BigDecimal.ZERO) > 0) {
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
        Iterator<Map.Entry<Envase, BigDecimal>> iterator = envases.entrySet().iterator();
        ajustarEnvasesRecursivo(iterator);
    }

    private void ajustarEnvasesRecursivo(Iterator<Map.Entry<Envase, BigDecimal>> iterator) {
        if (!iterator.hasNext()) {
            return;
        }
        Map.Entry<Envase, BigDecimal> entry = iterator.next();
        Envase envase = entry.getKey();
        BigDecimal cantidadRequerida = entry.getValue();
        BigDecimal stockDisponible = envase.obtenerCantidadTotal();

        if (cantidadRequerida.compareTo(stockDisponible) > 0) {
            System.out.println("Stock insuficiente para envase: " + envase.getNombre());
            if (stockDisponible.compareTo(BigDecimal.ZERO) > 0) {
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
        for (Map.Entry<Ingrediente, BigDecimal> entry : ingredientes.entrySet()) {
            Ingrediente ingrediente = entry.getKey();
            BigDecimal cantidad = entry.getValue();
            BigDecimal costoUnitario = ingrediente.calcularCostoTotal().divide(ingrediente.obtenerCantidadTotal(), 4, RoundingMode.HALF_UP);
            costoTotal = costoTotal.add(costoUnitario.multiply(cantidad));
        }
        for (Map.Entry<Envase, BigDecimal> entry : envases.entrySet()) {
            Envase envase = entry.getKey();
            BigDecimal cantidad = entry.getValue();
            BigDecimal costoUnitario = envase.calcularCostoTotal().divide(envase.obtenerCantidadTotal(), 4, RoundingMode.HALF_UP);
            costoTotal = costoTotal.add(costoUnitario.multiply(cantidad));
        }
        return costoTotal.setScale(2, RoundingMode.HALF_UP);
    }

    public int calcularMaximaProduccion() {
        int maxProduccion = Integer.MAX_VALUE;
        for (Map.Entry<Ingrediente, BigDecimal> entry : ingredientes.entrySet()) {
            Ingrediente ingrediente = entry.getKey();
            BigDecimal cantidadRequerida = entry.getValue();
            BigDecimal stockDisponible = ingrediente.obtenerCantidadTotal();
            int maxConIngrediente = stockDisponible.divide(cantidadRequerida, RoundingMode.DOWN).intValue();
            if (maxConIngrediente < maxProduccion) {
                maxProduccion = maxConIngrediente;
            }
        }
        for (Map.Entry<Envase, BigDecimal> entry : envases.entrySet()) {
            Envase envase = entry.getKey();
            BigDecimal cantidadRequerida = entry.getValue();
            BigDecimal stockDisponible = envase.obtenerCantidadTotal();
            int maxConEnvase = stockDisponible.divide(cantidadRequerida, RoundingMode.DOWN).intValue();
            if (maxConEnvase < maxProduccion) {
                maxProduccion = maxConEnvase;
            }
        }
        return maxProduccion;
    }

    public void consumirIngredientes(BigDecimal cantidadProduccion) throws StockBajoException {
        for (Map.Entry<Ingrediente, BigDecimal> entry : ingredientes.entrySet()) {
            Ingrediente ingrediente = entry.getKey();
            BigDecimal cantidadRequerida = entry.getValue().multiply(cantidadProduccion);
            ingrediente.consumirCantidad(cantidadRequerida);
        }
    }

    public void consumirEnvases(BigDecimal cantidadProduccion) throws StockBajoException {
        for (Map.Entry<Envase, BigDecimal> entry : envases.entrySet()) {
            Envase envase = entry.getKey();
            BigDecimal cantidadRequerida = entry.getValue().multiply(cantidadProduccion);
            envase.consumirCantidad(cantidadRequerida);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ingredientes:\n");
        for (Map.Entry<Ingrediente, BigDecimal> entry : ingredientes.entrySet()) {
            sb.append("- ").append(entry.getKey().getNombre())
                    .append(": ").append(entry.getValue()).append(" unidades\n");
        }
        sb.append("Envases:\n");
        for (Map.Entry<Envase, BigDecimal> entry : envases.entrySet()) {
            sb.append("- ").append(entry.getKey().getNombre())
                    .append(": ").append(entry.getValue()).append(" unidades\n");
        }
        sb.append("Costo Total de la Receta: S/.").append(calcularCostoTotal().toPlainString()).append("\n");
        return sb.toString();
    }
}
