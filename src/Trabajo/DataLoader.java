package Trabajo;

import java.io.BufferedReader; // Clase utilizada para leer archivos línea por línea.
import java.io.FileReader; // Clase utilizada para abrir y leer archivos.
import java.io.IOException; // Excepción que puede ocurrir al trabajar con archivos.
import java.math.BigDecimal; // Clase utilizada para manejar cantidades numéricas con alta precisión.
import java.util.Date; // Clase utilizada para manejar fechas.
import java.text.SimpleDateFormat; // Clase utilizada para formatear y analizar fechas.
import java.util.UUID; // Clase utilizada para generar identificadores únicos.

public class DataLoader {

    /**
     * Método para cargar datos desde un archivo y almacenarlos en un objeto `Inventario`.
     * El archivo tiene diferentes tipos de datos (ingredientes, envases, lotes), y este método
     * los procesa y agrega al inventario correspondiente.
     *
     * @param filePath   Ruta del archivo desde el cual se cargarán los datos.
     * @param inventario Objeto de tipo `Inventario` donde se almacenarán los ingredientes y envases cargados.
     */
    public void cargarDatos(String filePath, Inventario inventario) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line; // Variable para almacenar cada línea leída del archivo.
            Ingrediente ingredienteActual = null; // Variable para almacenar el ingrediente en proceso.
            Envase envaseActual = null; // Variable para almacenar el envase en proceso.

            // Leer línea por línea del archivo hasta que no queden más líneas.
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|"); // Dividir la línea en partes utilizando el carácter '|'.

                // Si la línea corresponde a un ingrediente.
                if (parts[0].equalsIgnoreCase("Ingrediente")) {
                    String nombre = parts[1]; // Nombre del ingrediente.
                    String categoria = parts[2]; // Categoría del ingrediente.
                    ingredienteActual = new Ingrediente(nombre, categoria); // Crear un nuevo objeto Ingrediente.
                    inventario.agregarIngrediente(ingredienteActual); // Agregar el ingrediente al inventario.
                    envaseActual = null; // Resetear la variable envaseActual ya que estamos procesando un ingrediente.
                
                // Si la línea corresponde a un envase.
                } else if (parts[0].equalsIgnoreCase("Envase")) {
                    String nombre = parts[1]; // Nombre del envase.
                    String tipo = parts[2]; // Tipo de envase.
                    envaseActual = new Envase(nombre, tipo); // Crear un nuevo objeto Envase.
                    inventario.agregarEnvase(envaseActual); // Agregar el envase al inventario.
                    ingredienteActual = null; // Resetear la variable ingredienteActual ya que estamos procesando un envase.
                
                // Si la línea corresponde a un lote.
                } else if (parts[0].equalsIgnoreCase("Lote")) {
                    // Si estamos procesando un ingrediente.
                    if (ingredienteActual != null) {
                        BigDecimal cantidad = new BigDecimal(parts[1]); // Cantidad del lote.
                        BigDecimal precioTotal = new BigDecimal(parts[2]); // Precio total del lote.
                        Date fechaVencimiento = parseFecha(parts[3]); // Fecha de vencimiento del lote.
                        String codigoLote = UUID.randomUUID().toString(); // Generar un código único para el lote.
                        Lote lote = new Lote(codigoLote, cantidad, fechaVencimiento, precioTotal); // Crear el lote.
                        ingredienteActual.agregarLote(lote); // Agregar el lote al ingrediente actual.
                    
                    // Si estamos procesando un envase.
                    } else if (envaseActual != null) {
                        BigDecimal cantidad = new BigDecimal(parts[1]); // Cantidad del lote.
                        BigDecimal precioTotal = new BigDecimal(parts[2]); // Precio total del lote.
                        String codigoLote = UUID.randomUUID().toString(); // Generar un código único para el lote.
                        Lote lote = new Lote(codigoLote, cantidad, precioTotal); // Crear el lote.
                        envaseActual.agregarLote(lote); // Agregar el lote al envase actual.
                    }
                }
            }
            // Imprimir mensaje indicando que los datos fueron cargados exitosamente.
            System.out.println("Datos cargados exitosamente desde " + filePath);
        
        // Manejar cualquier excepción de entrada/salida que pueda ocurrir al leer el archivo.
        } catch (IOException e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
        }
    }

    /**
     * Método auxiliar para analizar (parsear) una fecha desde una cadena de texto.
     * Si la fecha es inválida, se usa la fecha actual como valor por defecto.
     *
     * @param fechaStr Cadena de texto que contiene una fecha en formato "dd/MM/yyyy".
     * @return Un objeto de tipo `Date` que representa la fecha.
     */
    private Date parseFecha(String fechaStr) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(fechaStr); // Parsear la fecha desde el formato especificado.
        } catch (Exception e) {
            // Si ocurre un error, imprimir mensaje de advertencia y devolver la fecha actual.
            System.out.println("Fecha inválida en los datos: " + fechaStr + ". Se usará la fecha actual.");
            return new Date(); // Retornar la fecha actual si el formato es incorrecto.
        }
    }
}
