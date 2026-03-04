
public class Curso {//Prueba numero dos patra saber si funciona el commit

    private int id;//helou
    private String nCurso;
    private String profesor;
    private int estudiantes;
    
    public Curso(int id, String nCurso, String profesor, int estudiantes) {
        this.id = id;
        this.nCurso = nCurso;
        this.profesor = profesor;
        this.estudiantes = estudiantes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getnCurso() {
        return nCurso;
    }

    public void setnCurso(String nCurso) {
        this.nCurso = nCurso;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public int getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(int estudiantes) {
        this.estudiantes = estudiantes;
    }

    @Override
    public String toString() {
        return "Clase [id=" + id + ", nCurso=" + nCurso + ", profesor=" + profesor + ", estudiantes=" + estudiantes
                + "]";
    }

    
}