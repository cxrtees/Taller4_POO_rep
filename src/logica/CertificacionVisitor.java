package logica;
//Catalina Isidora Rojas Macaya - 21.953.080-3 - ICCI
//Benjamín Ismael Cortés Acuña - 21.890.703-2 - ICCI
/**
 * Interfaz del patrón Visitor para aplicar acciones sobre una certificación
 * en el contexto del registro de un estudiante.
 * Se utiliza para generar texto/resultado para el dashboard u otras salidas
 * sin modificar la clase {@link dominio.Certificacion}.
 */

import dominio.Certificacion;
import dominio.RegistroCertificacion;

public interface CertificacionVisitor {
	/**
	 * Aplica una acción sobre una certificación y el registro
	 * de un estudiante, devolviendo un texto para el dashboard.
	 */
	String visitarCertificacion(Certificacion cert, RegistroCertificacion reg);
}
