package logica;
//Catalina Isidora Rojas Macaya - 21.953.080-3 - ICCI
//Benjamín Ismael Cortés Acuña - 21.890.703-2 - ICCI
/**
 * Interfaz del patrón Strategy para verificación de requisitos.
 * Permite definir distintas formas de validar si un estudiante cumple
 * las condiciones para inscribir o avanzar en una certificación.
 */

public interface EstrategiaVerificacion {
	boolean verificar(String rut, String idCertificacion);
}
