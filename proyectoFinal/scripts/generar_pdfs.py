from pathlib import Path

from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER, TA_JUSTIFY
from reportlab.lib.pagesizes import letter
from reportlab.lib.styles import ParagraphStyle, getSampleStyleSheet
from reportlab.lib.units import cm
from reportlab.platypus import (
    PageBreak,
    Paragraph,
    SimpleDocTemplate,
    Spacer,
    Table,
    TableStyle,
)


ROOT = Path(__file__).resolve().parents[1]
OUT_DIR = ROOT / "documentacion"


def crear_estilos():
    estilos = getSampleStyleSheet()
    estilos.add(
        ParagraphStyle(
            name="TituloPortada",
            parent=estilos["Title"],
            fontName="Helvetica-Bold",
            fontSize=22,
            leading=28,
            alignment=TA_CENTER,
            textColor=colors.HexColor("#1F3A5F"),
            spaceAfter=18,
        )
    )
    estilos.add(
        ParagraphStyle(
            name="Subtitulo",
            parent=estilos["Normal"],
            fontName="Helvetica",
            fontSize=12,
            leading=16,
            alignment=TA_CENTER,
            textColor=colors.HexColor("#3F4A56"),
            spaceAfter=24,
        )
    )
    estilos.add(
        ParagraphStyle(
            name="Seccion",
            parent=estilos["Heading1"],
            fontName="Helvetica-Bold",
            fontSize=15,
            leading=19,
            textColor=colors.HexColor("#1F3A5F"),
            spaceBefore=14,
            spaceAfter=8,
        )
    )
    estilos.add(
        ParagraphStyle(
            name="Texto",
            parent=estilos["BodyText"],
            fontName="Helvetica",
            fontSize=10.5,
            leading=15,
            alignment=TA_JUSTIFY,
            spaceAfter=7,
        )
    )
    estilos.add(
        ParagraphStyle(
            name="BulletProyecto",
            parent=estilos["BodyText"],
            fontName="Helvetica",
            fontSize=10.5,
            leading=14,
            leftIndent=14,
            firstLineIndent=-8,
            spaceAfter=4,
        )
    )
    return estilos


def encabezado_pie(canvas, doc):
    canvas.saveState()
    canvas.setFont("Helvetica", 8)
    canvas.setFillColor(colors.HexColor("#5B6670"))
    canvas.drawString(2 * cm, 1.2 * cm, "Proyecto Final - Estructura de Datos")
    canvas.drawRightString(letter[0] - 2 * cm, 1.2 * cm, f"Página {doc.page}")
    canvas.restoreState()


def parrafo(texto, estilos):
    return Paragraph(texto, estilos["Texto"])


def bullet(texto, estilos):
    return Paragraph(f"• {texto}", estilos["BulletProyecto"])


def tabla(datos):
    t = Table(datos, colWidths=[4.2 * cm, 10.4 * cm])
    t.setStyle(
        TableStyle(
            [
                ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#DCE8F5")),
                ("TEXTCOLOR", (0, 0), (-1, 0), colors.HexColor("#1F3A5F")),
                ("FONTNAME", (0, 0), (-1, 0), "Helvetica-Bold"),
                ("FONTNAME", (0, 1), (-1, -1), "Helvetica"),
                ("FONTSIZE", (0, 0), (-1, -1), 9),
                ("GRID", (0, 0), (-1, -1), 0.4, colors.HexColor("#AAB4C0")),
                ("VALIGN", (0, 0), (-1, -1), "TOP"),
                ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.white, colors.HexColor("#F7F9FC")]),
                ("LEFTPADDING", (0, 0), (-1, -1), 6),
                ("RIGHTPADDING", (0, 0), (-1, -1), 6),
                ("TOPPADDING", (0, 0), (-1, -1), 5),
                ("BOTTOMPADDING", (0, 0), (-1, -1), 5),
            ]
        )
    )
    return t


