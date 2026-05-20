import estructuras.Universidad;
import excepciones.CupoLlenoException;
import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PilaDeshacerVaciaException;
import excepciones.PreRequisitoNoAprobadoException;
import java.util.Scanner;
import modelo.Estudiante;

public class Main {
    // Scanner lee los datos que el usuario escribe en consola.
    private static final Scanner scanner = new Scanner(System.in);

    // Universidad es logica del sistema ya que main solo maneja el menu.
    private static final Universidad universidad = new Universidad();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");
            procesarOpcion(opcion);
        } while (opcion != 21);
        System.out.println("Sistema finalizado.");
    }

    private static void mostrarMenu() {
        // Menu principal solicitado en el proyecto. Cada numero llama una funcionalidad.
        System.out.println();
        System.out.println("=== GESTION DE ESTUDIANTES ===");
        System.out.println("1. Registrar estudiante");
        System.out.println("2. Buscar estudiante por ID");
        System.out.println("3. Listar todos los estudiantes");
        System.out.println("4. Eliminar estudiante");
        System.out.println("=== GESTION DE MATERIAS ===");
        System.out.println("5. Crear materia");
        System.out.println("6. Agregar pre-requisito");
        System.out.println("7. Mostrar pre-requisitos");
        System.out.println("8. Inscribir estudiante");
        System.out.println("9. Cancelar inscripcion");
        System.out.println("10. Mostrar cola de espera");
        System.out.println("=== GESTION DE HORARIOS ===");
        System.out.println("11. Reservar horario en aula");
        System.out.println("12. Liberar horario");
        System.out.println("13. Consultar disponibilidad");
        System.out.println("=== RUTAS ENTRE EDIFICIOS ===");
        System.out.println("14. Inicializar/Ver conexiones del campus");
        System.out.println("15. Calcular ruta mas corta (Dijkstra)");
        System.out.println("=== REPORTES ACADEMICOS ===");
        System.out.println("16. Registrar nota");
        System.out.println("17. Ver reporte academico");
        System.out.println("18. Navegador de reportes (atras/adelante)");
        System.out.println("=== SISTEMA DESHACER/REHACER ===");
        System.out.println("19. Deshacer ultima operacion");
        System.out.println("20. Rehacer ultima operacion");
        System.out.println("=== SALIR ===");
        System.out.println("21. Salir");
    }

    private static void procesarOpcion(int opcion) {
        try {
            // switch-case decide que metodo ejecutar segun la opcion escogida.
            switch (opcion) {
                case 1:
                    registrarEstudiante();
                    break;
                case 2:
                    buscarEstudiante();
                    break;
                case 3:
                    System.out.println(universidad.listarEstudiantes());
                    break;
                case 4:
                    eliminarEstudiante();
                    break;
                case 5:
                    crearMateria();
                    break;
                case 6:
                    agregarPreRequisito();
                    break;
                case 7:
                    mostrarPreRequisitos();
                    break;
                case 8:
                    inscribirEstudiante();
                    break;
                case 9:
                    cancelarInscripcion();
                    break;
                case 10:
                    mostrarColaEspera();
                    break;
                case 11:
                    reservarHorario();
                    break;
                case 12:
                    liberarHorario();
                    break;
                case 13:
                    consultarDisponibilidad();
                    break;
                case 14:
                    System.out.println(universidad.verConexionesCampus());
                    break;
                case 15:
                    calcularRutaMasCorta();
                    break;
                case 16:
                    registrarNota();
                    break;
                case 17:
                    verReporteAcademico();
                    break;
                case 18:
                    navegarReportes();
                    break;
                case 19:
                    System.out.println(universidad.deshacer());
                    break;
                case 20:
                    System.out.println(universidad.rehacer());
                    break;
                case 21:
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } catch (EstudianteNoEncontradoException
                | PreRequisitoNoAprobadoException
                | CupoLlenoException
                | HorarioConflictivoException
                | PilaDeshacerVaciaException e) {
            //Cupo lleno, horario ocupado, etc.
            System.out.println("Error controlado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Errores por datos invalidos colocados por el usuario.
            System.out.println("Dato invalido: " + e.getMessage());
        } catch (Exception e) {
            // Ultima proteccion para que el programa no se cierre inesperadamente.
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    private static void registrarEstudiante() {
        String nombre = leerTexto("Nombre: ");
        String id = leerTexto("ID: ");
        String email = leerTexto("Email: ");
        int semestre = leerEntero("Semestre: ");
        universidad.registrarEstudiante(nombre, id, email, semestre);
        System.out.println("Estudiante registrado correctamente.");
    }

    private static void buscarEstudiante() throws EstudianteNoEncontradoException {
        String id = leerTexto("ID del estudiante: ");
        Estudiante estudiante = universidad.buscarEstudiante(id);
        System.out.println(estudiante.mostrarInformacion());
    }

    private static void eliminarEstudiante() throws EstudianteNoEncontradoException {
        String id = leerTexto("ID del estudiante: ");
        universidad.eliminarEstudiante(id);
        System.out.println("Estudiante eliminado. Puede deshacer esta operacion con la opcion 19.");
    }

    private static void crearMateria() {
        String codigo = leerTexto("Codigo: ");
        String nombre = leerTexto("Nombre: ");
        int cupo = leerEntero("Cupo: ");
        int creditos = leerEntero("Creditos: ");
        universidad.crearMateria(codigo, nombre, cupo, creditos);
        System.out.println("Materia creada correctamente.");
    }

    private static void agregarPreRequisito() {
        String codigoMateria = leerTexto("Codigo de la materia: ");
        String codigoRequisito = leerTexto("Codigo del pre-requisito: ");
        universidad.agregarPreRequisito(codigoMateria, codigoRequisito);
        System.out.println("Pre-requisito agregado correctamente.");
    }

    private static void mostrarPreRequisitos() {
        String codigoMateria = leerTexto("Codigo de la materia: ");
        System.out.println(universidad.mostrarPreRequisitos(codigoMateria));
    }

    private static void inscribirEstudiante()
            throws EstudianteNoEncontradoException, PreRequisitoNoAprobadoException, CupoLlenoException {
        String id = leerTexto("ID del estudiante: ");
        String codigoMateria = leerTexto("Codigo de materia: ");
        universidad.inscribirEstudiante(id, codigoMateria);
        System.out.println("Inscripcion realizada correctamente.");
    }

    private static void cancelarInscripcion() throws EstudianteNoEncontradoException {
        String id = leerTexto("ID del estudiante: ");
        String codigoMateria = leerTexto("Codigo de materia: ");
        universidad.cancelarInscripcion(id, codigoMateria);
        System.out.println("Inscripcion cancelada. Si habia cola de espera, se promovio al primero.");
    }

    private static void mostrarColaEspera() {
        String codigoMateria = leerTexto("Codigo de materia: ");
        System.out.println(universidad.mostrarColaEspera(codigoMateria));
    }

    private static void reservarHorario() throws HorarioConflictivoException {
        System.out.println("Aulas disponibles:");
        System.out.println(universidad.listarAulas());
        String aula = leerTexto("Nombre del aula: ");
        int dia = leerEntero("Dia (0=Domingo, 6=Sabado): ");
        int hora = leerEntero("Hora (0-23): ");
        universidad.reservarHorario(aula, dia, hora);
        System.out.println("Horario reservado correctamente.");
    }

    private static void liberarHorario() {
        String aula = leerTexto("Nombre del aula: ");
        int dia = leerEntero("Dia (0=Domingo, 6=Sabado): ");
        int hora = leerEntero("Hora (0-23): ");
        universidad.liberarHorario(aula, dia, hora);
        System.out.println("Horario liberado correctamente.");
    }

    private static void consultarDisponibilidad() {
        String aula = leerTexto("Nombre del aula: ");
        int dia = leerEntero("Dia (0=Domingo, 6=Sabado): ");
        int hora = leerEntero("Hora (0-23): ");
        boolean disponible = universidad.consultarDisponibilidad(aula, dia, hora);
        System.out.println(disponible ? "El aula esta disponible." : "El aula esta ocupada.");
    }

    private static void calcularRutaMasCorta() {
        System.out.println(universidad.verConexionesCampus());
        String origen = leerTexto("Edificio origen: ");
        String destino = leerTexto("Edificio destino: ");
        System.out.println(universidad.calcularRutaMasCorta(origen, destino));
    }

    private static void registrarNota() throws EstudianteNoEncontradoException {
        String id = leerTexto("ID del estudiante: ");
        String codigoMateria = leerTexto("Codigo de materia: ");
        int semestre = leerEntero("Semestre (1-10): ");
        int posicion = leerEntero("Posicion de la materia en el semestre (1-20): ");
        double nota = leerDouble("Nota: ");
        universidad.registrarNota(id, codigoMateria, semestre, posicion, nota);
        System.out.println("Nota registrada correctamente.");
    }

    private static void verReporteAcademico() throws EstudianteNoEncontradoException {
        String id = leerTexto("ID del estudiante: ");
        System.out.println(universidad.generarReporteAcademico(id));
    }

    private static void navegarReportes() {
        String movimiento = leerTexto("Escriba 'atras' o 'adelante': ");
        if ("atras".equalsIgnoreCase(movimiento)) {
            System.out.println(universidad.navegarReporteAtras());
        } else if ("adelante".equalsIgnoreCase(movimiento)) {
            System.out.println(universidad.navegarReporteAdelante());
        } else {
            System.out.println("Movimiento invalido.");
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                // Repite la lectura hasta que el usuario escriba un numero valido.
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                // Repite la lectura hasta que el usuario escriba un decimal valido.
                System.out.println("Ingrese un numero decimal valido.");
            }
        }
    }
}
