import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Inicialización del puerto con matriz 10x10 según requerimiento [cite: 22]
        // Se definen rangos de peso aleatorio (ej. 100 a 500kg) para el constructor
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
                        // Acceso al menú de registro de buques [cite: 33]
                        System.out.print("Ingrese el nombre del buque: ");
                        String nombreB = teclado.nextLine();
                        // Lógica simple para ocupar el arreglo de 10 posiciones [cite: 23]
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
                        // Acceso al menú de registro de contenedores [cite: 34]
                        miPuerto.mostrarEsquema(); // Muestra esquema antes de ingresar [cite: 43]
                        System.out.print("Ingrese el origen del contenedor: ");
                        String origen = teclado.nextLine();
                        System.out.print("Ingrese el peso (kg): ");
                        double peso = Double.parseDouble(teclado.nextLine());
                        System.out.print("Elija la columna (0-9) para apilar: ");
                        int col = Integer.parseInt(teclado.nextLine());

                        Contenedor nuevo = new Contenedor(peso, origen);
                        miPuerto.agregarContenedor(col, nuevo); // Lógica de apilamiento [cite: 24, 42]
                        break;

                    case 3:
                        // Mostrar el peso total de los contenedores [cite: 35]
                        double total = miPuerto.calcularPesoTotal();
                        System.out.printf("El peso total en el patio es: %.2f kg\n", total);
                        break;

                    case 4:
                        // Listar de manera agrupada el origen [cite: 36]
                        miPuerto.listarPorOrigen();
                        break;

                    case 5:
                        // Cierre de la aplicación [cite: 37, 46]
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