package Trabajo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.UUID; 

public class DataLoader {
    public void cargarDatos(String filePath, Inventario inventario) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            Ingrediente ingredienteActual = null;
            Envase envaseActual = null;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts[0].equalsIgnoreCase("Ingrediente")) {
                    String nombre = parts[1];
                    String categoria = parts[2];
                    ingredienteActual = new Ingrediente(nombre, categoria);
                    inventario.agregarIngrediente(ingredienteActual);
                    envaseActual = null; // Reset envaseActual
                } else if (parts[0].equalsIgnoreCase("Envase")) {
                    String nombre = parts[1];
                    String tipo = parts[2];
                    envaseActual = new Envase(nombre, tipo);
                    inventario.agregarEnvase(envaseActual);
                    ingredienteActual = null; // Reset ingredienteActual
                } else if (parts[0].equalsIgnoreCase("Lote")) {
                    if (ingredienteActual != null) {
                        BigDecimal cantidad = new BigDecimal(parts[1]);
                        BigDecimal precioTotal = new BigDecimal(parts[2]);
                        Date fechaVencimiento = parseFecha(parts[3]);
                        String codigoLote = UUID.randomUUID().toString();
                        Lote lote = new Lote(codigoLote, cantidad, fechaVencimiento, precioTotal);
                        ingredienteActual.agregarLote(lote);
                    } else if (envaseActual != null) {
                        BigDecimal cantidad = new BigDecimal(parts[1]);
                        BigDecimal precioTotal = new BigDecimal(parts[2]);
                        String codigoLote = UUID.randomUUID().toString();
                        Lote lote = new Lote(codigoLote, cantidad, precioTotal);
                        envaseActual.agregarLote(lote);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
        }
    }

    private Date parseFecha(String fechaStr) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);
        } catch (Exception e) {
            System.out.println("Fecha inválida en los datos: " + fechaStr + ". Se usará la fecha actual.");
            return new Date(); 
        }
    }
}
