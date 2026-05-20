package modelo;

// Accion guarda la informacion minima necesaria para deshacer o rehacer una operacion.
public class Accion {
    // Ejemplos: INSCRIPCION, CANCELACION, ELIMINACION, NOTA, RESERVA_HORARIO.
    private String tipoAccion;
    private String idEstudiante;
    private String codigoMateria;

    // Datos extra usados solo cuando la accion necesita restaurar informacion anterior.
    private Estudiante estudianteRespaldo;
    private Double notaAnterior;
    private Double notaNueva;
    private Materia materiaAnterior;
    private int semestre;
    private int posicionMateria;

    public Accion(String tipoAccion, String idEstudiante, String codigoMateria) {
        this.tipoAccion = tipoAccion;
        this.idEstudiante = idEstudiante;
        this.codigoMateria = codigoMateria;
    }

    public String getTipoAccion() {
        return tipoAccion;
    }

    public void setTipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
    }

    public String getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(String idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getCodigoMateria() {
        return codigoMateria;
    }

    public void setCodigoMateria(String codigoMateria) {
        this.codigoMateria = codigoMateria;
    }

    public Estudiante getEstudianteRespaldo() {
        return estudianteRespaldo;
    }

    public void setEstudianteRespaldo(Estudiante estudianteRespaldo) {
        this.estudianteRespaldo = estudianteRespaldo;
    }

    public Double getNotaAnterior() {
        return notaAnterior;
    }

    public void setNotaAnterior(Double notaAnterior) {
        this.notaAnterior = notaAnterior;
    }

    public Double getNotaNueva() {
        return notaNueva;
    }

    public void setNotaNueva(Double notaNueva) {
        this.notaNueva = notaNueva;
    }

    public Materia getMateriaAnterior() {
        return materiaAnterior;
    }

    public void setMateriaAnterior(Materia materiaAnterior) {
        this.materiaAnterior = materiaAnterior;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public int getPosicionMateria() {
        return posicionMateria;
    }

    public void setPosicionMateria(int posicionMateria) {
        this.posicionMateria = posicionMateria;
    }
}
