package dominio;

public class Usuario {
	
	private String nombreUsuario;
	private String contraseña;
	private String rol;
	private String area;
	
	public Usuario(String nombreUsuario, String contraseña, String rol, String area) {
		this.nombreUsuario = nombreUsuario;
		this.contraseña = contraseña;
		this.rol = rol;
		this.area = area;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	
	
	
}
