package excepciones;

// Se lanza cuando se intenta deshacer o rehacer y la pila correspondiente esta vacia.
public class PilaDeshacerVaciaException extends Exception {
    public PilaDeshacerVaciaException(String mensaje) {
        super(mensaje);
    }
}
