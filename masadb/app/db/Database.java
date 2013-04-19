package db;

import com.avaje.ebean.Ebean;
import java.util.List;
import models.Reference;

/**
 * Yksinkertainen wrapper-luokka Ebeanin operaatioille.
 */
public class Database {

    /**
     * Tallennetaan uusi viite kantaan.
     * @param entry Viite-olio.
     */
    public static void save(Reference entry) {
        Ebean.save(entry);
    }

    /**
     * Etsii yksittäisen viitten tunnisteen perusteella.
     * @return Viiteobjekti.
     */
    public static Reference find(int id) {
        return Ebean.find(Reference.class, id);
    }

    /**
     * Palauttaa kaikki viitteet listana. TODO: Voisi kehittää niin, että
     * setFirstRow/setMaxRows:n avulla palautetaan vain osa, jolloin lista
     * voidaan jakaa useaan sivuun.
     * @return Lista viitteistä.
     */
    public static List<Reference> findAll() {
        return Ebean.find(Reference.class).findList();
    }

    /**
     * Poistaa annetulla id:llä olevan viitteen.
     * @param id Viitteen sisäinen tunniste.
     * @return True jos viite poistettiin, false jos ei löytynyt.
     */
    public static boolean delete(int id) {
        return Ebean.delete(Reference.class, id) != 0;
    }

    /**
     * Hakee kaikki viitteet joiden tekijät sisältävät annetun
     * merkkijonon.
     * @param author Haettava merkkijono.
     * @return Lista löydetyistä viitteistä.
     */
    public static List<Reference> findByAuthor(String author) {
        return Ebean.find(Reference.class).where().icontains("author", author).findList();
    }
}
