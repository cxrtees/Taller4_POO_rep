package dominio;
//Catalina Isidora Rojas Macaya - 21.953.080-3 - ICCI
//Benjamín Ismael Cortés Acuña - 21.890.703-2 - ICCI
public class UsuarioFactory {
	
	// Crea un Usuario a partir de una línea del archivo usuarios.txt
	public static Usuario crearDesdeLinea(String linea) {
		String[] p = linea.split(";");
		
		String nombreUsuario = p[0].trim();
		String contraseña = p[1].trim();
		String rol = p[2].trim();
		String infoExtra = (p.length > 3) ? p[3].trim() : "";
		
		return new Usuario(nombreUsuario, contraseña, rol, infoExtra);
	}	
}
