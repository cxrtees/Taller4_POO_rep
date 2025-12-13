package dominio;
//Catalina Isidora Rojas Macaya - 21.953.080-3 - ICCI
//Benjamín Ismael Cortés Acuña - 21.890.703-2 - ICCI
/**
 * Clase principal del sistema AcademiCore.
 * Centraliza la lógica de negocio, gestiona las colecciones cargadas desde archivos
 * (usuarios, estudiantes, cursos, certificaciones, registros, notas y relaciones)
 * y expone los servicios definidos por {@link logica.SistemaIn}.
 *  *
 * <p>Patrones aplicados:
 * <ul>
 *   <li><b>Singleton</b>: asegura una única instancia mediante {@code getInstancia()}.</li>
 *   <li><b>Strategy</b>: permite cambiar la forma de verificación de requisitos usando
 *       {@link logica.EstrategiaVerificacion}.</li>
 * </ul>
 * 
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import logica.EstrategiaVerificacion;
import logica.SistemaIn;

public class Sistema implements SistemaIn {
	
	// SINGLETON
	private static Sistema instancia;
	
	// STRATEGY
	private EstrategiaVerificacion estrategiaVerificacion;
	
	// constructor priv para que nadie pueda hacer un "new Sistema()" desde fuera
	private Sistema() {}	
	
	// método estático de acceso global 
	/**
	 * Retorna la única instancia del sistema (Singleton).
	 * Si no existe, la crea de forma perezosa (lazy initialization).
	 *
	 * @return instancia única de {@code Sistema}.
	 */

	public static Sistema getInstancia() {
		if (instancia == null) {
			instancia = new Sistema();
		}
		return instancia;
	}
	
	// setter opcional por si alguien quiere cambiar la estrategia
	/**
	 * Define la estrategia de verificación académica a utilizar (Strategy).
	 * Permite cambiar la lógica de validación sin modificar el sistema.
	 *
	 * @param estrategia estrategia a utilizar para verificar requisitos.
	 */
	public void setEstrategiaVerificacion(EstrategiaVerificacion estrategia) {
		this.estrategiaVerificacion = estrategia;
	}
	
    // Rutas a los archivos de texto
    private static final String RUTA_USUARIOS        = "usuarios.txt";
    private static final String RUTA_ESTUDIANTES     = "estudiantes.txt";
    private static final String RUTA_CURSOS          = "cursos.txt";
    private static final String RUTA_CERTIFICACIONES = "Certificaciones.txt";
    private static final String RUTA_REGISTROS       = "Registros.txt";
    private static final String RUTA_NOTAS           = "Notas.txt";
    private static final String RUTA_ASIG_CERT       = "Asignaturas_certificaciones.txt";

    // Listas del sistema
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private ArrayList<Estudiante> estudiantes = new ArrayList<>();
    private ArrayList<Curso> cursos = new ArrayList<>();
    private ArrayList<Certificacion> certificaciones = new ArrayList<>();
    private ArrayList<RegistroCertificacion> registros = new ArrayList<>();
    private ArrayList<Nota> notas = new ArrayList<>();
    private ArrayList<AsignaturaCertificacion> asignaturasCertificaciones = new ArrayList<>();

    /**
     * Inicializa el sistema cargando todos los datos desde los archivos de texto.
     * Carga usuarios, estudiantes, cursos, certificaciones, registros, notas y asignaturas por certificación.
     */
    @Override
    public void iniciarSistema() {
    	/**
    	 * Carga la lista de usuarios desde el archivo correspondiente.
    	 * Limpia la lista actual y luego agrega los usuarios leídos.
    	 */
        cargarUsuarios();
        /**
         * Carga la lista de estudiantes desde el archivo correspondiente.
         * Limpia la lista actual y luego agrega los estudiantes leídos.
         */
        cargarEstudiantes();
        /**
         * Carga la lista de cursos desde el archivo correspondiente.
         * Limpia la lista actual y luego agrega los cursos leídos.
         */
        cargarCursos();
        /**
         * Carga la lista de certificaciones desde el archivo correspondiente.
         * Limpia la lista actual y luego agrega las certificaciones leídas.
         */
        cargarCertificaciones();
        /**
         * Carga la lista de registros de certificación (inscripciones) desde el archivo correspondiente.
         * Limpia la lista actual y luego agrega los registros leídos.
         */
        cargarRegistros();
        /**
         * Carga la lista de notas desde el archivo correspondiente.
         * Limpia la lista actual y luego agrega las notas leídas.
         */
        cargarNotas();
        /**
         * Carga las relaciones entre certificaciones y cursos requeridos desde el archivo correspondiente.
         * Limpia la lista actual y luego agrega las relaciones leídas.
         */
        cargarAsignaturas();

    }

    //   CARGA DE ARCHIVOS (Scanner)

    @Override
    public void cargarUsuarios() {
        usuarios.clear();
        try {
            File file = new File(RUTA_USUARIOS);
            Scanner sc = new Scanner(file, "UTF-8");

            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (linea.isEmpty()) continue;

                Usuario u = UsuarioFactory.crearDesdeLinea(linea);
                usuarios.add(u);
            }

            sc.close();
        } catch (Exception e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
        }
    }

    @Override
    public void cargarEstudiantes() {
        estudiantes.clear();
        try {
            File file = new File(RUTA_ESTUDIANTES);
            Scanner sc = new Scanner(file, "UTF-8");

            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (linea.isEmpty()) continue;
                
                Estudiante e = EstudianteFactory.crearDesdeLinea(linea);
                estudiantes.add(e);
            }

            sc.close();
        } catch (Exception e) {
            System.out.println("Error al cargar estudiantes: " + e.getMessage());
        }
    }

    @Override
    public void cargarCursos() {
        cursos.clear();
        try {
            File file = new File(RUTA_CURSOS);
            Scanner sc = new Scanner(file, "UTF-8");

            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (linea.isEmpty()) continue;

                Curso c = CursoFactory.crearDesdeLinea(linea);
                cursos.add(c);
            }

            sc.close();
        } catch (Exception e) {
            System.out.println("Error al cargar cursos: " + e.getMessage());
        }
    }

    @Override
    public void cargarCertificaciones() {
        certificaciones.clear();
        try {
            File file = new File(RUTA_CERTIFICACIONES);
            Scanner sc = new Scanner(file, "UTF-8");

            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (linea.isEmpty()) continue;

                Certificacion c = CertificacionFactory.crearDesdeLinea(linea);
                certificaciones.add(c);
            }

            sc.close();
        } catch (Exception e) {
            System.out.println("Error al cargar certificaciones: " + e.getMessage());
        }
    }

    @Override
    public void cargarRegistros() {
        registros.clear();
        try {
            File file = new File(RUTA_REGISTROS);
            Scanner sc = new Scanner(file, "UTF-8");

            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (linea.isEmpty()) continue;

                RegistroCertificacion r  = RegistroCertificacionFactory.crearDesdeLinea(linea);
                registros.add(r);
            }

            sc.close();
        } catch (Exception e) {
            System.out.println("Error al cargar registros: " + e.getMessage());
        }
    }

    @Override
    public void cargarNotas() {
        notas.clear();
        try {
            File file = new File(RUTA_NOTAS);
            Scanner sc = new Scanner(file, "UTF-8");

            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (linea.isEmpty()) continue;

                Nota n = NotaFactory.crearDesdeLinea(linea);
                notas.add(n);
            }

            sc.close();
        } catch (Exception e) {
            System.out.println("Error al cargar notas: " + e.getMessage());
        }
    }

    @Override
    public void cargarAsignaturas() {
        asignaturasCertificaciones.clear();
        try {
            File file = new File(RUTA_ASIG_CERT);
            Scanner sc = new Scanner(file, "UTF-8");

            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (linea.isEmpty()) continue;
               
                AsignaturaCertificacion ac = AsignaturaCertificacionFactory.crearDesdeLinea(linea);
                asignaturasCertificaciones.add(ac);
            }

            sc.close();
        } catch (Exception e) {
            System.out.println("Error al cargar asignaturas_certificaciones: " + e.getMessage());
        }
    }

    public ArrayList<Usuario> getUsuariosInternos() {
        return usuarios;
    }

    public ArrayList<Estudiante> getEstudiantesInternos() {
        return estudiantes;
    }

    public ArrayList<Curso> getCursosInternos() {
        return cursos;
    }

    public ArrayList<Certificacion> getCertificacionesInternas() {
        return certificaciones;
    }

    public ArrayList<RegistroCertificacion> getRegistrosInternos() {
        return registros;
    }

    public ArrayList<Nota> getNotasInternas() {
        return notas;
    }

    public ArrayList<AsignaturaCertificacion> getAsignaturasCertificacionesInternas() {
        return asignaturasCertificaciones;
    }
