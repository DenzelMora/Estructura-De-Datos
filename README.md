# Estructura-De-Datos
SISTEMA UNIVERSITARIO para gestionar estudiantes, materias, horarios,rutas de campus, reportes academicos y operaciones de deshacer/rehacer


Se construyo por paquetes como lo decia el pdf: modelo para las identidades, estructuras para la logica central y excepciones para errores.

De que cosas hago uso y porque

HashMap<String, Estudiante> Esto me permite buscar los estudiantes y materias por id de manera facil

TreeMap<String, Aula> Esto lo que hace es guardar datos de manera ordenada de menor a mayor

LinkedList<Materia> Se usa para pre-requisitos e historial academico porque permite agregar elementos de forma sencilla

Queue<Estudiante> Es una cola de espera, el primero en entrar sera el primero en salir.

Stack<Accion> Esto es el ultimo en entrar el primero en salir. Esto sirve para revertir las acciones que realizaste ya sea eliminar un estudiante o etc.

boolean[7][24] aqui se guarda los dias y las horas para consultar la disponibilidad de las aulas

Double[10][20] Respresenta notas por semestre y posicion de materia(las posicion de materias sirve porque ahi un limite de materias por semestre entonces esa es la posicion de la materia)


Se uso java collection para que el codigo sea mas corto y facil de mantener 

El algoritmo de Dijkstra se aplica sobre una matriz, cada matriz indica la distacia entre dos edificios y si no ahi una conexion directa se usa un valor gigante llamado INF

EL deshacer y rehacer funciona con una clase llamada accion. Esta clase conserva el tipo de operacion que se hizo y los datos minimos necesarios para aplicar la operacion a la inversa

LAs excepciones se usan cuando ocurren errores inesperados con el fin de evitar cierres inesperados

