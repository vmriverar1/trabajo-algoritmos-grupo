package Trabajo;

// Clase Usuario que representa un usuario del sistema
public class Usuario {
    // Atributos privados: nombre de usuario y contraseña
    private String nombreUsuario;
    private String contrasena;

    // Constructor que inicializa los atributos con los valores proporcionados
    public Usuario(String nombreUsuario, String contrasena) {
        // Asigna el nombre de usuario proporcionado al atributo nombreUsuario
        this.nombreUsuario = nombreUsuario;
        // Asigna la contraseña proporcionada al atributo contrasena
        this.contrasena = contrasena;
    }

    // Método toString sobrescrito para proporcionar una representación en String del objeto
    @Override
    public String toString() {
        // Devuelve una cadena que muestra el nombre de usuario
        return "Usuario: " + nombreUsuario;
    }
}
