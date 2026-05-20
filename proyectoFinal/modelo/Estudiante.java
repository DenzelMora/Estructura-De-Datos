package modelo;

import java.util.LinkedList;

// Estudiante hereda de Persona y agrega datos academicos.
public class Estudiante extends Persona {
    private int semestre;

    // Matriz nativa: 10 semestres y hasta 20 materias por semestre.
    private Double[][] notas;

    // Matriz paralela a notas: permite saber a que materia corresponde cada nota.
    private Materia[][] materiasPorSemestre;

    // LinkedList guarda las materias aprobadas y funciona como historial academico.
    private LinkedList<Materia> historialAcademico;

    public Estudiante(String nombre, String id, String email, int semestre) {
        super(nombre, id, email);
        this.semestre = semestre;
        this.notas = new Double[10][20];
        this.materiasPorSemestre = new Materia[10][20];
        this.historialAcademico = new LinkedList<>();
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public Double[][] getNotas() {
        return notas;
    }

    public void setNotas(Double[][] notas) {
        this.notas = notas;
    }

    public Materia[][] getMateriasPorSemestre() {
        return materiasPorSemestre;
    }

    public void setMateriasPorSemestre(Materia[][] materiasPorSemestre) {
        this.materiasPorSemestre = materiasPorSemestre;
    }

    public LinkedList<Materia> getHistorialAcademico() {
        return historialAcademico;
    }

    public void setHistorialAcademico(LinkedList<Materia> historialAcademico) {
        this.historialAcademico = historialAcademico;
    }

    public void registrarNota(int semestre, int posicionMateria, Materia materia, double nota) {
        validarPosicionNota(semestre, posicionMateria);

        // Se resta 1 porque el usuario digita desde 1, pero los arreglos Java empiezan en 0.
        notas[semestre - 1][posicionMateria - 1] = nota;
        materiasPorSemestre[semestre - 1][posicionMateria - 1] = materia;
        reconstruirHistorialAcademico();
    }

    public void restaurarNota(int semestre, int posicionMateria, Materia materiaAnterior, Double notaAnterior) {
        // Se usa al deshacer una nota: recupera el valor y la materia que habia antes.
        validarPosicionNota(semestre, posicionMateria);
        notas[semestre - 1][posicionMateria - 1] = notaAnterior;
        materiasPorSemestre[semestre - 1][posicionMateria - 1] = materiaAnterior;
        reconstruirHistorialAcademico();
    }

    public boolean aproboMateria(String codigoMateria) {
        // Se usa para validar pre-requisitos antes de inscribir una materia.
        for (Materia materia : historialAcademico) {
            if (materia.getCodigo().equalsIgnoreCase(codigoMateria)) {
                return true;
            }
        }
        return false;
    }

    public double calcularPromedioSemestre(int semestre) {
        if (semestre < 1 || semestre > 10) {
            return 0.0;
        }
        // Recorre las 20 posibles materias de un semestre y promedia solo las notas registradas.
        double suma = 0.0;
        int cantidad = 0;
        for (Double nota : notas[semestre - 1]) {
            if (nota != null) {
                suma += nota;
                cantidad++;
            }
        }
        return cantidad == 0 ? 0.0 : suma / cantidad;
    }

    public double calcularPromedioAcumulado() {
        // Recorre toda la matriz de notas para calcular el promedio general del estudiante.
        double suma = 0.0;
        int cantidad = 0;
        for (int i = 0; i < notas.length; i++) {
            for (int j = 0; j < notas[i].length; j++) {
                if (notas[i][j] != null) {
                    suma += notas[i][j];
                    cantidad++;
                }
            }
        }
        return cantidad == 0 ? 0.0 : suma / cantidad;
    }

    public LinkedList<Materia> obtenerMateriasReprobadas() {
        // Devuelve las materias con nota menor a 3.0.
        LinkedList<Materia> reprobadas = new LinkedList<>();
        for (int i = 0; i < notas.length; i++) {
            for (int j = 0; j < notas[i].length; j++) {
                if (notas[i][j] != null && notas[i][j] < 3.0 && materiasPorSemestre[i][j] != null) {
                    reprobadas.add(materiasPorSemestre[i][j]);
                }
            }
        }
        return reprobadas;
    }

    @Override
    public String mostrarInformacion() {
        return "ID: " + getId()
                + " | Nombre: " + getNombre()
                + " | Email: " + getEmail()
                + " | Semestre: " + semestre
                + " | Promedio acumulado: " + String.format("%.2f", calcularPromedioAcumulado());
    }

    private void reconstruirHistorialAcademico() {
        // El historial se reconstruye con las materias aprobadas para evitar duplicados o datos viejos.
        historialAcademico.clear();
        for (int i = 0; i < notas.length; i++) {
            for (int j = 0; j < notas[i].length; j++) {
                Materia materia = materiasPorSemestre[i][j];
                Double nota = notas[i][j];
                if (materia != null && nota != null && nota >= 3.0 && !historialAcademico.contains(materia)) {
                    historialAcademico.add(materia);
                }
            }
        }
    }

    private void validarPosicionNota(int semestre, int posicionMateria) {
        // Controla que el usuario no intente acceder fuera de la matriz Double[10][20].
        if (semestre < 1 || semestre > 10 || posicionMateria < 1 || posicionMateria > 20) {
            throw new IllegalArgumentException("El semestre debe estar entre 1 y 10, y la materia entre 1 y 20.");
        }
    }
}
