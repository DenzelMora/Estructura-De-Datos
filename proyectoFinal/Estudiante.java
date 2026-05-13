package proyectoFinal;

import java.util.Arrays;

class Estudiante extends Persona{
    
    private int semestre;
    private double notas[][] = new double[10][20];

    public Estudiante(String nombre, int id, String correo, int semestre, double[][] notas) {
        super(nombre, id, correo);
        this.semestre = semestre;
        this.notas = notas;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public double[][] getNotas() {
        return notas;
    }

    public void setNotas(double[][] notas) {
        this.notas = notas;
    }

    @Override
    public String toString() {
        return "Estudiante [semestre=" + semestre + ", notas=" + Arrays.toString(notas) + "]";
    }

    

    
}
