package models;

import com.avaje.ebean.validation.NotNull;
import javax.persistence.*;
import play.db.ebean.*;

/**
 * Tietue viitteen tietojen tallennukseen ORM:n avulla.
 */
@Entity
public class Reference extends Model {

    /**
     * Yksikäsitteinen automaattisesti generoitu tunniste.
     */
    @Id
    private Integer id;

    /**
     * Yksikäsitteinen automaattisesti generoitu tunniste. Pakollinen
     * kenttä.
     */
    @NotNull
    private ReferenceType type;

    /**
     * Latex-dokumentissa käytettävä viiteavain (citation key).
     * Pakollinen kenttä.
     * TODO: Pitäisi olla yksikäsitteinen, ja olisi hyvä generoida
     * automaattisesti käyttäjän puolesta.
     */
    @NotNull
    private String citeKey;

    /**
     * Nimi. Pakollinen kenttä.
     */
    @NotNull
    private String title;

    /**
     * Tekijän tai tekijöiden nimet. Pakollinen kenttä.
     */
    @NotNull
    private String author;

    /**
     * Julkaisuvuosi. Pakollinen kenttä.
     */
    @NotNull
    private Integer year;

    /**
     * Julkaisukuukausi.
     */
    private String month;

    /**
     * Kirjan osa.
     */
    private Integer volume;

    /**
     * Tiedejulkaisun, lehden tms numero.
     */
    private Integer number;

    /**
     * Kirjan painos.
     */
    private String edition;

    /**
     * Sivunumerot muodossa "123--234".
     */
    private String pages;

    /**
     * Kirjan tai muun julkaisun nimi viitteille, jotka ovat osa
     * isompaa julkaisua.
     */
    private String bookTitle;

    /**
     * Julkaisija.
     */
    private String publisher;

    /**
     * Julkaisijan osoite.
     */
    private String address;

    /**
     * Organisaatio (esim. konferenssin järjestäjä).
     */
    private String organization;

    /**
     * Default-konstruktori.
     */
    public Reference() {
    }

    /**
     * Luo uuden viite-objektin pakollisista tiedoista.
     *
     * @param type Viitteen tyyppi.
     * @param citeKey Viiteavain.
     * @param title Nimi.
     * @param author Tekijä(t).
     * @param year Vuosi.
     */
    public Reference(ReferenceType type, String citeKey, String title,
            String author, int year) {
        setType(type);
        setCiteKey(citeKey);
        setTitle(title);
        setAuthor(author);
        setYear(year);
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

    public ReferenceType getType() {
        return type;
    }

    public void setType(ReferenceType type) {
        this.type = type;
    }

    public String getCiteKey() {
        return citeKey;
    }

    public void setCiteKey(String citeKey) {
        this.citeKey = citeKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String bookTitle) {
        this.pages = pages;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

}
