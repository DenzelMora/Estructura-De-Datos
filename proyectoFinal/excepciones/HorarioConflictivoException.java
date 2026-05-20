package excepciones;

// Se lanza cuando se intenta reservar un aula en un horario ya ocupado.
public class HorarioConflictivoException extends Exception {
    public HorarioConflictivoException(String mensaje) {
        super(mensaje);
    }
}
