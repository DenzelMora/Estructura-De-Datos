package modelo;

// Clase base abstracta: contiene los datos comunes de Estudiante y Profesor.
public abstract class Persona {
    // Atributos privados para cumplir encapsulamiento.
    private String nombre;
    private String id;
    private String email;

    public Persona(String nombre, String id, String email) {
        this.nombre = nombre;
        this.id = id;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Cada subclase define su propia manera de mostrar la informacion.
    public abstract String mostrarInformacion();
}
