package logica;
//Catalina Isidora Rojas Macaya - 21.953.080-3 - ICCI
//Benjamín Ismael Cortés Acuña - 21.890.703-2 - ICCI
import dominio.Certificacion;
import dominio.RegistroCertificacion;

public interface CertificacionVisitor {
	/**
	 * Aplica una acción sobre una certificación y el registro
	 * de un estudiante, devolviendo un texto para el dashboard.
	 */
	String visitarCertificacion(Certificacion cert, RegistroCertificacion reg);
}
