package models;

import com.avaje.ebean.validation.NotNull;
import javax.persistence.*;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

/**
 * Tietue viitteen tietojen tallennukseen ORM:n avulla.
 */
@Entity
public class Reference extends Model {

    /**
     * Yksikäsitteinen automaattisesti generoitu tunniste.
     */
    @Id
    public Integer id;

    /**
     * Viitteen tyyppi (kirja/artikkeli jne). Pakollinen kenttä.
     */
    @NotNull @Constraints.Required
    public ReferenceType type;

    /**
     * Latex-dokumentissa käytettävä viiteavain (citation key).
     * Pakollinen kenttä.
     */
    @NotNull
    public String citeKey;

    /**
     * Nimi. Pakollinen kenttä.
     */
    @NotNull @Constraints.Required
    public String title;

    /**
     * Tekijän tai tekijöiden nimet. Pakollinen kenttä.
     */
    @NotNull @Constraints.Required
    public String author;

    /**
     * Julkaisuvuosi. Pakollinen kenttä.
     */
    @NotNull @Constraints.Required @Constraints.Min(1) @Constraints.Max(2099)
    public Integer year;

    /**
     * Julkaisukuukausi.
     */
    public String month;

    /**
     * Kirjan osa.
     */
    @Constraints.Min(1) @Constraints.Max(9999)
    public Integer volume;

    /**
     * Tiedejulkaisun, lehden tms numero.
     */
    @Constraints.Min(1) @Constraints.Max(9999)
    public Integer number;

    /**
     * Kirjan painos.
     */
    public String edition;

    /**
     * Sivunumerot muodossa "123--234".
     */
    public String pages;

    /**
     * Kirjan tai muun julkaisun nimi viitteille, jotka ovat osa
     * isompaa julkaisua.
     */
    public String bookTitle;

    /**
     * Julkaisija.
     */
    public String publisher;

    /**
     * Julkaisijan osoite.
     */
    public String address;

    /**
     * Organisaatio (esim. konferenssin järjestäjä).
     */
    public String organization;


    /**
     * Tietueen haku
     */
    public static Finder<Integer,Reference> find = new Finder<Integer,Reference>(Integer.class, Reference.class);

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

        sb.append("@" + type.toString().toLowerCase() + "{" + citeKey + ",\n");

        generateTag("title", title, sb);
        generateTag("author", author, sb);
        generateTag("year", year != null ? Integer.toString(year) : null, sb);
        generateTag("month", month, sb);
        generateTag("volume", volume != null ? Integer.toString(volume) : null, sb);
        generateTag("number", number != null ? Integer.toString(number) : null, sb);
        generateTag("edition", edition, sb);
        generateTag("pages", pages, sb);
        generateTag("booktitle", bookTitle, sb);
        generateTag("publisher", publisher, sb);
        generateTag("address", address, sb);
        generateTag("organization", organization, sb);

        sb.append("}");

        return sb.toString();
    }

    /**
     * Luo BibTex-koodin yksittäistä tagia varten.
     */
    private void generateTag(String name, String value, StringBuilder sb) {
        if (value != null && !value.isEmpty()) {
            sb.append("    ");
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

    public void setPages(String pages) {
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
