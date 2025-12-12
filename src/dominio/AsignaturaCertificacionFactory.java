package dominio;
//Catalina Isidora Rojas Macaya - 21.953.080-3 - ICCI
//Benjamín Ismael Cortés Acuña - 21.890.703-2 - ICCI
public class AsignaturaCertificacionFactory {
	// Crea una Asignatura Certificacion a partir de una línea del archivo asignaturas_certificaciones.txt
	public static AsignaturaCertificacion crearDesdeLinea(String linea) {
		String[] p = linea.split(";");
				
		String idCert = p[0].trim();
		String nrc = p[1].trim();	
		
		return new AsignaturaCertificacion(idCert, nrc);
	}	
	
}
