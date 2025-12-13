package dominio;
//Catalina Isidora Rojas Macaya - 21.953.080-3 - ICCI
//Benjamín Ismael Cortés Acuña - 21.890.703-2 - ICCI
public class EstudianteFactory {
	
	// Crea un Estudiante a partir de una línea del archivo estudiantes.txt
	/**
	 * Crea una instancia del objeto correspondiente a partir de una línea de texto
	 * leída desde un archivo de datos.
	 *
	 * @param linea línea del archivo con los datos separados por ';'
	 * @return objeto creado a partir de la línea
	 */
	public static Estudiante crearDesdeLinea(String linea) {
		String[] p = linea.split(";");
			
		String rut = p[0].trim();
		String nombre = p[1].trim();
		String carrera = p[2].trim();
		int semestre = Integer.parseInt(p[3].trim());
		String correo = p[4].trim();
		String contraseña = p[5].trim();		
			
		return new Estudiante(rut, nombre, carrera, semestre, correo, contraseña);
	}	
}
