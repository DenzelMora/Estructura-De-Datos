package modelo;

import excepciones.HorarioConflictivoException;

// Aula maneja su disponibilidad horaria con una matriz booleana de 7 dias por 24 horas.
public class Aula {
    private String nombre;
    private int capacidad;
    // false = libre, true = reservado.
    private boolean[][] horario;

    public Aula(String nombre, int capacidad) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.horario = new boolean[7][24];
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public boolean[][] getHorario() {
        return horario;
    }

    public void setHorario(boolean[][] horario) {
        this.horario = horario;
    }

    public void reservar(int dia, int hora) throws HorarioConflictivoException {
        validarHorario(dia, hora);
        if (horario[dia][hora]) {
            throw new HorarioConflictivoException("El aula ya esta reservada en ese dia y hora.");
        }
        horario[dia][hora] = true;
    }

    public void liberar(int dia, int hora) {
        validarHorario(dia, hora);
        horario[dia][hora] = false;
    }

    public boolean estaDisponible(int dia, int hora) {
        // Retorna true cuando la casilla de la matriz esta libre.
        validarHorario(dia, hora);
        return !horario[dia][hora];
    }

    @Override
    public String toString() {
        return nombre + " | Capacidad: " + capacidad;
    }

    private void validarHorario(int dia, int hora) {
        // Protege la matriz para que solo se usen dias 0-6 y horas 0-23.
        if (dia < 0 || dia > 6 || hora < 0 || hora > 23) {
            throw new IllegalArgumentException("Dia invalido. Use 0 a 6. Hora invalida. Use 0 a 23.");
        }
    }
}
