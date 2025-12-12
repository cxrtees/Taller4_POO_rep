package dominio;
//Catalina Isidora Rojas Macaya - 21.953.080-3 - ICCI
//Benjamín Ismael Cortés Acuña - 21.890.703-2 - ICCI
public class EstudianteFactory {
	
	// Crea un Estudiante a partir de una línea del archivo estudiantes.txt
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
