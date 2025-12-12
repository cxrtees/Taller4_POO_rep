package dominio;

import GUI.Menu;
import dominio.Sistema;
import logica.SistemaIn;

public class App {

    public static void main(String[] args) {
        // Creamos el sistema (lógica/dominio)
        SistemaIn sistema = Sistema.getInstancia();

        // Creamos el menú (GUI) y lo iniciamos
        Menu menu = new Menu(sistema);
        menu.iniciar();
    }
}
