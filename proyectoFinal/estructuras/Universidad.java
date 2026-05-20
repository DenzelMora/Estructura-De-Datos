package estructuras;

import excepciones.CupoLlenoException;
import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PilaDeshacerVaciaException;
import excepciones.PreRequisitoNoAprobadoException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import modelo.Accion;
import modelo.Aula;
import modelo.Estudiante;
import modelo.Materia;

// Clase controladora: concentra las reglas de negocio del sistema universitario.
public class Universidad {
    // Valor grande usado para representar que no hay conexion directa entre edificios.
    private static final int INF = 999_999;

    // HashMap permite busqueda rapida por ID del estudiante.
    private HashMap<String, Estudiante> estudiantes;

    // HashMap permite busqueda rapida por codigo de materia.
    private HashMap<String, Materia> materias;

    // TreeMap mantiene las aulas ordenadas alfabeticamente por nombre.
    private TreeMap<String, Aula> aulas;

    // Stack almacena acciones para deshacer en orden LIFO.
    private Stack<Accion> pilaDeshacer;

    // Stack almacena acciones deshechas para poder rehacerlas.
    private Stack<Accion> pilaRehacer;

    // Stack de Strings para navegar reportes academicos ya generados.
    private Stack<String> pilaReportes;

    // Arreglo fijo de los 5 edificios del campus.
    private String[] edificios;

    // Matriz fija de distancias para el grafo no dirigido del campus.
    private int[][] matrizDistancias;

    public Universidad() {
        this.estudiantes = new HashMap<>();
        this.materias = new HashMap<>();
        this.aulas = new TreeMap<>();
        this.pilaDeshacer = new Stack<>();
        this.pilaRehacer = new Stack<>();
        this.pilaReportes = new Stack<>();
        inicializarAulasBase();
        // El campus queda listo desde que se crea la universidad.
        inicializarCampusFijo();
    }

