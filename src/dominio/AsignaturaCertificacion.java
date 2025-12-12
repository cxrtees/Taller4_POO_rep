package dominio;

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
