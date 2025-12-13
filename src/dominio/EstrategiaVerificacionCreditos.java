package dominio;
//Catalina Isidora Rojas Macaya - 21.953.080-3 - ICCI
//Benjamín Ismael Cortés Acuña - 21.890.703-2 - ICCI
/**
 * Estrategia concreta de verificación basada en créditos aprobados.
 * Calcula los créditos del estudiante considerando únicamente los cursos
 * asociados a una certificación y aprobados con nota >= 4.0, comparándolos
 * con los créditos requeridos por la certificación.
 */

import java.util.ArrayList;

import logica.EstrategiaVerificacion;

public class EstrategiaVerificacionCreditos implements EstrategiaVerificacion {

	private Sistema sistema;

	public EstrategiaVerificacionCreditos(Sistema sistema) {
		this.sistema = sistema;
	}

	@Override
	public boolean verificar(String rut, String idCertificacion) {

		// Buscar la certificacion
		Certificacion cert = sistema.buscarCertificacion(idCertificacion);
		if (cert == null)
			return false;

		int creditosRequeridos = cert.getCreditosRequeridos();

		// Obtener todos los NRC asociados a esa certificacion
		ArrayList<String> nrcAsociados = new ArrayList<>();
		for (AsignaturaCertificacion ac : sistema.getAsignaturasCertificacionesInternas()) {
			if (ac.getIdCertificacion().equals(idCertificacion)) {
				nrcAsociados.add(ac.getNrcCurso());
			}
		}

		// Si la certificacion no tiene ramos asociados, la dejamos pasar
		if (nrcAsociados.isEmpty())
			return true;

		// Recorrer las notas del estudiante y sumar créditos aprobados
		int creditosAprobados = 0;

		for (Nota n : sistema.getNotasInternas()) {

			// Solo notas del estudiantes
			if (!n.getRutEstudiante().equals(rut)) {
				continue;
			}

			// Solo ramos que pertenecen a la certificación
			if (!nrcAsociados.contains(n.getCodigoAsignatura())) {
				continue;
			}

			// Solo si está aprobado
			if (n.getCalificacion() >= 4.0) {
				Curso c = buscarCursoPorNrcLocal(n.getCodigoAsignatura());
				if (c != null) {
					creditosAprobados += c.getCreditos();
				}
			}
		}
		// Comparar contra todos los créditos requeridos
		return creditosAprobados >= creditosRequeridos;
	}

	// Búsqueda local de curso usando los cursos que expone Sistema
	private Curso buscarCursoPorNrcLocal(String nrc) {
		for (Curso c : sistema.getCursosInternos()) {
			if (c.getNcr().equals(nrc)) {
				return c;
			}
		}
		return null;
	}
}
