package Trabajo;

public class Usuario {
    private String nombreUsuario;
    private String contrasena;

    public Usuario(String nombreUsuario, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return "Usuario: " + nombreUsuario;
    }
}
