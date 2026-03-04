public class Buque {
    // Atributo para identificar el barco en el muelle
    private String nombre;

    // Constructor solicitado implícitamente por la lógica de registro
    public Buque(String nombre) {
        this.nombre = nombre;
    }

    // Métodos Getter y Setter para la administración de la información
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Buque: " + nombre;
    }
}