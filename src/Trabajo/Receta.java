package Trabajo;

import java.util.HashMap; // Mapa que se usa para almacenar ingredientes y envases con sus cantidades.
import java.util.Iterator; // Para recorrer los elementos de los mapas.
import java.util.Map; // Para manejar las entradas del mapa.
import java.math.BigDecimal; // Para manejar cantidades con alta precisión decimal.
import java.math.RoundingMode; // Para controlar cómo se redondean los números decimales.

public class Receta {
    // Mapa para almacenar ingredientes y sus respectivas cantidades.
    private HashMap<Ingrediente, BigDecimal> ingredientes;
    // Mapa para almacenar envases y sus respectivas cantidades.
    private HashMap<Envase, BigDecimal> envases;

    // Constructor de la clase Receta.
    public Receta() {
        this.ingredientes = new HashMap<>(); // Inicializa el mapa de ingredientes vacío.
        this.envases = new HashMap<>(); // Inicializa el mapa de envases vacío.
    }

    /**
     * Método para agregar un ingrediente a la receta.
     *
     * @param ingrediente El ingrediente a agregar.
     * @param cantidad    La cantidad requerida de ese ingrediente.
     */
    public void agregarIngrediente(Ingrediente ingrediente, BigDecimal cantidad) {
        // Validación de parámetros: el ingrediente no puede ser nulo y la cantidad debe ser positiva.
        if (ingrediente == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Ingrediente no puede ser nulo y la cantidad debe ser positiva.");
        }
        ingredientes.put(ingrediente, cantidad); // Agrega el ingrediente al mapa.
    }

    /**
     * Método para agregar un envase a la receta.
     *
     * @param envase   El envase a agregar.
     * @param cantidad La cantidad requerida de ese envase.
     */
    public void agregarEnvase(Envase envase, BigDecimal cantidad) {
        // Validación de parámetros: el envase no puede ser nulo y la cantidad debe ser positiva.
        if (envase == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Envase no puede ser nulo y la cantidad debe ser positiva.");
        }
        envases.put(envase, cantidad); // Agrega el envase al mapa.
    }

    /**
     * Método para ajustar la receta según el stock disponible.
     * Si no hay suficiente stock de algún ingrediente o envase, ajusta la cantidad o lo elimina.
     */
    public void ajustarReceta() {
        ajustarIngredientes(); // Ajusta los ingredientes.
        ajustarEnvases(); // Ajusta los envases.
    }

    // Método privado para ajustar los ingredientes según el stock disponible.
    private void ajustarIngredientes() {
        Iterator<Map.Entry<Ingrediente, BigDecimal>> iterator = ingredientes.entrySet().iterator();
        ajustarIngredientesRecursivo(iterator); // Llama al método recursivo para ajustar cada ingrediente.
    }

    // Método recursivo para ajustar los ingredientes.
    private void ajustarIngredientesRecursivo(Iterator<Map.Entry<Ingrediente, BigDecimal>> iterator) {
        if (!iterator.hasNext()) {
            return; // Condición de parada: si no hay más ingredientes, se detiene.
        }
        Map.Entry<Ingrediente, BigDecimal> entry = iterator.next(); // Obtiene el siguiente ingrediente.
        Ingrediente ingrediente = entry.getKey();
        BigDecimal cantidadRequerida = entry.getValue();
        BigDecimal stockDisponible = ingrediente.obtenerCantidadTotal(); // Obtiene el stock disponible del ingrediente.

        // Si la cantidad requerida es mayor al stock disponible.
        if (cantidadRequerida.compareTo(stockDisponible) > 0) {
            System.out.println("Stock insuficiente para ingrediente: " + ingrediente.getNombre());
            if (stockDisponible.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println("Ajustando cantidad a " + stockDisponible);
                ingredientes.put(ingrediente, stockDisponible); // Ajusta la cantidad disponible.
            } else {
                System.out.println("Eliminando ingrediente de la receta: " + ingrediente.getNombre());
                iterator.remove(); // Elimina el ingrediente si no hay stock disponible.
            }
        }
        ajustarIngredientesRecursivo(iterator); // Llama recursivamente para ajustar el siguiente ingrediente.
    }

    // Método privado para ajustar los envases según el stock disponible.
    private void ajustarEnvases() {
        Iterator<Map.Entry<Envase, BigDecimal>> iterator = envases.entrySet().iterator();
        ajustarEnvasesRecursivo(iterator); // Llama al método recursivo para ajustar cada envase.
    }

    // Método recursivo para ajustar los envases.
    private void ajustarEnvasesRecursivo(Iterator<Map.Entry<Envase, BigDecimal>> iterator) {
        if (!iterator.hasNext()) {
            return; // Condición de parada: si no hay más envases, se detiene.
        }
        Map.Entry<Envase, BigDecimal> entry = iterator.next(); // Obtiene el siguiente envase.
        Envase envase = entry.getKey();
        BigDecimal cantidadRequerida = entry.getValue();
        BigDecimal stockDisponible = envase.obtenerCantidadTotal(); // Obtiene el stock disponible del envase.

        // Si la cantidad requerida es mayor al stock disponible.
        if (cantidadRequerida.compareTo(stockDisponible) > 0) {
            System.out.println("Stock insuficiente para envase: " + envase.getNombre());
            if (stockDisponible.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println("Ajustando cantidad a " + stockDisponible);
                envases.put(envase, stockDisponible); // Ajusta la cantidad disponible.
            } else {
                System.out.println("Eliminando envase de la receta: " + envase.getNombre());
                iterator.remove(); // Elimina el envase si no hay stock disponible.
            }
        }
        ajustarEnvasesRecursivo(iterator); // Llama recursivamente para ajustar el siguiente envase.
    }

