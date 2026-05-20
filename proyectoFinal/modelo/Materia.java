package modelo;

import excepciones.CupoLlenoException;
import excepciones.PreRequisitoNoAprobadoException;
import java.util.LinkedList;
import java.util.Queue;

// Materia guarda cupos, pre-requisitos, inscritos y cola de espera.
public class Materia {
    private String codigo;
    private String nombre;
    private int cupo;
    private int creditos;

    // LinkedList almacena pre-requisitos en orden de insercion.
    private LinkedList<Materia> preRequisitos;

    // LinkedList almacena los estudiantes que lograron inscribirse.
    private LinkedList<Estudiante> estudiantesInscritos;

    // Queue aplica la regla FIFO: el primero en entrar a espera es el primero en salir.
    private Queue<Estudiante> colaEspera;

    public Materia(String codigo, String nombre, int cupo, int creditos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cupo = cupo;
        this.creditos = creditos;
        this.preRequisitos = new LinkedList<>();
        this.estudiantesInscritos = new LinkedList<>();
        this.colaEspera = new LinkedList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public LinkedList<Materia> getPreRequisitos() {
        return preRequisitos;
    }

    public void setPreRequisitos(LinkedList<Materia> preRequisitos) {
        this.preRequisitos = preRequisitos;
    }

    public LinkedList<Estudiante> getEstudiantesInscritos() {
        return estudiantesInscritos;
    }

    public void setEstudiantesInscritos(LinkedList<Estudiante> estudiantesInscritos) {
        this.estudiantesInscritos = estudiantesInscritos;
    }

    public Queue<Estudiante> getColaEspera() {
        return colaEspera;
    }

    public void setColaEspera(Queue<Estudiante> colaEspera) {
        this.colaEspera = colaEspera;
    }

    public void agregarPreRequisito(Materia materia) {
        // Evita agregar dos veces el mismo pre-requisito.
        if (!preRequisitos.contains(materia)) {
            preRequisitos.add(materia);
        }
    }

    public void inscribir(Estudiante estudiante) throws PreRequisitoNoAprobadoException, CupoLlenoException {
        // Primero valida reglas academicas; luego valida cupo.
        validarPreRequisitos(estudiante);
        if (estudiantesInscritos.contains(estudiante)) {
            return;
        }
        if (estudiantesInscritos.size() >= cupo) {
            // Si no hay cupo, el estudiante entra a la cola de espera.
            if (!colaEspera.contains(estudiante)) {
                colaEspera.offer(estudiante);
            }
            throw new CupoLlenoException("La materia esta llena. El estudiante fue agregado a la cola de espera.");
        }
        estudiantesInscritos.add(estudiante);
    }

    public Estudiante cancelarInscripcion(Estudiante estudiante) {
        // Si se libera un cupo, se promueve automaticamente al primero de la cola.
        if (!estudiantesInscritos.remove(estudiante)) {
            return null;
        }
        Estudiante promovido = colaEspera.poll();
        if (promovido != null) {
            estudiantesInscritos.add(promovido);
        }
        return promovido;
    }

    public void inscribirSinValidarCupo(Estudiante estudiante) {
        if (!estudiantesInscritos.contains(estudiante)) {
            estudiantesInscritos.add(estudiante);
        }
    }

    public void removerEstudiante(Estudiante estudiante) {
        estudiantesInscritos.remove(estudiante);
        colaEspera.remove(estudiante);
    }

    public String mostrarPreRequisitos() {
        if (preRequisitos.isEmpty()) {
            return "La materia no tiene pre-requisitos.";
        }
        StringBuilder sb = new StringBuilder();
        for (Materia materia : preRequisitos) {
            sb.append(materia).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public String mostrarColaEspera() {
        if (colaEspera.isEmpty()) {
            return "La cola de espera esta vacia.";
        }
        StringBuilder sb = new StringBuilder();
        for (Estudiante estudiante : colaEspera) {
            sb.append(estudiante.getId()).append(" - ").append(estudiante.getNombre()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " | Creditos: " + creditos + " | Cupo: " + cupo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Materia)) {
            return false;
        }
        Materia otra = (Materia) obj;
        return codigo.equalsIgnoreCase(otra.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.toLowerCase().hashCode();
    }

    private void validarPreRequisitos(Estudiante estudiante) throws PreRequisitoNoAprobadoException {
        // Cada pre-requisito debe estar aprobado en el historial academico del estudiante.
        for (Materia requisito : preRequisitos) {
            if (!estudiante.aproboMateria(requisito.getCodigo())) {
                throw new PreRequisitoNoAprobadoException("Falta aprobar el pre-requisito: " + requisito.getNombre());
            }
        }
    }
}
