package logica;

import java.util.ArrayList;
import dominio.*;

public interface SistemaIn {

    // iniciar
    public void iniciarSistema();


    // leer archivos
    public void cargarUsuarios();
    public void cargarCertificaciones();
    public void cargarEstudiantes();
    public void cargarCursos();
    public void cargarRegistros();
    public void cargarNotas();
    public void cargarAsignaturas();


    // ADMINISTRADOR

    // crear / modificar / eliminar usuarios
    public boolean crearCuenta(Usuario u);
    public boolean modificarCuenta(Usuario u);
    public boolean eliminarCuenta(String nombreUsuario);
    public boolean restablecerContrasena(String nombreUsuario);

    // obtener usuarios (para tablas o menús)
    public ArrayList<Usuario> getUsuarios();


    // COORDINADOR

    // gestión de certificaciones
    public Certificacion buscarCertificacion(String id);
    public boolean modificarLineaCertificacion(Certificacion c);
    public ArrayList<Certificacion> getCertificaciones();

    // generar certificados
    public ArrayList<RegistroCertificacion> getRegistrosCertificacion(String idCertificacion);

    // métricas
    public int contarInscritos(String idCertificacion);
    public ArrayList<Curso> obtenerAsignaturasCriticas(String idCertificacion);

    // perfiles estudiantes en una certificación
    public ArrayList<Estudiante> obtenerPerfilesDeCertificacion(String idCertificacion);


    // ESTUDIANTE

    // perfil
    public Estudiante getEstudiante(String rut);

    // notas
    public ArrayList<Nota> getNotasEstudiante(String rut);

    // malla
    public ArrayList<Curso> getMallaCompleta(String rut);
    public ArrayList<Curso> getAsignaturasPendientes(String rut);

    // promedios
    public double calcularPromedioGeneral(String rut);
    public double calcularPromedioSemestre(String rut, int semestre);

    // certificaciones
    public boolean inscribirCertificacion(String rut, String idCertificacion);
    public boolean verificarRequisitos(String rut, String idCertificacion);
    public ArrayList<RegistroCertificacion> getCertificacionesInscritas(String rut);
    public ArrayList<Curso> getRamosFaltantesCertificacion(String rut, String idCertificacion);

}