    /**
     * Calcula el costo total de los ingredientes y envases de la receta.
     *
     * @return El costo total de la receta.
     */
    public BigDecimal calcularCostoTotal() {
        BigDecimal costoTotal = BigDecimal.ZERO;
        // Calcula el costo total de los ingredientes.
        for (Map.Entry<Ingrediente, BigDecimal> entry : ingredientes.entrySet()) {
            Ingrediente ingrediente = entry.getKey();
            BigDecimal cantidad = entry.getValue();
            BigDecimal costoUnitario = ingrediente.calcularCostoTotal().divide(ingrediente.obtenerCantidadTotal(), 4, RoundingMode.HALF_UP);
            costoTotal = costoTotal.add(costoUnitario.multiply(cantidad));
        }
        // Calcula el costo total de los envases.
        for (Map.Entry<Envase, BigDecimal> entry : envases.entrySet()) {
            Envase envase = entry.getKey();
            BigDecimal cantidad = entry.getValue();
            BigDecimal costoUnitario = envase.calcularCostoTotal().divide(envase.obtenerCantidadTotal(), 4, RoundingMode.HALF_UP);
            costoTotal = costoTotal.add(costoUnitario.multiply(cantidad));
        }
        return costoTotal.setScale(2, RoundingMode.HALF_UP); // Redondea el costo total a 2 decimales.
    }

    /**
     * Calcula la cantidad máxima que se puede producir según el stock de ingredientes y envases.
     *
     * @return La cantidad máxima que se puede producir.
     */
    public int calcularMaximaProduccion() {
        int maxProduccion = Integer.MAX_VALUE;
        // Calcula la máxima producción en función de los ingredientes.
        for (Map.Entry<Ingrediente, BigDecimal> entry : ingredientes.entrySet()) {
            Ingrediente ingrediente = entry.getKey();
            BigDecimal cantidadRequerida = entry.getValue();
            BigDecimal stockDisponible = ingrediente.obtenerCantidadTotal();
            int maxConIngrediente = stockDisponible.divide(cantidadRequerida, RoundingMode.DOWN).intValue();
            if (maxConIngrediente < maxProduccion) {
                maxProduccion = maxConIngrediente; // Ajusta la producción máxima.
            }
        }
        // Calcula la máxima producción en función de los envases.
        for (Map.Entry<Envase, BigDecimal> entry : envases.entrySet()) {
            Envase envase = entry.getKey();
            BigDecimal cantidadRequerida = entry.getValue();
            BigDecimal stockDisponible = envase.obtenerCantidadTotal();
            int maxConEnvase = stockDisponible.divide(cantidadRequerida, RoundingMode.DOWN).intValue();
            if (maxConEnvase < maxProduccion) {
                maxProduccion = maxConEnvase; // Ajusta la producción máxima.
            }
        }
        return maxProduccion; // Devuelve la cantidad máxima de producción.
    }

    /**
     * Consume los ingredientes necesarios para producir la cantidad especificada.
     *
     * @param cantidadProduccion La cantidad a producir.
     * @throws StockBajoException Si no hay suficiente stock.
     */
    public void consumirIngredientes(BigDecimal cantidadProduccion) throws StockBajoException {
        // Recorre los ingredientes y consume la cantidad necesaria para la producción.
        for (Map.Entry<Ingrediente, BigDecimal> entry : ingredientes.entrySet()) {
            Ingrediente ingrediente = entry.getKey();
            BigDecimal cantidadRequerida = entry.getValue().multiply(cantidadProduccion);
            ingrediente.consumirCantidad(cantidadRequerida); // Reduce el stock del ingrediente.
        }
    }

    /**
     * Consume los envases necesarios para producir la cantidad especificada.
     *
     * @param cantidadProduccion La cantidad a producir.
     * @throws StockBajoException Si no hay suficiente stock.
     */
    public void consumirEnvases(BigDecimal cantidadProduccion) throws StockBajoException {
        // Recorre los envases y consume la cantidad necesaria para la producción.
        for (Map.Entry<Envase, BigDecimal> entry : envases.entrySet()) {
            Envase envase = entry.getKey();
            BigDecimal cantidadRequerida = entry.getValue().multiply(cantidadProduccion);
            envase.consumirCantidad(cantidadRequerida); // Reduce el stock del envase.
        }
    }

    // Representación en cadena de la receta con ingredientes, envases y costos.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ingredientes:\n");
        // Agrega la información de cada ingrediente.
        for (Map.Entry<Ingrediente, BigDecimal> entry : ingredientes.entrySet()) {
            sb.append("- ").append(entry.getKey().getNombre())
                    .append(": ").append(entry.getValue()).append(" unidades\n");
        }
        sb.append("Envases:\n");
        // Agrega la información de cada envase.
        for (Map.Entry<Envase, BigDecimal> entry : envases.entrySet()) {
            sb.append("- ").append(entry.getKey().getNombre())
                    .append(": ").append(entry.getValue()).append(" unidades\n");
        }
        // Agrega el costo total de la receta.
        sb.append("Costo Total de la Receta: S/.").append(calcularCostoTotal().toPlainString()).append("\n");
        return sb.toString(); // Devuelve la representación en cadena de la receta.
    }
}
