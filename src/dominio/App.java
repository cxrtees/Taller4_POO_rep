package dominio;
// Catalina Isidora Rojas Macaya - 21.953.080-3 - ICCI
// Benjamín Ismael Cortés Acuña - 21.890.703-2 - ICCI
import GUI.Menu;
import dominio.Sistema;
import logica.SistemaIn;
/**
 * Punto de entrada de la aplicación.
 * Inicializa el sistema (lógica/dominio) y lanza la interfaz gráfica principal.
 */

public class App {

    public static void main(String[] args) {
        // Creamos el sistema (lógica/dominio)
        SistemaIn sistema = Sistema.getInstancia();

        // Creamos el menú (GUI) y lo iniciamos
        Menu menu = new Menu(sistema);
        menu.iniciar();
    }
}