//admi
    
    /**
     * Crea una nueva cuenta de usuario en el sistema.
     *
     * @param u usuario a agregar.
     * @return {@code true} si se agregó correctamente; {@code false} en caso contrario.
     */
    @Override
    public boolean crearCuenta(Usuario u) {
        return usuarios.add(u);
    }
    
    /**
     * Modifica los datos de una cuenta existente, identificándola por su nombre de usuario.
     *
     * @param u usuario con los datos actualizados.
     * @return {@code true} si se modificó; {@code false} si no se encontró el usuario.
     */
    @Override
    public boolean modificarCuenta(Usuario u) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getNombreUsuario().equals(u.getNombreUsuario())) {
                usuarios.set(i, u);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Elimina una cuenta de usuario según su nombre de usuario.
     *
     * @param nombreUsuario nombre de usuario a eliminar.
     * @return {@code true} si se eliminó; {@code false} si no existía.
     */
    @Override
    public boolean eliminarCuenta(String nombreUsuario) {
        return usuarios.removeIf(u -> u.getNombreUsuario().equals(nombreUsuario));
    }
    
    /**
     * Restablece la contraseña de un usuario a un valor por defecto.
     *
     * @param nombreUsuario nombre de usuario al que se le restablece la contraseña.
     * @return {@code true} si se restableció; {@code false} si no se encontró el usuario.
     */
    @Override
    public boolean restablecerContrasena(String nombreUsuario) {
        for (Usuario u : usuarios) {
            if (u.getNombreUsuario().equals(nombreUsuario)) {
                u.setContraseña("1234"); // contraseña por defecto
                return true;
            }
        }
        return false;
    }
    
    /**
     * Retorna la lista de usuarios del sistema.
     *
     * @return lista de usuarios cargados.
     */
    @Override
    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    // ---------- COORDINADOR ----------
    
    /**
     * Busca una certificación por su identificador.
     *
     * @param id id de la certificación.
     * @return la certificación encontrada o {@code null} si no existe.
     */
    @Override
    public Certificacion buscarCertificacion(String id) {
        for (Certificacion c : certificaciones) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }
    
    /**
     * Modifica una certificación existente reemplazándola por una nueva versión (misma id).
     *
     * @param c certificación con valores actualizados.
     * @return {@code true} si se modificó; {@code false} si no se encontró la certificación.
     */
    @Override
    public boolean modificarLineaCertificacion(Certificacion c) {
        for (int i = 0; i < certificaciones.size(); i++) {
            if (certificaciones.get(i).getId().equals(c.getId())) {
                certificaciones.set(i, c);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Retorna la lista de certificaciones disponibles.
     *
     * @return lista de certificaciones.
     */
    @Override
    public ArrayList<Certificacion> getCertificaciones() {
        return certificaciones;
    }
    
    /**
     * Obtiene los registros (inscripciones) asociados a una certificación.
     *
     * @param idCertificacion id de la certificación.
     * @return lista de registros de esa certificación.
     */
    @Override
    public ArrayList<RegistroCertificacion> getRegistrosCertificacion(String idCertificacion) {
        ArrayList<RegistroCertificacion> lista = new ArrayList<>();
        for (RegistroCertificacion r : registros) {
            if (r.getIdCertificacion().equals(idCertificacion)) {
                lista.add(r);
            }
        }
        return lista;
    }
    
    /**
     * Cuenta cuántos estudiantes están inscritos en una certificación.
     *
     * @param idCertificacion id de la certificación.
     * @return cantidad de inscritos.
     */
    @Override
    public int contarInscritos(String idCertificacion) {
        int count = 0;
        for (RegistroCertificacion r : registros) {
            if (r.getIdCertificacion().equals(idCertificacion)) count++;
        }
        return count;
    }

    /**
     * Analiza los cursos asociados a una certificación y retorna aquellos considerados "críticos"
     * según un umbral de reprobación.
     *
     * @param idCertificacion id de la certificación.
     * @return lista de cursos críticos.
     */
    public ArrayList<Curso> obtenerAsignaturasCriticas(String idCertificacion) {
        ArrayList<Curso> criticas = new ArrayList<>();

        // 1. Buscar todos los cursos asociados a esa certificación
        for (AsignaturaCertificacion ac : asignaturasCertificaciones) {
            if (!ac.getIdCertificacion().equals(idCertificacion)) continue;

            Curso curso = buscarCursoPorNrc(ac.getNrcCurso());
            if (curso == null) continue;

            // 2. Mirar las notas de ese curso
            int total = 0;
            int reprobados = 0;

            for (Nota n : notas) {
                if (n.getCodigoAsignatura().equals(curso.getNcr())) {
                    total++;
                    if (n.getCalificacion() < 4.0) {
                        reprobados++;
                    }
                }
            }

            // 3. Si tiene muchas reprobaciones, lo marcamos como "crítico"
            if (total > 0) {
                double porcentajeReprob = (double) reprobados / total;
                if (porcentajeReprob >= 0.4) { // 40% o más reprobados
                    criticas.add(curso);
                }
            }
        }

        return criticas;
    }

    /**
     * Obtiene los perfiles de estudiantes inscritos en una certificación.
     *
     * @param idCertificacion id de la certificación.
     * @return lista de estudiantes inscritos.
     */
    @Override
    public ArrayList<Estudiante> obtenerPerfilesDeCertificacion(String idCertificacion) {
        ArrayList<Estudiante> lista = new ArrayList<>();
        for (RegistroCertificacion r : registros) {
            if (r.getIdCertificacion().equals(idCertificacion)) {
                Estudiante e = buscarEstudiantePorRUT(r.getRutEstudiante());
                if (e != null) lista.add(e);
            }
        }
        return lista;
    }

    // ---------- ESTUDIANTE ----------

    private Estudiante buscarEstudiantePorRUT(String rut) {
        for (Estudiante e : estudiantes) {
            if (e.getRut().equals(rut)) return e;
        }
        return null;
    }
    
    /**
     * Obtiene el perfil de un estudiante por su RUT.
     *
     * @param rut rut del estudiante.
     * @return estudiante encontrado o {@code null} si no existe.
     */
    @Override
    public Estudiante getEstudiante(String rut) {
        return buscarEstudiantePorRUT(rut);
    }
    
    /**
     * Obtiene todas las notas asociadas a un estudiante.
     *
     * @param rut rut del estudiante.
     * @return lista de notas del estudiante.
     */
    @Override
    public ArrayList<Nota> getNotasEstudiante(String rut) {
        ArrayList<Nota> lista = new ArrayList<>();
        for (Nota n : notas) {
            if (n.getRutEstudiante().equals(rut)) lista.add(n);
        }
        return lista;
    }
    
    /**
     * Retorna la malla completa para un estudiante.
     * En esta implementación se retorna la lista general de cursos.
     *
     * @param rut rut del estudiante.
     * @return lista de cursos de la malla.
     */
    @Override
    public ArrayList<Curso> getMallaCompleta(String rut) {
        // Si todos tienen misma malla, puedes devolver cursos tal cual
        return cursos;
    }
    
    /**
     * Retorna las asignaturas pendientes de un estudiante.
     * (Método disponible para implementar la lógica de pendientes).
     *
     * @param rut rut del estudiante.
     * @return lista de cursos pendientes.
     */
    @Override
    public ArrayList<Curso> getAsignaturasPendientes(String rut) {
        ArrayList<Curso> pendientes = new ArrayList<>();
        // Aquí puedes implementar: cursos no aprobados aún
        return pendientes;
    }
    
    /**
     * Calcula el promedio general de un estudiante usando todas sus notas registradas.
     *
     * @param rut rut del estudiante.
     * @return promedio general, o 0 si no tiene notas.
     */
    @Override
    public double calcularPromedioGeneral(String rut) {
        double suma = 0;
        int count = 0;
        for (Nota n : notas) {
            if (n.getRutEstudiante().equals(rut)) {
                suma += n.getCalificacion();
                count++;
            }
        }
        return count == 0 ? 0 : suma / count;
    }

    /**
     * Calcula el promedio de un estudiante para un semestre específico.
     *
     * @param rut rut del estudiante.
     * @param semestre semestre a evaluar (por ejemplo 1 o 2).
     * @return promedio del semestre, o 0 si no hay notas en ese semestre.
     */
    @Override
    public double calcularPromedioSemestre(String rut, int semestreBuscado) {
        double suma = 0;
        int count = 0;

        for (Nota n : notas) {
            if (!n.getRutEstudiante().equals(rut)) {
                continue;
            }

            String semStr = n.getSemestre();   // ej: "2023-2"
            int semNota;

            try {
                if (semStr.contains("-")) {
                    // toma la parte DESPUÉS del '-'  → "2"
                    String[] partes = semStr.split("-");
                    semNota = Integer.parseInt(partes[1].trim());
                } else {
                    // por si en algún caso viene "2" a secas
                    semNota = Integer.parseInt(semStr.trim());
                }
            } catch (NumberFormatException e) {
                // si viene raro, lo saltamos
                continue;
            }

            if (semNota == semestreBuscado) {
                suma += n.getCalificacion();
                count++;
            }
        }

        return count == 0 ? 0.0 : suma / count;
    }


    /**
     * Inscribe a un estudiante en una certificación creando un nuevo registro.
     *
     * @param rut rut del estudiante.
     * @param idCertificacion id de la certificación.
     * @return {@code true} si se agregó el registro; {@code false} en caso contrario.
     */
    @Override
    public boolean inscribirCertificacion(String rut, String idCertificacion) {
        // Podrías validar verificarRequisitos(rut, idCertificacion) antes
        RegistroCertificacion r = new RegistroCertificacion(
                rut, idCertificacion, "2024-12-05", "Activa", 0.0);
        return registros.add(r);
    }
    
    /**
     * Verifica si un estudiante cumple los requisitos académicos para una certificación.
     * En esta implementación se valida que tenga aprobadas (nota >= 4.0) todas las asignaturas requeridas.
     *
     * @param rut rut del estudiante.
     * @param idCertificacion id de la certificación.
     * @return {@code true} si cumple requisitos; {@code false} si no cumple.
     */
    @Override
    public boolean verificarRequisitos(String rut, String idCertificacion) {

        // 1) NRC requeridos por esa certificación
        ArrayList<String> nrcRequeridos = new ArrayList<>();
        for (AsignaturaCertificacion ac : asignaturasCertificaciones) {
            if (ac.getIdCertificacion().equals(idCertificacion)) {
                nrcRequeridos.add(ac.getNrcCurso());
            }
        }

        // Si no hay ramos configurados para esa línea, la dejamos pasar
        if (nrcRequeridos.isEmpty()) {
            return true;
        }

        // 2) Para cada NRC requerido, ver si el estudiante lo tiene APROBADO
        for (String nrc : nrcRequeridos) {
            boolean aprobado = false;

            for (Nota n : notas) {
                if (n.getRutEstudiante().equals(rut)
                        && n.getCodigoAsignatura().equals(nrc)
                        && n.getCalificacion() >= 4.0) {
                    aprobado = true;
                    break;
                }
            }

            // Si encontramos UN ramo requerido que NO esté aprobado → no cumple
            if (!aprobado) {
                return false;
            }
        }

        // Si todos los ramos requeridos están aprobados → cumple requisitos
        return true;
    }
    
    /**
     * Obtiene las certificaciones en las que un estudiante está inscrito.
     *
     * @param rut rut del estudiante.
     * @return lista de registros de certificación del estudiante.
     */
    @Override
    public ArrayList<RegistroCertificacion> getCertificacionesInscritas(String rut) {
        ArrayList<RegistroCertificacion> lista = new ArrayList<>();
        for (RegistroCertificacion r : registros) {
            if (r.getRutEstudiante().equals(rut)) lista.add(r);
        }
        return lista;
    }
    private Curso buscarCursoPorNrc(String nrc) {
        for (Curso c : cursos) {
            if (c.getNcr().equals(nrc)) {
                return c;
            }
        }
        return null;
    }
 // Devuelve la lista de cursos que le faltan al estudiante
 // para poder inscribir la certificación.
   
    /**
     * Retorna los cursos que el estudiante aún no ha aprobado y que son requeridos
     * para inscribir o completar una certificación.
     *
     * @param rut rut del estudiante.
     * @param idCertificacion id de la certificación.
     * @return lista de cursos faltantes.
     */   
 @Override
 public ArrayList<Curso> getRamosFaltantesCertificacion(String rut, String idCertificacion) {

     ArrayList<Curso> faltantes = new ArrayList<>();

     // 1) NRC requeridos por esa certificación
     ArrayList<String> nrcRequeridos = new ArrayList<>();
     for (AsignaturaCertificacion ac : asignaturasCertificaciones) {
         if (ac.getIdCertificacion().equals(idCertificacion)) {
             nrcRequeridos.add(ac.getNrcCurso());
         }
     }

     // 2) Por cada NRC requerido, ver si el estudiante lo tiene APROBADO
     for (String nrc : nrcRequeridos) {
         boolean aprobado = false;

         for (Nota n : notas) {
             if (n.getRutEstudiante().equals(rut)
                     && n.getCodigoAsignatura().equals(nrc)
                     && n.getCalificacion() >= 4.0) {
                 aprobado = true;
                 break;
             }
         }

         // Si no está aprobado, lo agregamos a la lista de faltantes
         if (!aprobado) {
             Curso c = buscarCursoPorNrc(nrc);
             if (c != null) {
                 faltantes.add(c);
             }
         }
     }

     return faltantes;
 }



}