def crear_pdf(nombre_archivo, titulo, subtitulo, elementos):
    OUT_DIR.mkdir(exist_ok=True)
    ruta = OUT_DIR / nombre_archivo
    doc = SimpleDocTemplate(
        str(ruta),
        pagesize=letter,
        rightMargin=2 * cm,
        leftMargin=2 * cm,
        topMargin=2 * cm,
        bottomMargin=2 * cm,
    )
    estilos = crear_estilos()
    historia = [
        Spacer(1, 2.5 * cm),
        Paragraph(titulo, estilos["TituloPortada"]),
        Paragraph(subtitulo, estilos["Subtitulo"]),
        Spacer(1, 1 * cm),
        parrafo(
            "Sistema universitario desarrollado en Java 17 para gestionar estudiantes, materias, horarios, rutas de campus, reportes académicos y operaciones de deshacer/rehacer.",
            estilos,
        ),
        PageBreak(),
    ]
    historia.extend(elementos(estilos))
    doc.build(historia, onFirstPage=encabezado_pie, onLaterPages=encabezado_pie)
    return ruta


def contenido_manual(estilos):
    e = []
    e.append(Paragraph("1. Objetivo Del Sistema", estilos["Seccion"]))
    e.append(parrafo("El sistema permite administrar información académica básica desde consola. Está diseñado para registrar estudiantes, crear materias, validar pre-requisitos, controlar cupos, reservar aulas, calcular rutas entre edificios y consultar reportes académicos.", estilos))

    e.append(Paragraph("2. Inicio Del Programa", estilos["Seccion"]))
    e.append(parrafo("Para compilar y ejecutar el proyecto desde PowerShell, ubíquese en la carpeta raíz del proyecto y use los siguientes comandos:", estilos))
    e.append(tabla([["Acción", "Comando"], ["Compilar", "javac -d out (Get-ChildItem -Recurse -Filter *.java).FullName"], ["Ejecutar", "java -cp out Main"]]))
    e.append(Spacer(1, 0.3 * cm))

    e.append(Paragraph("3. Gestión De Estudiantes", estilos["Seccion"]))
    for texto in [
        "Opción 1: registra un estudiante solicitando nombre, ID, email y semestre.",
        "Opción 2: busca un estudiante por ID usando el HashMap de estudiantes.",
        "Opción 3: lista todos los estudiantes registrados.",
        "Opción 4: elimina un estudiante y guarda una acción para poder deshacer la eliminación.",
    ]:
        e.append(bullet(texto, estilos))

    e.append(Paragraph("4. Gestión De Materias", estilos["Seccion"]))
    for texto in [
        "Opción 5: crea una materia con código, nombre, cupo y créditos.",
        "Opción 6: agrega un pre-requisito a una materia existente.",
        "Opción 7: muestra los pre-requisitos almacenados en la LinkedList de la materia.",
        "Opción 8: inscribe un estudiante. Primero valida pre-requisitos y luego verifica el cupo.",
        "Opción 9: cancela una inscripción. Si hay estudiantes en cola de espera, se promueve el primero.",
        "Opción 10: muestra la cola de espera de una materia.",
    ]:
        e.append(bullet(texto, estilos))

    e.append(Paragraph("5. Gestión De Horarios", estilos["Seccion"]))
    e.append(parrafo("Cada aula usa una matriz boolean[7][24]. El primer índice representa el día de la semana y el segundo representa la hora. El valor false significa disponible y true significa reservado.", estilos))
    for texto in [
        "Opción 11: reserva un horario. Si la casilla ya está ocupada, se lanza HorarioConflictivoException.",
        "Opción 12: libera un horario reservado.",
        "Opción 13: consulta si un aula está disponible en un día y hora específicos.",
    ]:
        e.append(bullet(texto, estilos))

    e.append(Paragraph("6. Rutas Entre Edificios", estilos["Seccion"]))
    e.append(parrafo("El campus tiene un grafo fijo de cinco edificios: Ingeniería, Biblioteca, Cafetería, Rectoría y Laboratorios. Las distancias se almacenan en una matriz int[5][5].", estilos))
    e.append(bullet("Opción 14: muestra las conexiones del campus y sus distancias.", estilos))
    e.append(bullet("Opción 15: calcula la ruta más corta entre dos edificios usando Dijkstra.", estilos))
    e.append(parrafo("Ejemplo: si se ingresa origen Ingeniería y destino Rectoría, el sistema calcula la ruta más corta según la matriz de distancias.", estilos))

    e.append(Paragraph("7. Reportes Académicos", estilos["Seccion"]))
    for texto in [
        "Opción 16: registra una nota en la matriz Double[10][20] del estudiante.",
        "Opción 17: muestra promedio por semestre, promedio acumulado y materias reprobadas.",
        "Opción 18: recupera reportes anteriores desde una Stack<String>.",
    ]:
        e.append(bullet(texto, estilos))

    e.append(Paragraph("8. Deshacer Y Rehacer", estilos["Seccion"]))
    e.append(parrafo("Las opciones 19 y 20 usan pilas Stack<Accion>. Cada operación importante guarda una Acción con datos mínimos para ejecutar su operación inversa o repetirla.", estilos))
    e.append(tabla([["Operación", "Acción inversa"], ["Inscripción", "Remover estudiante de la materia"], ["Cancelación", "Reinscribir estudiante"], ["Eliminación", "Restaurar estudiante en el HashMap"], ["Nota", "Restaurar nota anterior"], ["Horario", "Liberar o reservar según corresponda"]]))

    e.append(Paragraph("9. Recomendaciones De Uso", estilos["Seccion"]))
    for texto in [
        "Crear primero estudiantes y materias antes de intentar inscribir.",
        "Registrar notas aprobadas para que los pre-requisitos puedan validarse correctamente.",
        "Escribir los nombres de edificios tal como aparecen en la opción 14.",
        "Usar las opciones 19 y 20 inmediatamente después de operaciones que se quieran revertir o repetir.",
    ]:
        e.append(bullet(texto, estilos))
    return e


