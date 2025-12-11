package dominio;

public class CursoFactory {
	
	// Crea un Curso a partir de una línea del archivo cursos.txt
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
