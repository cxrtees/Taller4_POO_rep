package dominio;

public class RegistroCertificacion {

	private String rutEstudiante;
	private String idCertificacion;
	private String fechaRegistro;
	private String estado;
	private double progreso;

	public RegistroCertificacion(String rutEstudiante, String idCertificacion, String fechaRegistro, String estado,
			double progreso) {
		this.rutEstudiante = rutEstudiante;
		this.idCertificacion = idCertificacion;
		this.fechaRegistro = fechaRegistro;
		this.estado = estado;
		this.progreso = progreso;
	}

	public String getRutEstudiante() {
		return rutEstudiante;
	}

	public void setRutEstudiante(String rutEstudiante) {
		this.rutEstudiante = rutEstudiante;
	}

	public String getIdCertificacion() {
		return idCertificacion;
	}

	public void setIdCertificacion(String idCertificacion) {
		this.idCertificacion = idCertificacion;
	}

	public String getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public double getProgreso() {
		return progreso;
	}

	public void setProgreso(double progreso) {
		this.progreso = progreso;
	}

}
