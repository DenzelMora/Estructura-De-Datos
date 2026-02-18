package TrabajoIndependiente;

public class ejecutarCurso {

    public static void main(String[] args) {
        
        Curso[] c = new Curso[5];

        c[0] = new Curso(2029,"Estructura De Datos","Jhon Cano",51);
        c[1] = new Curso(2018,"Calculo Integral","Segundo Castro",31);
        c[2] = new Curso(2050,"Automatas y Lenguajes Formales","Andres Valencia",33);
        c[3] = new Curso(2032,"Estadistica y Probabilidad","Henry Pulgarin",65);
        c[4] = new Curso(2049,"Fisica, Electricidad y Magnetismo","Andres Valencia",18);

        int suma = 0;

        for (int i = 0;i < c.length;i++){

            suma += c[i].getEstudiantes();  
        }
        System.out.println("La suma de los estudiantes de todos los cursos es de: " + suma);
    }
    
}
