package dominio;

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
