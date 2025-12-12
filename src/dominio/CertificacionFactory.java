package dominio;
//Catalina Isidora Rojas Macaya - 21.953.080-3 - ICCI
//Benjamín Ismael Cortés Acuña - 21.890.703-2 - ICCI
public class CertificacionFactory {

	// Crea una Certificación a partir de una línea del archivo certificaciones.txt
	public static Certificacion crearDesdeLinea(String linea) {
		String[] p = linea.split(";");

		String id = p[0].trim();
		String nombre = p[1].trim();
		String descripcion = p[2].trim();
		int creditosReq = Integer.parseInt(p[3].trim());
		int añosValidez = Integer.parseInt(p[4].trim());

		return new Certificacion(id, nombre, descripcion, creditosReq, añosValidez);
	}

}
