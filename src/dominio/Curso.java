package dominio;
//Catalina Isidora Rojas Macaya - 21.953.080-3 - ICCI
//Benjamín Ismael Cortés Acuña - 21.890.703-2 - ICCI
/**
 * Representa una asignatura/curso de la malla curricular.
 * Incluye identificador (NRC/código), nombre, semestre, créditos, área y prerrequisitos en texto.
 *
 * <p>Se usa para construir la malla del estudiante, calcular créditos aprobados,
 * listar asignaturas asociadas a certificaciones y detectar ramos críticos.
 */

public class Curso {
	
	
	private String ncr;
	private String nombre;
	private int semestre;
	private int creditos;
	private String area;
	private String prerrequisitos;

	public Curso(String ncr, String nombre, int semestre, int creditos, String area, String prerrequisitos) {
		this.ncr = ncr;
		this.nombre = nombre;
		this.semestre = semestre;
		this.creditos = creditos;
		this.area = area;
		this.prerrequisitos = prerrequisitos;
	}

	public String getNcr() {
		return ncr;
	}

	public void setNcr(String ncr) {
		this.ncr = ncr;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getSemestre() {
		return semestre;
	}

	public void setSemestre(int semestre) {
		this.semestre = semestre;
	}

	public int getCreditos() {
		return creditos;
	}

	public void setCreditos(int creditos) {
		this.creditos = creditos;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPrerrequisitos() {
		return prerrequisitos;
	}

	public void setPrerrequisitos(String prerrequisitos) {
		this.prerrequisitos = prerrequisitos;
	}
	
	
}
