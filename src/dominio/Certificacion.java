package dominio;
//Catalina Isidora Rojas Macaya - 21.953.080-3 - ICCI
//Benjamín Ismael Cortés Acuña - 21.890.703-2 - ICCI
/**
 * Representa una línea de certificación disponible en el sistema.
 * Contiene información descriptiva y criterios generales como créditos requeridos
 * y años de validez.
 *
 * <p>Implementa el punto de extensión del patrón Visitor mediante el método {@code aceptar},
 * permitiendo aplicar acciones sobre la certificación sin modificar su estructura.
 */

import logica.CertificacionVisitor;

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
	
	public String aceptar(RegistroCertificacion reg, CertificacionVisitor visitor) {
		return visitor.visitarCertificacion(this, reg);
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
