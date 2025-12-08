package dominio;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import logica.SistemaIn;

public class Sistema implements SistemaIn {

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

    public Sistema() { }
    //   MÉTODO PRINCIPAL SISTEMA
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

                String[] p = linea.split(";");
            
                String nombreUsuario = p[0].trim();
                String contrasena    = p[1].trim();
                String rol           = p[2].trim();
                String infoExtra     = (p.length > 3) ? p[3].trim() : "";

               
                Usuario u = new Usuario(nombreUsuario, contrasena, rol, infoExtra);
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

                String[] p = linea.split(";");
                // RUT;Nombre;Carrera;Semestre;Correo;Contraseña
                String rut       = p[0].trim();
                String nombre    = p[1].trim();
                String carrera   = p[2].trim();
                int semestre     = Integer.parseInt(p[3].trim());
                String correo    = p[4].trim();
                String pass      = p[5].trim();

                Estudiante e = new Estudiante(rut, nombre, carrera, semestre, correo, pass);
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

                String[] p = linea.split(";");
             
                String nrc        = p[0].trim();
                String nombre     = p[1].trim();
                int semestre      = Integer.parseInt(p[2].trim());
                int creditos      = Integer.parseInt(p[3].trim());
                String area       = p[4].trim();
                String prereqText = (p.length > 5) ? p[5].trim() : "";

                Curso c = new Curso(nrc, nombre, semestre, creditos, area, prereqText);
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

    @Override
    public ArrayList<Curso> obtenerAsignaturasCriticas(String idCertificacion) {
        // Aquí puedes después aplicar tu lógica real de "asignatura crítica"
        ArrayList<Curso> criticas = new ArrayList<>();
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
    public double calcularPromedioSemestre(String rut, int semestre) {
        double suma = 0;
        int count = 0;
        for (Nota n : notas) {
            if (n.getRutEstudiante().equals(rut)
                    && Integer.parseInt(n.getSemestre()) == semestre) {
                suma += n.getCalificacion();
                count++;
            }
        }
        return count == 0 ? 0 : suma / count;
    }

    @Override
    public boolean inscribirCertificacion(String rut, String idCertificacion) {
        // Podrías validar verificarRequisitos(rut, idCertificacion) antes
        RegistroCertificacion r = new RegistroCertificacion(
                rut, idCertificacion, "2024-12-05", "Activa", 0.0);
        return registros.add(r);
    }

    @Override
    public boolean verificarRequisitos(String rut, String idCertificacion) {
        // Lógica real de requisitos la puedes implementar después
        return true;
    }

    @Override
    public ArrayList<RegistroCertificacion> getCertificacionesInscritas(String rut) {
        ArrayList<RegistroCertificacion> lista = new ArrayList<>();
        for (RegistroCertificacion r : registros) {
            if (r.getRutEstudiante().equals(rut)) lista.add(r);
        }
        return lista;
    }
}

