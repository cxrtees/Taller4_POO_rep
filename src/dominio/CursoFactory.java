package dominio;
//Catalina Isidora Rojas Macaya - 21.953.080-3 - ICCI
//Benjamín Ismael Cortés Acuña - 21.890.703-2 - ICCI
public class CursoFactory {
	
	// Crea un Curso a partir de una línea del archivo cursos.txt
	/**
	 * Crea una instancia del objeto correspondiente a partir de una línea de texto
	 * leída desde un archivo de datos.
	 *
	 * @param linea línea del archivo con los datos separados por ';'
	 * @return objeto creado a partir de la línea
	 */
	public static Curso crearDesdeLinea(String linea) {
		String[] p = linea.split(";");
			
		String ncr = p[0].trim();
		String nombre = p[1].trim();
		int semestre =Integer.parseInt(p[2].trim()); 
		int creditos =Integer.parseInt(p[3].trim()); 
		String area = p[4].trim();
		String prereqTexto = (p.length > 5) ? p[5].trim() : "";
			
		return new Curso(ncr, nombre, semestre, creditos, area, prereqTexto);
	}	
}
