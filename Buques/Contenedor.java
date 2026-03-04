public class Contenedor {
   
    private double peso;
    private String origen;

    
    public Contenedor(double peso, String origen) {
        this.peso = peso;
        this.origen = origen;
    }

   
    public double getPeso() {
        return peso;
    }

    public String getOrigen() {
        return origen;
    }

  
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