    public HashMap<String, Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(HashMap<String, Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public HashMap<String, Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(HashMap<String, Materia> materias) {
        this.materias = materias;
    }

    public TreeMap<String, Aula> getAulas() {
        return aulas;
    }

    public void setAulas(TreeMap<String, Aula> aulas) {
        this.aulas = aulas;
    }

    public Stack<Accion> getPilaDeshacer() {
        return pilaDeshacer;
    }

    public void setPilaDeshacer(Stack<Accion> pilaDeshacer) {
        this.pilaDeshacer = pilaDeshacer;
    }

    public Stack<Accion> getPilaRehacer() {
        return pilaRehacer;
    }

    public void setPilaRehacer(Stack<Accion> pilaRehacer) {
        this.pilaRehacer = pilaRehacer;
    }

    public Stack<String> getPilaReportes() {
        return pilaReportes;
    }

    public void setPilaReportes(Stack<String> pilaReportes) {
        this.pilaReportes = pilaReportes;
    }

    public String[] getEdificios() {
        return edificios;
    }

    public void setEdificios(String[] edificios) {
        this.edificios = edificios;
    }

    public int[][] getMatrizDistancias() {
        return matrizDistancias;
    }

    public void setMatrizDistancias(int[][] matrizDistancias) {
        this.matrizDistancias = matrizDistancias;
    }

    public void registrarEstudiante(String nombre, String id, String email, int semestre) {
        // El ID es la clave del HashMap, por eso buscar luego es rapido.
        estudiantes.put(id, new Estudiante(nombre, id, email, semestre));
    }

    public Estudiante buscarEstudiante(String id) throws EstudianteNoEncontradoException {
        // Si el ID no existe, se lanza una excepcion personalizada.
        Estudiante estudiante = estudiantes.get(id);
        if (estudiante == null) {
            throw new EstudianteNoEncontradoException("No existe estudiante con ID: " + id);
        }
        return estudiante;
    }

    public String listarEstudiantes() {
        if (estudiantes.isEmpty()) {
            return "No hay estudiantes registrados.";
        }
        StringBuilder sb = new StringBuilder();
        for (Estudiante estudiante : estudiantes.values()) {
            sb.append(estudiante.mostrarInformacion()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public void eliminarEstudiante(String id) throws EstudianteNoEncontradoException {
        Estudiante eliminado = buscarEstudiante(id);
        estudiantes.remove(id);

        // Tambien se elimina de materias y colas para no dejar referencias inconsistentes.
        for (Materia materia : materias.values()) {
            materia.removerEstudiante(eliminado);
        }
        Accion accion = new Accion("ELIMINACION", id, null);
        accion.setEstudianteRespaldo(eliminado);
        // Se guarda respaldo para poder restaurarlo con deshacer.
        guardarAccion(accion);
    }

    public void crearMateria(String codigo, String nombre, int cupo, int creditos) {
        materias.put(codigo, new Materia(codigo, nombre, cupo, creditos));
    }

    public Materia buscarMateria(String codigo) {
        Materia materia = materias.get(codigo);
        if (materia == null) {
            throw new IllegalArgumentException("No existe materia con codigo: " + codigo);
        }
        return materia;
    }

    public void agregarPreRequisito(String codigoMateria, String codigoRequisito) {
        Materia materia = buscarMateria(codigoMateria);
        Materia requisito = buscarMateria(codigoRequisito);
        materia.agregarPreRequisito(requisito);
    }

    public String mostrarPreRequisitos(String codigoMateria) {
        return buscarMateria(codigoMateria).mostrarPreRequisitos();
    }

    public void inscribirEstudiante(String idEstudiante, String codigoMateria)
            throws EstudianteNoEncontradoException, PreRequisitoNoAprobadoException, CupoLlenoException {
        Estudiante estudiante = buscarEstudiante(idEstudiante);
        Materia materia = buscarMateria(codigoMateria);
        materia.inscribir(estudiante);

        // Si la inscripcion fue exitosa, se guarda la accion para deshacerla.
        guardarAccion(new Accion("INSCRIPCION", idEstudiante, codigoMateria));
    }

    public void cancelarInscripcion(String idEstudiante, String codigoMateria) throws EstudianteNoEncontradoException {
        Estudiante estudiante = buscarEstudiante(idEstudiante);
        Materia materia = buscarMateria(codigoMateria);
        materia.cancelarInscripcion(estudiante);
        // Se guarda la cancelacion para que deshacer pueda reinscribirlo.
        guardarAccion(new Accion("CANCELACION", idEstudiante, codigoMateria));
    }

    public String mostrarColaEspera(String codigoMateria) {
        return buscarMateria(codigoMateria).mostrarColaEspera();
    }

    public void crearAula(String nombre, int capacidad) {
        aulas.put(nombre, new Aula(nombre, capacidad));
    }

    public Aula buscarAula(String nombre) {
        Aula aula = aulas.get(nombre);
        if (aula == null) {
            throw new IllegalArgumentException("No existe aula con nombre: " + nombre);
        }
        return aula;
    }

    public void reservarHorario(String nombreAula, int dia, int hora) throws HorarioConflictivoException {
        Aula aula = buscarAula(nombreAula);
        aula.reservar(dia, hora);
        // Los datos del horario se guardan en un String simple: aula|dia|hora.
        guardarAccion(new Accion("RESERVA_HORARIO", null, nombreAula + "|" + dia + "|" + hora));
    }

    public void liberarHorario(String nombreAula, int dia, int hora) {
        Aula aula = buscarAula(nombreAula);
        aula.liberar(dia, hora);
        guardarAccion(new Accion("LIBERACION_HORARIO", null, nombreAula + "|" + dia + "|" + hora));
    }

    public boolean consultarDisponibilidad(String nombreAula, int dia, int hora) {
        return buscarAula(nombreAula).estaDisponible(dia, hora);
    }

    public void registrarNota(String idEstudiante, String codigoMateria, int semestre, int posicionMateria, double nota)
            throws EstudianteNoEncontradoException {
        Estudiante estudiante = buscarEstudiante(idEstudiante);
        Materia materia = buscarMateria(codigoMateria);
        Double notaAnterior = estudiante.getNotas()[semestre - 1][posicionMateria - 1];
        Materia materiaAnterior = estudiante.getMateriasPorSemestre()[semestre - 1][posicionMateria - 1];
        estudiante.registrarNota(semestre, posicionMateria, materia, nota);

        // Para deshacer una nota se guardan la nota anterior y la materia anterior.
        Accion accion = new Accion("NOTA", idEstudiante, codigoMateria);
        accion.setSemestre(semestre);
        accion.setPosicionMateria(posicionMateria);
        accion.setNotaAnterior(notaAnterior);
        accion.setNotaNueva(nota);
        accion.setMateriaAnterior(materiaAnterior);
        guardarAccion(accion);
    }

    public String generarReporteAcademico(String idEstudiante) throws EstudianteNoEncontradoException {
        Estudiante estudiante = buscarEstudiante(idEstudiante);
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE ACADEMICO ===").append(System.lineSeparator());
        sb.append(estudiante.mostrarInformacion()).append(System.lineSeparator());
        for (int semestre = 1; semestre <= 10; semestre++) {
            double promedio = estudiante.calcularPromedioSemestre(semestre);
            if (promedio > 0.0) {
                sb.append("Promedio semestre ").append(semestre).append(": ")
                        .append(String.format("%.2f", promedio)).append(System.lineSeparator());
            }
        }

        LinkedList<Materia> reprobadas = estudiante.obtenerMateriasReprobadas();
        sb.append("Materias reprobadas:").append(System.lineSeparator());
        if (reprobadas.isEmpty()) {
            sb.append("Ninguna");
        } else {
            for (Materia materia : reprobadas) {
                sb.append(materia).append(System.lineSeparator());
            }
        }

        String reporte = sb.toString();
        // Cada reporte generado se apila como texto para poder verlo despues.
        pilaReportes.push(reporte);
        return reporte;
    }

    public String navegarReporteAtras() {
        // pop recupera el ultimo reporte visto, siguiendo comportamiento de pila.
        if (pilaReportes.isEmpty()) {
            return "No hay reportes para navegar.";
        }
        return pilaReportes.pop();
    }

    public String navegarReporteAdelante() {
        return "La version simplificada usa una sola pila. Genere otro reporte para avanzar.";
    }

    public String verConexionesCampus() {
        // Muestra el grafo fijo: edificios y distancias guardadas en la matriz.
        StringBuilder sb = new StringBuilder();
        sb.append("Edificios del campus:").append(System.lineSeparator());
        for (int i = 0; i < edificios.length; i++) {
            sb.append(i).append(". ").append(edificios[i]).append(System.lineSeparator());
        }
        sb.append("Conexiones en metros:").append(System.lineSeparator());
        for (int i = 0; i < matrizDistancias.length; i++) {
            for (int j = i + 1; j < matrizDistancias[i].length; j++) {
                if (matrizDistancias[i][j] < INF) {
                    sb.append(edificios[i]).append(" <-> ").append(edificios[j])
                            .append(": ").append(matrizDistancias[i][j]).append(" m")
                            .append(System.lineSeparator());
                }
            }
        }
        return sb.toString();
    }

    public String calcularRutaMasCorta(String origen, String destino) {
        // Dijkstra calcula la ruta con menor suma de distancias entre origen y destino.
        int indiceOrigen = buscarIndiceEdificio(origen);
        int indiceDestino = buscarIndiceEdificio(destino);
        if (indiceOrigen == -1 || indiceDestino == -1) {
            throw new IllegalArgumentException("Edificio origen o destino no existe.");
        }

        int[] distancias = new int[5]; // Mejor distancia conocida desde el origen.
        int[] anteriores = new int[5]; // Permite reconstruir el camino final.
        boolean[] visitados = new boolean[5]; // Marca edificios ya procesados.

        for (int i = 0; i < 5; i++) {
            distancias[i] = INF;
            anteriores[i] = -1;
        }
        distancias[indiceOrigen] = 0;

        for (int paso = 0; paso < 5; paso++) {
            // Se escoge el edificio no visitado con menor distancia acumulada.
            int actual = buscarNoVisitadoMasCercano(distancias, visitados);
            if (actual == -1) {
                break;
            }
            visitados[actual] = true;

            for (int vecino = 0; vecino < 5; vecino++) {
                if (!visitados[vecino] && matrizDistancias[actual][vecino] < INF) {
                    // Relajacion: si encuentro un camino mas corto, actualizo distancia y anterior.
                    int distanciaNueva = distancias[actual] + matrizDistancias[actual][vecino];
                    if (distanciaNueva < distancias[vecino]) {
                        distancias[vecino] = distanciaNueva;
                        anteriores[vecino] = actual;
                    }
                }
            }
        }

        if (distancias[indiceDestino] == INF) {
            return "No existe ruta entre los edificios indicados.";
        }
        return construirRuta(anteriores, indiceDestino) + " | Distancia total: " + distancias[indiceDestino] + " m";
    }

    public String deshacer() throws PilaDeshacerVaciaException {
        if (pilaDeshacer.isEmpty()) {
            throw new PilaDeshacerVaciaException("No hay operaciones para deshacer.");
        }
        // Se toma la ultima accion realizada y se ejecuta su operacion inversa.
        Accion accion = pilaDeshacer.pop();
        aplicarInversa(accion);
        pilaRehacer.push(accion);
        return "Operacion deshecha: " + accion.getTipoAccion();
    }

    public String rehacer() throws PilaDeshacerVaciaException {
        if (pilaRehacer.isEmpty()) {
            throw new PilaDeshacerVaciaException("No hay operaciones para rehacer.");
        }
        // Se toma la ultima accion deshecha y se ejecuta otra vez.
        Accion accion = pilaRehacer.pop();
        aplicarOriginal(accion);
        pilaDeshacer.push(accion);
        return "Operacion rehecha: " + accion.getTipoAccion();
    }

    private void guardarAccion(Accion accion) {
        // Una nueva accion invalida el historial de rehacer, como en editores de texto.
        pilaDeshacer.push(accion);
        pilaRehacer.clear();
    }

    private void aplicarInversa(Accion accion) {
        // Logica inversa: cada tipo de accion se deshace con la operacion contraria.
        switch (accion.getTipoAccion()) {
            case "INSCRIPCION":
                buscarMateria(accion.getCodigoMateria()).removerEstudiante(estudiantes.get(accion.getIdEstudiante()));
                break;
            case "CANCELACION":
                buscarMateria(accion.getCodigoMateria()).inscribirSinValidarCupo(estudiantes.get(accion.getIdEstudiante()));
                break;
            case "ELIMINACION":
                estudiantes.put(accion.getIdEstudiante(), accion.getEstudianteRespaldo());
                break;
            case "NOTA":
                Estudiante estudiante = estudiantes.get(accion.getIdEstudiante());
                estudiante.restaurarNota(
                        accion.getSemestre(),
                        accion.getPosicionMateria(),
                        accion.getMateriaAnterior(),
                        accion.getNotaAnterior());
                break;
            case "RESERVA_HORARIO":
                aplicarHorario(accion.getCodigoMateria(), false);
                break;
            case "LIBERACION_HORARIO":
                aplicarHorario(accion.getCodigoMateria(), true);
                break;
            default:
                throw new IllegalArgumentException("Tipo de accion desconocido: " + accion.getTipoAccion());
        }
    }

    private void aplicarOriginal(Accion accion) {
        // Logica original: repite la accion que habia sido deshecha.
        switch (accion.getTipoAccion()) {
            case "INSCRIPCION":
                buscarMateria(accion.getCodigoMateria()).inscribirSinValidarCupo(estudiantes.get(accion.getIdEstudiante()));
                break;
            case "CANCELACION":
                buscarMateria(accion.getCodigoMateria()).removerEstudiante(estudiantes.get(accion.getIdEstudiante()));
                break;
            case "ELIMINACION":
                estudiantes.remove(accion.getIdEstudiante());
                break;
            case "NOTA":
                Estudiante estudiante = estudiantes.get(accion.getIdEstudiante());
                estudiante.registrarNota(
                        accion.getSemestre(),
                        accion.getPosicionMateria(),
                        buscarMateria(accion.getCodigoMateria()),
                        accion.getNotaNueva());
                break;
            case "RESERVA_HORARIO":
                aplicarHorario(accion.getCodigoMateria(), true);
                break;
            case "LIBERACION_HORARIO":
                aplicarHorario(accion.getCodigoMateria(), false);
                break;
            default:
                throw new IllegalArgumentException("Tipo de accion desconocido: " + accion.getTipoAccion());
        }
    }

    private void aplicarHorario(String datosHorario, boolean reservado) {
        // Convierte el String aula|dia|hora de nuevo en datos usables.
        String[] partes = datosHorario.split("\\|");
        Aula aula = buscarAula(partes[0]);
        int dia = Integer.parseInt(partes[1]);
        int hora = Integer.parseInt(partes[2]);
        if (reservado) {
            aula.getHorario()[dia][hora] = true;
        } else {
            aula.getHorario()[dia][hora] = false;
        }
    }

    private void inicializarAulasBase() {
        // Aulas iniciales para probar gestion de horarios sin tener que crearlas por menu.
        crearAula("Aula 101", 35);
        crearAula("Laboratorio 1", 25);
        crearAula("Auditorio Principal", 120);
    }

    private void inicializarCampusFijo() {
        // Grafo fijo de 5 edificios, como pide la version simplificada del proyecto.
        edificios = new String[] { "Ingenieria", "Biblioteca", "Cafeteria", "Rectoria", "Laboratorios" };
        matrizDistancias = new int[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrizDistancias[i][j] = i == j ? 0 : INF;
            }
        }
        conectar(0, 1, 120);
        conectar(0, 4, 90);
        conectar(1, 2, 80);
        conectar(1, 3, 200);
        conectar(2, 3, 140);
        conectar(2, 4, 110);
        conectar(3, 4, 230);
    }

    private void conectar(int origen, int destino, int distancia) {
        // Grafo no dirigido: la distancia se guarda en ambos sentidos.
        matrizDistancias[origen][destino] = distancia;
        matrizDistancias[destino][origen] = distancia;
    }

    private int buscarIndiceEdificio(String nombre) {
        for (int i = 0; i < edificios.length; i++) {
            if (edificios[i].equalsIgnoreCase(nombre)) {
                return i;
            }
        }
        return -1;
    }

    private int buscarNoVisitadoMasCercano(int[] distancias, boolean[] visitados) {
        int menor = INF;
        int indice = -1;
        for (int i = 0; i < distancias.length; i++) {
            if (!visitados[i] && distancias[i] < menor) {
                menor = distancias[i];
                indice = i;
            }
        }
        return indice;
    }

    private String construirRuta(int[] anteriores, int destino) {
        // La pila invierte el camino: primero se llega al destino y luego se imprime desde el origen.
        Stack<String> ruta = new Stack<>();
        int actual = destino;
        while (actual != -1) {
            ruta.push(edificios[actual]);
            actual = anteriores[actual];
        }

        StringBuilder sb = new StringBuilder("Ruta: ");
        while (!ruta.isEmpty()) {
            sb.append(ruta.pop());
            if (!ruta.isEmpty()) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }

    public String listarAulas() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Aula> entrada : aulas.entrySet()) {
            sb.append(entrada.getValue()).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
