package Trabajo;

import java.io.BufferedReader; // Para leer el archivo línea por línea.
import java.io.FileReader; // Para abrir el archivo en modo lectura.
import java.io.IOException; // Para manejar posibles excepciones de entrada/salida.
import java.util.HashMap; // Para almacenar las credenciales de usuario y contraseña.

public class LectorUsuarioContrasena {
    // Patrón Singleton: instancia única de la clase.
    private static LectorUsuarioContrasena instancia;

    // Constructor privado para evitar instanciación externa.
    private LectorUsuarioContrasena() {}

    /**
     * Método para obtener la instancia única de la clase (Singleton).
     * @return La instancia única de LectorUsuarioContrasena.
     */
    public static LectorUsuarioContrasena getInstance() {
        // Si la instancia no ha sido creada, la crea.
        if (instancia == null) {
            instancia = new LectorUsuarioContrasena();
        }
        return instancia; // Retorna la única instancia disponible.
    }

    /**
     * Método que lee un archivo de texto con usuarios y contraseñas y los almacena en un HashMap.
     * El formato del archivo debe ser "usuario-contraseña" en cada línea.
     * 
     * @param filePath La ruta del archivo de usuarios y contraseñas.
     * @param credenciales Un HashMap que almacenará los usuarios como claves y las contraseñas como valores.
     * @return Un valor entero indicando si se cargaron credenciales válidas (1 si se encontraron, -1 si hubo errores).
     */
    public int leerArchivoUsuarioYContrasena(String filePath, HashMap<String, String> credenciales) {
        // Valida si la ruta del archivo es nula o está vacía.
        if (filePath == null || filePath.isEmpty()) {
            System.out.println("El archivo de ruta no puede ser nulo o vacío.");
            return -1; // Retorna -1 si la ruta es inválida.
        }

        // Intenta abrir y leer el archivo.
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineasValidas = 0; // Contador de líneas válidas procesadas.

            // Lee el archivo línea por línea.
            while ((line = br.readLine()) != null) {
                // Divide la línea en dos partes usando el carácter '-' como separador.
                String[] parts = line.split("-");
                
                // Verifica si la línea tiene exactamente dos partes (usuario y contraseña).
                if (parts.length == 2) {
                    String username = parts[0].trim(); // Remueve espacios en blanco del nombre de usuario.
                    String password = parts[1].trim(); // Remueve espacios en blanco de la contraseña.

                    // Verifica que el usuario y la contraseña no estén vacíos.
                    if (!username.isEmpty() && !password.isEmpty()) {
                        // Añade el usuario y contraseña al HashMap de credenciales.
                        credenciales.put(username, password);
                        lineasValidas++; // Incrementa el contador de líneas válidas.
                    } else {
                        System.out.println("Línea inválida (datos vacíos): " + line); // Mensaje si los datos están vacíos.
                    }
                } else {
                    System.out.println("Línea inválida (formato incorrecto): " + line); // Mensaje si el formato es incorrecto.
                }
            }

            // Verifica si se encontraron líneas válidas.
            if (lineasValidas > 0) {
                System.out.println("Credenciales cargadas exitosamente.");
                return 1; // Retorna 1 si se cargaron correctamente las credenciales.
            } else {
                System.out.println("No se encontraron credenciales válidas en el archivo.");
                return -1; // Retorna -1 si no se encontró ninguna credencial válida.
            }

        } catch (IOException e) {
            // Captura cualquier error de entrada/salida durante la lectura del archivo.
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return -1; // Retorna -1 si ocurre un error al leer el archivo.
        }
    }
}
