package dominio;

import java.util.ArrayList;

public class Sistema {
	
	private ArrayList<Usuario> usuarios = new ArrayList<>();
	private ArrayList<Estudiante> estudiantes = new ArrayList<>();
	private ArrayList<Curso> cursos = new ArrayList<>();
	private ArrayList<Certificacion> certificaciones = new ArrayList<>();
	private ArrayList<RegistroCertificacion> registros = new ArrayList<>();
	private ArrayList<Nota> notas = new ArrayList<>();
	private ArrayList<AsignaturaCertificacion> asignaturasCertificaciones = new ArrayList<>();
	
	public Sistema() {}

	public ArrayList<Usuario> getUsuarios() {return usuarios;}

	public ArrayList<Estudiante> getEstudiantes() {return estudiantes;}

	public ArrayList<Curso> getCursos() {return cursos;}

	public ArrayList<Certificacion> getCertificaciones() {return certificaciones;}

	public ArrayList<RegistroCertificacion> getRegistros() {return registros;}

	public ArrayList<Nota> getNotas() {return notas;}

	public ArrayList<AsignaturaCertificacion> getAsignaturasCertificaciones() {return asignaturasCertificaciones;}
	
	
	
	
	
}
