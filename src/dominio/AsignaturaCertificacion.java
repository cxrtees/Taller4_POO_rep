package dominio;
/**
 * Representa la relación entre una certificación y una asignatura (curso) requerida.
 * Vincula el id de la certificación con el NRC/código del curso asociado.
 *
 * <p>Se utiliza para determinar qué ramos pertenecen a una certificación, verificar
 * requisitos y calcular ramos faltantes.
 */

public class AsignaturaCertificacion {
	
	private String idCertificacion;
	private String nrcCurso;
	
	public AsignaturaCertificacion(String idCertificacion, String nrcCurso) {
		this.idCertificacion = idCertificacion;
		this.nrcCurso = nrcCurso;
	}

	public String getIdCertificacion() {
		return idCertificacion;
	}

	public void setIdCertificacion(String idCertificacion) {
		this.idCertificacion = idCertificacion;
	}

	public String getNrcCurso() {
		return nrcCurso;
	}

	public void setNrcCurso(String nrcCurso) {
		this.nrcCurso = nrcCurso;
	}
	
	
	
	
}
