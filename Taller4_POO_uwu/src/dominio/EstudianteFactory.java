package dominio;

public class EstudianteFactory {
	// Crea un Usuario a partir de una línea del archivo usuarios.txt
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
