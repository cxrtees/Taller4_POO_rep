package logica;

import java.util.ArrayList;

import dominio.AsignaturaCertificacion;
import dominio.Certificacion;
import dominio.Curso;
import dominio.Estudiante;
import dominio.Nota;
import dominio.RegistroCertificacion;
import dominio.Usuario;

public class Sistema {
	
	private ArrayList<Usuario> usuarios = new ArrayList<>();
	private ArrayList<Estudiante> estudiantes = new ArrayList<>();
	private ArrayList<Curso> cursos = new ArrayList<>();
	private ArrayList<Certificacion> certificaciones = new ArrayList<>();
	private ArrayList<RegistroCertificacion> registros = new ArrayList<>();
	private ArrayList<Nota> notas = new ArrayList<>();
	private ArrayList<AsignaturaCertificacion> asignaturasCertificaciones = new ArrayList<>();
	
	public Sistema() {}
	
	// lectura de archivos
	public void cargarUsuarios() {}
	public void cargarCertficaciones() {}
	public void cargarEstudiantes() {}
	public void cargarCursos() {}

	public ArrayList<Usuario> getUsuarios() {return usuarios;}

	public ArrayList<Estudiante> getEstudiantes() {return estudiantes;}

	public ArrayList<Curso> getCursos() {return cursos;}

	public ArrayList<Certificacion> getCertificaciones() {return certificaciones;}

	public ArrayList<RegistroCertificacion> getRegistros() {return registros;}

	public ArrayList<Nota> getNotas() {return notas;}

	public ArrayList<AsignaturaCertificacion> getAsignaturasCertificaciones() {return asignaturasCertificaciones;}
	
	
	
	
	
}
