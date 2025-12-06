package dominio;

public class Certificacion {
	
	private String id;
	private String nombre;
	private String descripcion;
	private int creditosRequeridos;
	private int añosValidez;
	
	public Certificacion(String id, String nombre, String descripcion, int creditosRequeridos, int añosValidez) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.creditosRequeridos = creditosRequeridos;
		this.añosValidez = añosValidez;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getCreditosRequeridos() {
		return creditosRequeridos;
	}

	public void setCreditosRequeridos(int creditosRequeridos) {
		this.creditosRequeridos = creditosRequeridos;
	}

	public int getAñosValidez() {
		return añosValidez;
	}

	public void setAñosValidez(int añosValidez) {
		this.añosValidez = añosValidez;
	}
	
	
	
	
}
