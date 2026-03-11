import java.util.Random;

public class Puerto {

    Contenedor[][] patioContenedores;
    Buque[] muelleBuques;
    
    
    public Puerto(int filas, int columnas, int minPeso, int maxPeso) {
        patioContenedores = new Contenedor[filas][columnas];
        muelleBuques = new Buque[10]; 
        
        //aqui lo que hacemos es generar contenedores con un pais y peso asignado aleatoriamente siguiendo los valores minimos que les dismos en el constructor.
        Random r = new Random();
        String[] paises = {"Colombia", "China", "EEUU", "Brasil", "España"};
        for (int i = 7; i < filas; i++) { 
            for (int j = 0; j < columnas; j++) {
                if (r.nextBoolean()) {
                    double pesoAleatorio = minPeso + (maxPeso - minPeso) * r.nextDouble();
                    String origenAleatorio = paises[r.nextInt(paises.length)];
                    
                    patioContenedores[i][j] = new Contenedor(pesoAleatorio, origenAleatorio);
                }
            }
        }
    }

    public void mostrarEsquema() {
        System.out.println("\n=== ESTADO ACTUAL DEL PATIO DE CONTENEDORES ===");
        System.out.print("      ");
        for (int c = 0; c < 10; c++) System.out.print(c + "   ");
        System.out.println("\n    -----------------------------------------");

        //Este for lo que hace es recorrer la matriz donde se guardan los contenedores y imprimiendolo en forma de matriz para mostrar el esquema.
        for (int i = 0; i < 10; i++) {
            System.out.print("F" + i + " | ");
            for (int j = 0; j < 10; j++) {
                if (patioContenedores[i][j] == null) {
                    System.out.print("[ ] "); 
                } else {
                    System.out.print("[X] ");
                }
            }
            System.out.println();
        }
        System.out.println("    -----------------------------------------");
    }

    public boolean agregarContenedor(int columna, Contenedor nuevoContenedor) {
        boolean asignado = false;
        
        //esto para evitar que el usuario asigne un contenedor en una columna no existente y que esto pueda causar un error en el programa
        if (columna < 0 || columna > 9) {
            System.out.println("Error: Columna Invalida");
            return false;
        }

        //Esto lo que hace es un bucle que inicie desde la ultima posicion de la matriz para poder agregar el contenedor, y que este tenga alguna clase de "gravedad" aunque esto funcion a medias porque al ejecutar main y asignar contenedores aleatorios esto no se muestra en la matriz correctamente desde abajo
        for (int fila = 9; fila >= 0; fila--) {
            if (patioContenedores[fila][columna] == null) {
                patioContenedores[fila][columna] = nuevoContenedor;
                System.out.println("Contenedor agregado con exito en fila: " + fila + ", Columna: " + columna);
                asignado = true;
                break;
            }
        }

        if (!asignado) {
            System.out.println("Advertencia: La Columna " + columna + " esta llena");
        }
        return asignado;
    }

    public double calcularPesoTotal() {
        double sumaPesos = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (patioContenedores[i][j] != null) {
                    sumaPesos += patioContenedores[i][j].getPeso();
                }
            }
        }
        return sumaPesos;
    }

    public void listarPorOrigen() {
        System.out.println("\n--- LISTADO DE CONTENEDORES POR ORIGEN ---");
        String[] origenesProcesados = new String[100]; 
        int contadorProcesados = 0;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Contenedor actual = patioContenedores[i][j];
                if (actual != null) {
                    String origenActual = actual.getOrigen();
                    boolean yaProcesado = false;
                    for (int k = 0; k < contadorProcesados; k++) {
                        if (origenesProcesados[k].equalsIgnoreCase(origenActual)) {
                            yaProcesado = true;
                            break;
                        }
                    }

                    if (!yaProcesado) {
                        System.out.println("\nORIGEN: " + origenActual.toUpperCase());
                        for (int r = 0; r < 10; r++) {
                            for (int c = 0; c < 10; c++) {
                                Contenedor temp = patioContenedores[r][c];
                                if (temp != null && temp.getOrigen().equalsIgnoreCase(origenActual)) {
                                    System.out.println("  -> Contenedor en [F" + r + "][C" + c + "] - Peso: " + temp.getPeso() + "kg");
                                }
                            }
                        }
                        origenesProcesados[contadorProcesados] = origenActual;
                        contadorProcesados++;
                    }
                }
            }
        }
        if (contadorProcesados == 0) System.out.println("No hay contenedores registrados.");
    }
}