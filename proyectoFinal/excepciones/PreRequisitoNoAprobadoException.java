package excepciones;

// Se lanza cuando un estudiante intenta inscribir una materia sin aprobar sus pre-requisitos.
public class PreRequisitoNoAprobadoException extends Exception {
    public PreRequisitoNoAprobadoException(String mensaje) {
        super(mensaje);
    }
}
