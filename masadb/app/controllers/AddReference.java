package controllers;

import db.Database;
import models.Reference;
import models.ReferenceType;
import play.data.Form;
import static play.data.Form.*;
import play.data.validation.Constraints.Max;
import play.data.validation.Constraints.Min;
import play.data.validation.Constraints.Required;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.add;
import java.util.ArrayList;

/**
 * Sovelluslogiikka viittausten lisäyssivua varten.
 */
public class AddReference extends Controller {

    /**
     * Tietue lomakkeeseen syötetyille tiedoille.
     */
    public static class UserInput {
        @Required public ReferenceType type;
        @Required public String citeKey;
        @Required public String title;
        @Required public String author;
        @Required @Min(1) @Max(2099) public Integer year;
        public String month;
        @Min(1) @Max(9999) public Integer volume;
        @Min(1) @Max(9999) public Integer number;
        public String edition;
        public String pages;
        public String bookTitle;
        public String publisher;
        public String address;
        public String organization;
    }

    /**
     * Generoi sivun sisällön.
     *
     * @return Sivun sisältö.
     */
    public static Result show() {
        return ok(add.render(form(UserInput.class)));
    }

    /**
     * Tallentaa lomakkeen tiedot. Jos lomakkeessa on virhe, näytetään se
     * uudestaan virheilmoitusten kanssa, muuten tallennetaan tiedot
     * tietokantaan ja siirrytään takaisin aloitussivulle.
     *
     * @return Sivun sisältö.
     */
    public static Result save() {
        Form<UserInput> form = form(UserInput.class).bindFromRequest();
        if (formHasErrors(form)) {
            return badRequest(add.render(form));
        } else {
            UserInput input = form.get();

            Reference ref = new Reference(input.type, generoiViite(input.author, input.year),
                    input.title, input.author, input.year);
            ref.setMonth(input.month);
            ref.setVolume(input.volume);
            ref.setNumber(input.number);
            ref.setEdition(input.edition);
            ref.setPages(input.pages);
            ref.setBookTitle(input.bookTitle);
            ref.setPublisher(input.publisher);
            ref.setAddress(input.address);
            ref.setOrganization(input.organization);

            Database.save(ref);

            return redirect(routes.ReferenceList.show());
        }
    }

    /**
     * Tarkistaa onko lomakkeessa virheellisiä kenttiä.
     *
     * @param form lomakkeen tiedot
     * @return true jos lomakkeessa on virheitä
     */
    private static boolean formHasErrors(Form<UserInput> form) {
        if (form.hasErrors()) {
            return true;
        }

        if (!form.get().citeKey.matches("[a-zA-Z][a-zA-Z0-9_-]*")) {
            form.reject("citeKey", "Voi sisältää vain merkkejä"
                    + " \"a-z\", \"A-Z\", \"0-9\", \"-\", ja \"_\".");
            return true;
        }

        if (!form.get().month.isEmpty() && !form.get().month.matches(
                "(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)")) {
            form.reject("month", "Anna kuukausi muodossa \"jan\", \"feb\", jne.");
            return true;
        }

        if (!form.get().pages.isEmpty() && !form.get().pages.matches(
                "[1-9][0-9]*--[1-9][0-9]*")) {
            form.reject("month", "Anna sivut muodossa 123--321.");
            return true;
        }

        return false;
    }

    /**
     * Generoi viiteavaimen sukunimien ja vuosiluvun perusteella.
     *
     * @return viiteavain.
     */
    public static String generoiViite(String authorString, int year)
    {
        String viite = "";
        String separator = " and ";
        ArrayList<String> authorList = new ArrayList<String>();

        // Erotellaan nimet listaan, jos enemman kuin yksi tekija.
        while (authorString.contains(separator)) {
            authorList.add(authorString.substring(0, authorString.indexOf(separator)));
            authorString = authorString.substring(authorString.indexOf(separator)+separator.length());
        }

        // Lisataan viimeinen/ainoa tekija listaan.
        authorList.add(authorString);

        // Lisataan viitteeseen jokaisen sukunimen ensimmainen kirjain.
        for (int i = 0; i < authorList.size(); i++) {
            String nimi = authorList.get(i);

            // Sukunimi, Etunimi ("Kekkonen, Urho Kaleva")
            if (nimi.contains(","))	{
                // Useampiosaiset sukunimet ("van Gogh, Vincent")
                if (nimi.substring(0, nimi.indexOf(",")).contains(" ")) {
                    char merkki = Character.toLowerCase(nimi.charAt(0));
                    if (merkki == 'ä') merkki = 'a';
                    if (merkki == 'ö') merkki = 'o';
                    viite += merkki;

                    merkki = nimi.charAt(nimi.indexOf(" ")+1);
                    if (merkki == 'Ä') merkki = 'A';
                    if (merkki == 'Ö') merkki = 'O';
                    viite += merkki;
                }
                // Yksiosaiset sukunimet ("Kekkonen, Urho Kaleva")
                else
                {
                    char merkki = nimi.charAt(0);
                    if (merkki == 'Ä') merkki = 'A';
                    if (merkki == 'Ö') merkki = 'O';
                    viite += merkki;
                }
            }
            // Etunimi Sukunimi ("Urho Kekkonen")
            else
            {
                char merkki = nimi.charAt(nimi.lastIndexOf(" ")+1);
                if (merkki == 'Ä') merkki = 'A';
                if (merkki == 'Ö') merkki = 'O';
                viite += merkki;
            }
        }

        // Vain 4 ensimmaista kirjainta mahtuu viitteeseen.
        if (viite.length() > 4)
        viite = viite.substring(0,4);

        // Vuosiluvun kaksi viimeista numeroa viitteen loppuun.
        viite += Integer.toString(year).substring(2,4);

        return viite;
    }
}

