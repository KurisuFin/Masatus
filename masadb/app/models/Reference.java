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

    /**
     * Generoi viitettä vastaavan BibTeX-koodin.
     *
     * @return Koodi merkkijonona.
     */
    public String generateBibtexEntry() {
        StringBuilder sb = new StringBuilder();

        sb.append("@book{" + citeKey + ",\n");

        generateTag("title", title, sb);
        generateTag("author", author, sb);
        generateTag("publisher", publisher, sb);
        generateTag("year", year != null ? Integer.toString(year) : null, sb);
        generateTag("address", address, sb);
        generateTag("edition", edition, sb);
        generateTag("volume", volume != null ? Integer.toString(volume) : null, sb);

        sb.append("}");

        return sb.toString();
    }

    /**
     * Luo BibTex-koodin yksittäistä tagia varten.
     */
    private void generateTag(String name, String value, StringBuilder sb) {
        if (value != null && !value.isEmpty()) {
            sb.append(name);
            sb.append(" = {");
            sb.append(encodeString(value));
            sb.append("},\n");
        }
    }

    /**
     * Muuntaa erikoismerkit BibTeXin vaatimaan muotoon, ja akronyymit siten
     * ettei niitä muuteta automaattisesti pieniksi kirjaimiksi.
     */
    private String encodeString(String s) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < s.length(); ++i) {
            boolean capitalize = isPartOfAcronym(s, i);

            if(capitalize)
                sb.append('{');

            switch (s.charAt(i)) {
                case 'ä':
                    sb.append("\\\"{a}");
                    break;
                case 'Ä':
                    sb.append("\\\"{A}");
                    break;
                case 'ö':
                    sb.append("\\\"{o}");
                    break;
                case 'Ö':
                    sb.append("\\\"{O}");
                    break;
                case 'å':
                    sb.append("\\r{a}");
                    break;
                case 'Å':
                    sb.append("\\r{A}");
                    break;
                default:
                    sb.append(s.charAt(i));
                //TODO muut erikoismerkit
            }

            if(capitalize)
                sb.append('}');
        }

        return sb.toString();
    }

    private boolean isPartOfAcronym(String s, int idx) {
        if (Character.isUpperCase(s.charAt(idx))) {
            return idx > 0 && Character.isUpperCase(s.charAt(idx - 1))
                    || idx < s.length() - 1 && Character.isUpperCase(s.charAt(idx + 1));
        }

        return false;
    }
}
