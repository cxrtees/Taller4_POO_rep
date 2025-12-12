package dominio;
//Catalina Isidora Rojas Macaya - 21.953.080-3 - ICCI
//Benjamín Ismael Cortés Acuña - 21.890.703-2 - ICCI
public class RegistroCertificacionFactory {
	// Crea un registro a partir de una línea del archivo registros.txt
	public static RegistroCertificacion crearDesdeLinea(String linea) {
		String[] p = linea.split(";");
				
		String rut = p[0].trim();
		String idCert = p[1].trim();
		String fechaRegistro = p[2].trim();
		String estado = p[3].trim();
		double progreso = Double.parseDouble(p[4].trim());
		
		return new RegistroCertificacion(rut, idCert, fechaRegistro, estado, progreso);
	}	
}
