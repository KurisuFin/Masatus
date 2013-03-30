package models;

import com.avaje.ebean.validation.NotNull;
import javax.persistence.*;
import play.db.ebean.*;

/**
 * Tietue kirjan (tai muun viitteen) tietojen tallennukseen ORM:n avulla.
 */
@Entity
public class Reference extends Model {

    /**
     * Yksikäsitteinen automaattisesti generoitu tunniste.
     */
    @Id
    private Integer id;

    /**
     * Latex-dokumentissa käytettävä "sitaattiavain" (citation key).
     * Pakollinen kenttä.
     * TODO: Pitäisi olla yksikäsitteinen, ja olisi hyvä generoida
     * automaattisesti käyttäjän puolesta.
     */
    @NotNull
    public String citeKey;

    /**
     * Julkaisijan osoite. Valinnainen kenttä.
     */
    public String address;

    /**
     * Tekijän tai tekijöiden nimet. Pakollinen kenttä.
     */
    @NotNull
    public String author;

    /**
     * Kirjan painos. Valinnainen kenttä.
     */
    public String edition;

    /**
     * Julkaisuvuosi. Pakollinen kenttä.
     */
    @NotNull
    public Integer year;

    /**
     * Nimi. Pakollinen kenttä.
     */
    @NotNull
    public String title;

    /**
     * Julkaisija. Valinnainen kenttä.
     */
    @NotNull
    public String publisher;

    /**
     * Kirjan osa. Valinnainen kenttä.
     */
    public Integer volume;

    /**
     * Luo uuden viite-objektin pakollisista tiedoista.
     * @param citeKey Sitaattiavain.
     * @param title Nimi.
     * @param author Tekijä(t).
     * @param publisher Julkaisija.
     * @param year Vuosi.
     */
    public Reference(String citeKey, String title, String author, String publisher,
            int year) {
        this.citeKey = citeKey;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
    }

    /**
     * Palauttaa automaattisesti generoidun tunnisteen.
     * @return Tunnisteen arvo tai null, jos tietuetta ei ole tallennettu.
     */
    public Integer getId() {
        return id;
    }
}
