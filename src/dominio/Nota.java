package dominio;

/**
 * Representa una calificación asociada a un estudiante en una asignatura.
 * Guarda el rut del estudiante, el código/NRC de la asignatura, la calificación,
 * el estado (aprobada/reprobada/cursando/etc.) y el semestre cursado.
 *
 * <p>Permite calcular promedios, determinar asignaturas aprobadas y verificar
 * requisitos académicos para certificaciones.
 */

public class Nota {

	private String rutEstudiante;
	private String codigoAsignatura;
	private double calificacion;
	private String estado;
	private String semestre;

	public Nota(String rutEstudiante, String codigoAsignatura, double calificacion, String estado, String semestre) {
		this.rutEstudiante = rutEstudiante;
		this.codigoAsignatura = codigoAsignatura;
		this.calificacion = calificacion;
		this.estado = estado;
		this.semestre = semestre;
	}

	public String getRutEstudiante() {
		return rutEstudiante;
	}

	public void setRutEstudiante(String rutEstudiante) {
		this.rutEstudiante = rutEstudiante;
	}

	public String getCodigoAsignatura() {
		return codigoAsignatura;
	}

	public void setCodigoAsignatura(String codigoAsignatura) {
		this.codigoAsignatura = codigoAsignatura;
	}

	public double getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(double calificacion) {
		this.calificacion = calificacion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

}
