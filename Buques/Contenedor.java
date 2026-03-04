public class Contenedor {
    // Atributos necesarios para los reportes solicitados
    private double peso;
    private String origen;

    // Constructor para inicializar los datos del contenedor
    public Contenedor(double peso, String origen) {
        this.peso = peso;
        this.origen = origen;
    }

    // Métodos Getter para acceder a la información desde la clase Puerto
    public double getPeso() {
        return peso;
    }

    public String getOrigen() {
        return origen;
    }

    // Métodos Setter (opcionales para este proyecto, pero recomendados para integridad)
    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    @Override
    public String toString() {
        return "Contenedor [Origen=" + origen + ", Peso=" + peso + "kg]";
    }
}
