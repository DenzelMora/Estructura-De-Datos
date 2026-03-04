import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Puerto miPuerto = new Puerto(10, 10, 100, 500);
        Scanner teclado = new Scanner(System.in);
        int opcion = 0;

        do {
            System.out.println("\n--- LOGÍSTICA DISTRIBUCIONES JH ---");
            System.out.println("1. Registrar Buque en muelle");
            System.out.println("2. Registrar Contenedor en patio");
            System.out.println("3. Mostrar peso total de contenedores");
            System.out.println("4. Listar contenedores agrupados por origen");
            System.out.println("5. Salir de la aplicación");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(teclado.nextLine());

                switch (opcion) {
                    case 1:
                   
                        System.out.print("Ingrese el nombre del buque: ");
                        String nombreB = teclado.nextLine();
                        
                        boolean buqueAsignado = false;
                        for (int i = 0; i < miPuerto.muelleBuques.length; i++) {
                            if (miPuerto.muelleBuques[i] == null) {
                                miPuerto.muelleBuques[i] = new Buque(nombreB);
                                System.out.println("Buque registrado en la posición " + i);
                                buqueAsignado = true;
                                break;
                            }
                        }
                        if (!buqueAsignado) System.out.println("Muelle lleno. No hay puestos para buques ");
                        break;

                    case 2:
                       
                        miPuerto.mostrarEsquema(); 
                        System.out.print("Ingrese el origen del contenedor: ");
                        String origen = teclado.nextLine();
                        System.out.print("Ingrese el peso (kg): ");
                        double peso = Double.parseDouble(teclado.nextLine());
                        System.out.print("Elija la columna (0-9) para apilar: ");
                        int col = Integer.parseInt(teclado.nextLine());

                        Contenedor nuevo = new Contenedor(peso, origen);
                        miPuerto.agregarContenedor(col, nuevo); 
                        break;

                    case 3:
                        
                        double total = miPuerto.calcularPesoTotal();
                        System.out.printf("El peso total en el patio es: %.2f kg\n", total);
                        break;

                    case 4:
                       
                        miPuerto.listarPorOrigen();
                        break;

                    case 5:
                       
                        System.out.println("Saliendo del sistema... ¡Buen día!");
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Error: Ingrese datos válidos para evitar cierres inesperados ");
            }

        } while (opcion != 5);

        teclado.close();
    }
}