package dominio;

import java.util.ArrayList;

public abstract class Documento {
	
	private int id;
	private String titulo;
	private ArrayList<String> autores;
	private int añoPublicacion;
	
	public Documento(int id, String titulo, ArrayList<String> autores, int añoPublicacion) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.autores = autores;
		this.añoPublicacion = añoPublicacion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public ArrayList<String> getAutores() {
		return autores;
	}

	public void setAutores(ArrayList<String> autores) {
		this.autores = autores;
	}

	public int getAñoPublicacion() {
		return añoPublicacion;
	}

	public void setAñoPublicacion(int añoPublicacion) {
		this.añoPublicacion = añoPublicacion;
	}
	
	
	
}
