package dominio;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import logica.SistemaIn;

public class Sistema implements SistemaIn {
	
	// SINGLETON
	private static Sistema instancia;
	
	// constructor priv para que nadie pueda hacer un "new Sistema()" desde fuera
	private Sistema() {}	
	
	// método estático de acceso global 
	public static Sistema getInstancia() {
		if (instancia == null) {
			instancia = new Sistema();
		}
		return instancia;
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

    @Override
    public void iniciarSistema() {

        cargarUsuarios();
        cargarEstudiantes();
        cargarCursos();
        cargarCertificaciones();
        cargarRegistros();
        cargarNotas();
        cargarAsignaturas();

        // Cuando tengas GUI:
        // new gui.VentanaLogin(this).setVisible(true);
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

                String[] p = linea.split(";");
                // ID;Nombre;Descripción;RequisitosCreditos;ValidezAños
                String id          = p[0].trim();
                String nombre      = p[1].trim();
                String descripcion = p[2].trim();
                int creditosReq    = Integer.parseInt(p[3].trim());
                int anosValidez    = Integer.parseInt(p[4].trim());

                Certificacion c = new Certificacion(id, nombre, descripcion, creditosReq, anosValidez);
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

                String[] p = linea.split(";");
                // RUT;IDcert;FechaRegistro;Estado;Progreso
                String rut           = p[0].trim();
                String idCert        = p[1].trim();
                String fechaRegistro = p[2].trim();
                String estado        = p[3].trim();
                double progreso      = Double.parseDouble(p[4].trim());

                RegistroCertificacion r = new RegistroCertificacion(
                        rut, idCert, fechaRegistro, estado, progreso);
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

                String[] p = linea.split(";");
                // RUT;CódigoAsignatura;Calificación;Estado;Semestre
                String rut          = p[0].trim();
                String codAsig      = p[1].trim();
                double calificacion = Double.parseDouble(p[2].trim());
                String estado       = p[3].trim();
                String semestre     = p[4].trim();

                Nota n = new Nota(rut, codAsig, calificacion, estado, semestre);
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

                String[] p = linea.split(";");
                // IDcert;NRCcurso
                String idCert = p[0].trim();
                String nrc    = p[1].trim();

                AsignaturaCertificacion ac = new AsignaturaCertificacion(idCert, nrc);
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

    @Override
    public boolean crearCuenta(Usuario u) {
        return usuarios.add(u);
    }

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

    @Override
    public boolean eliminarCuenta(String nombreUsuario) {
        return usuarios.removeIf(u -> u.getNombreUsuario().equals(nombreUsuario));
    }

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

    @Override
    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    // ---------- COORDINADOR ----------

    @Override
    public Certificacion buscarCertificacion(String id) {
        for (Certificacion c : certificaciones) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

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

    @Override
    public ArrayList<Certificacion> getCertificaciones() {
        return certificaciones;
    }

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

    @Override
    public int contarInscritos(String idCertificacion) {
        int count = 0;
        for (RegistroCertificacion r : registros) {
            if (r.getIdCertificacion().equals(idCertificacion)) count++;
        }
        return count;
    }

  
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

    @Override
    public Estudiante getEstudiante(String rut) {
        return buscarEstudiantePorRUT(rut);
    }

    @Override
    public ArrayList<Nota> getNotasEstudiante(String rut) {
        ArrayList<Nota> lista = new ArrayList<>();
        for (Nota n : notas) {
            if (n.getRutEstudiante().equals(rut)) lista.add(n);
        }
        return lista;
    }

    @Override
    public ArrayList<Curso> getMallaCompleta(String rut) {
        // Si todos tienen misma malla, puedes devolver cursos tal cual
        return cursos;
    }

    @Override
    public ArrayList<Curso> getAsignaturasPendientes(String rut) {
        ArrayList<Curso> pendientes = new ArrayList<>();
        // Aquí puedes implementar: cursos no aprobados aún
        return pendientes;
    }

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



    @Override
    public boolean inscribirCertificacion(String rut, String idCertificacion) {
        // Podrías validar verificarRequisitos(rut, idCertificacion) antes
        RegistroCertificacion r = new RegistroCertificacion(
                rut, idCertificacion, "2024-12-05", "Activa", 0.0);
        return registros.add(r);
    }

    public boolean verificarRequisitos(String rut, String idCertificacion) {

        // 1. Buscar la certificación
        Certificacion cert = buscarCertificacion(idCertificacion);
        if (cert == null) {
            return false;
        }

        int creditosRequeridos = cert.getCreditosRequeridos();

        ArrayList<String> nrcAsociados = new ArrayList<>();
        for (AsignaturaCertificacion ac : asignaturasCertificaciones) {
            if (ac.getIdCertificacion().equals(idCertificacion)) {
                nrcAsociados.add(ac.getNrcCurso());
            }
        }

        if (nrcAsociados.isEmpty()) {
            return true;
        }

        int creditosAprobados = 0;

        for (Nota n : notas) {
            if (!n.getRutEstudiante().equals(rut)) {
                continue;
            }

            if (!nrcAsociados.contains(n.getCodigoAsignatura())) {
                continue;
            }

            if (n.getCalificacion() >= 4.0) {
                Curso c = buscarCursoPorNrc(n.getCodigoAsignatura());
                if (c != null) {
                    creditosAprobados += c.getCreditos();
                }
            }
        }
        return creditosAprobados >= creditosRequeridos;
    }


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
             // Buscar el curso para mostrar nombre, créditos, etc.
             for (Curso c : cursos) {
                 if (c.getNcr().equals(nrc)) {
                     faltantes.add(c);
                     break;
                 }
             }
         }
     }

     return faltantes;
 }


}

