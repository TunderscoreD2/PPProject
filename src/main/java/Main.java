import de.daniel.barista.Barista;
import de.daniel.card.Menu;

import java.util.logging.Level;

public class Main {


    public static void main(String[] args) {
        //Silence Hibernate, copy from stackoverflow
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        Menu menu = new Menu();
        Barista barista = new Barista(menu);

        barista.handleCustomer();

        menu.terminateConnection();

    }


}
