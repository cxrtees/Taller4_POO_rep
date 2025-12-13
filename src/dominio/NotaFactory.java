package dominio;
//Catalina Isidora Rojas Macaya - 21.953.080-3 - ICCI
//Benjamín Ismael Cortés Acuña - 21.890.703-2 - ICCI
public class NotaFactory {
	// Crea una Nota a partir de una línea del archivo notas.txt
	/**
	 * Crea una instancia del objeto correspondiente a partir de una línea de texto
	 * leída desde un archivo de datos.
	 *
	 * @param linea línea del archivo con los datos separados por ';'
	 * @return objeto creado a partir de la línea
	 */
	public static Nota crearDesdeLinea(String linea) {
		String[] p = linea.split(";");
				
		String rut = p[0].trim();
		String codAsig = p[1].trim();
		double calificacion = Double.parseDouble(p[2].trim());
		String estado = p[3].trim();
		String semestre = p[4].trim();		
		
		return new Nota(rut, codAsig, calificacion, estado, semestre);
	}
}