def contenido_justificacion(estilos):
    e = []
    e.append(Paragraph("1. Enfoque Arquitectónico", estilos["Seccion"]))
    e.append(parrafo("El proyecto adopta una arquitectura simplificada con separación por paquetes: modelo para entidades, estructuras para la lógica central y excepciones para errores personalizados. Esta decisión facilita explicar responsabilidades y evita mezclar menú, reglas de negocio y datos.", estilos))

    e.append(Paragraph("2. Justificación De Estructuras", estilos["Seccion"]))
    e.append(tabla([
        ["Estructura", "Justificación"],
        ["HashMap<String, Estudiante>", "Permite buscar estudiantes por ID en tiempo promedio O(1), ideal para consultas frecuentes."],
        ["HashMap<String, Materia>", "Permite localizar materias por código de forma rápida y directa."],
        ["TreeMap<String, Aula>", "Mantiene las aulas ordenadas por nombre, útil para listarlas de forma clara."],
        ["LinkedList<Materia>", "Se usa para pre-requisitos e historial académico porque permite agregar elementos de forma sencilla y mantiene el orden."],
        ["Queue<Estudiante>", "Representa naturalmente la cola de espera: primero en entrar, primero en salir."],
        ["Stack<Accion>", "Modela deshacer/rehacer porque la última acción realizada es la primera que debe revertirse."],
        ["Stack<String>", "Guarda reportes ya generados para navegación simple hacia atrás."],
        ["boolean[7][24]", "Representa disponibilidad semanal de aulas con acceso directo por día y hora."],
        ["Double[10][20]", "Representa notas por semestre y posición de materia con límites claros."],
        ["int[5][5]", "Representa el grafo fijo del campus como matriz de adyacencia."],
    ]))
    e.append(Spacer(1, 0.3 * cm))

    e.append(Paragraph("3. Por Qué Usar Java Collections", estilos["Seccion"]))
    e.append(parrafo("Se eligieron estructuras nativas de Java para reducir complejidad accidental. En lugar de implementar manualmente listas, pilas y colas, el proyecto se concentra en aplicar correctamente las estructuras al problema: inscripción, espera, reportes, rutas y deshacer/rehacer.", estilos))
    for texto in [
        "El código queda más corto y fácil de mantener.",
        "Las estructuras son confiables porque pertenecen a la API estándar de Java.",
        "La sustentación puede enfocarse en por qué se usa cada estructura y no en errores internos de implementación.",
    ]:
        e.append(bullet(texto, estilos))

    e.append(Paragraph("4. Matrices Nativas", estilos["Seccion"]))
    e.append(parrafo("Las matrices se mantienen como arreglos nativos porque el problema tiene tamaños fijos: 7 días, 24 horas, 10 semestres, 20 materias por semestre y 5 edificios. Esto hace que el acceso sea directo y muy eficiente.", estilos))
    e.append(tabla([["Matriz", "Uso"], ["boolean[7][24]", "Control de disponibilidad de aulas."], ["Double[10][20]", "Registro de notas por semestre."], ["Materia[10][20]", "Relación entre cada nota y su materia."], ["int[5][5]", "Distancias entre edificios para Dijkstra."]]))

    e.append(Paragraph("5. Dijkstra Con Grafo Fijo", estilos["Seccion"]))
    e.append(parrafo("El algoritmo de Dijkstra se aplica sobre una matriz de adyacencia int[5][5]. Cada posición matrizDistancias[i][j] indica la distancia entre dos edificios. Si no hay conexión directa, se usa un valor grande llamado INF.", estilos))
    for texto in [
        "El arreglo distancias guarda la mejor distancia conocida desde el origen.",
        "El arreglo anteriores permite reconstruir la ruta final.",
        "El arreglo visitados evita procesar dos veces el mismo edificio.",
        "En cada paso se selecciona el edificio no visitado con menor distancia acumulada.",
    ]:
        e.append(bullet(texto, estilos))

    e.append(Paragraph("6. Deshacer/Rehacer Con Accion", estilos["Seccion"]))
    e.append(parrafo("En lugar de clonar todo el estado del sistema, se guarda una clase ligera Accion. Esta clase conserva el tipo de operación y los datos mínimos necesarios para aplicar la operación inversa.", estilos))
    e.append(parrafo("Esta decisión reduce memoria, simplifica el código y es suficiente para operaciones como inscripción, cancelación, eliminación, registro de notas y cambios de horario.", estilos))

    e.append(Paragraph("7. Manejo De Excepciones", estilos["Seccion"]))
    e.append(parrafo("Las excepciones personalizadas hacen explícitas las reglas de negocio. En el menú principal se capturan con try-catch para evitar que el programa se cierre ante errores esperados.", estilos))
    e.append(tabla([
        ["Excepción", "Cuándo se usa"],
        ["EstudianteNoEncontradoException", "Cuando no existe el ID consultado."],
        ["PreRequisitoNoAprobadoException", "Cuando un estudiante no cumple los pre-requisitos."],
        ["CupoLlenoException", "Cuando una materia no tiene cupos disponibles."],
        ["HorarioConflictivoException", "Cuando un aula ya está reservada."],
        ["PilaDeshacerVaciaException", "Cuando no hay acciones para deshacer o rehacer."],
    ]))

    e.append(Paragraph("8. Conclusión", estilos["Seccion"]))
    e.append(parrafo("La solución prioriza claridad, mantenimiento y explicación académica. Cada estructura se eligió porque se ajusta naturalmente a una regla del negocio: HashMap para búsqueda rápida, Queue para espera, Stack para deshacer/rehacer, LinkedList para listas académicas y matrices para datos de tamaño fijo.", estilos))
    return e


if __name__ == "__main__":
    manual = crear_pdf(
        "Manual_de_Usuario_Sistema_Universitario.pdf",
        "Manual De Usuario",
        "Sistema Universitario - Proyecto de Estructura de Datos",
        contenido_manual,
    )
    justificacion = crear_pdf(
        "Justificacion_Estructuras_De_Datos.pdf",
        "Justificación De Estructuras De Datos",
        "Decisiones de diseño y selección de estructuras",
        contenido_justificacion,
    )
    print(manual)
    print(justificacion)
