package logica;

import dominio.Certificacion;
import dominio.RegistroCertificacion;

public interface CertificacionVisitor {
	/**
	 * Aplica una acción sobre una certificación y el registro
	 * de un estudiante, devolviendo un texto para el dashboard.
	 */
	String visitarCertificacion(Certificacion cert, RegistroCertificacion reg);
}
