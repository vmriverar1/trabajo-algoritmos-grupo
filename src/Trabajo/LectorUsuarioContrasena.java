package Trabajo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class LectorUsuarioContrasena {
    private static LectorUsuarioContrasena instancia;

    private LectorUsuarioContrasena() {}

    public static LectorUsuarioContrasena getInstance() {
        if (instancia == null) {
            instancia = new LectorUsuarioContrasena();
        }
        return instancia;
    }

    public int leerArchivoUsuarioYContrasena(String filePath, HashMap<String, String> credenciales) {
        if (filePath == null || filePath.isEmpty()) {
            System.out.println("El archivo de ruta no puede ser nulo o vacío.");
            return -1;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineasValidas = 0;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("-");
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();

                    if (!username.isEmpty() && !password.isEmpty()) {
                        credenciales.put(username, password);
                        lineasValidas++;
                    } else {
                        System.out.println("Línea inválida (datos vacíos): " + line);
                    }
                } else {
                    System.out.println("Línea inválida (formato incorrecto): " + line);
                }
            }

            if (lineasValidas > 0) {
                System.out.println("Credenciales cargadas exitosamente.");
                return 1; 
            } else {
                System.out.println("No se encontraron credenciales válidas en el archivo.");
                return -1;
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return -1;
        }
    }
}
