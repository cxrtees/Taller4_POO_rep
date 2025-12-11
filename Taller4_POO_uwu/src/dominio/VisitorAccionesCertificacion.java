package dominio;

import logica.CertificacionVisitor;


public class VisitorAccionesCertificacion implements CertificacionVisitor{
	
	@Override
	public String visitarCertificacion(Certificacion cert, RegistroCertificacion reg) {
		
		int creditos = cert.getCreditosRequeridos();
		
		// "Tipo" de certificación según créditos requeridos
		String tipo;
		if (creditos < 20) {
			tipo = "Básica";
		} else if (creditos < 40) {
			tipo = "Intermedia";
		} else {
			tipo = "Avanzada";
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("- ").append(cert.getNombre())
		.append(" (").append(tipo).append(")\n")
		.append(" Estado: ").append(reg.getEstado())
		.append(" | Progreso: ").append(reg.getProgreso()).append("%\n");
		
		// Acciones distintas según tipo de certificación
		switch (tipo) {
			case "Básica" :
				if (reg.getProgreso() >= 100) {
					sb.append("  >> Recomendación: puedes avanzar a certificaciones más avanzadas.\n");
				}
				break;
			case "Intermedia" :
				if (reg.getProgreso() < 50) {
					sb.append("  >> Recomendación: refuerza los ramos base antes de seguir.\n");
				}
				break;
			case "Avanzada" :
				if (reg.getProgreso() < 100) {
					sb.append("  >> Recomendación: conversa con tu coordinador para planificar mejor la carga.\n");
				}
				break;
		}
		
		sb.append("\n");
		
		return sb.toString();
	}	
}
