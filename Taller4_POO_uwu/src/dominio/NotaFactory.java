package dominio;

public class NotaFactory {
	// Crea una Nota a partir de una línea del archivo notas.txt
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
