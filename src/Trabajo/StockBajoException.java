package Trabajo;

// Esta clase extiende la clase Exception para crear una excepción personalizada
public class StockBajoException extends Exception {
    
    // Constructor de la clase que recibe un mensaje de error como parámetro
    public StockBajoException(String message) {
        // Llama al constructor de la clase base Exception y pasa el mensaje
        super(message);
    }
}
