package controllers;

import db.Database;
import models.Reference;
import play.data.Form;
import static play.data.Form.*;
import play.data.validation.Constraints.Max;
import play.data.validation.Constraints.Min;
import play.data.validation.Constraints.Required;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.add;

/**
 * Sovelluslogiikka viittausten lisäyssivua varten.
 */
public class AddReference extends Controller {

    /**
     * Tietue lomakkeeseen syötetyille tiedoille.
     */
    public static class UserInput {

        @Required public String title;
        @Required public String author;
        @Required public String publisher;
        @Required @Min(1) @Max(2099) public Integer year;
        @Required public String citeKey;
        public String address;
        public String edition;
        @Min(1) @Max(9999) public Integer volume;
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
        if (form.hasErrors()) {
            return badRequest(add.render(form));
        } else {
            UserInput input = form.get();

            Reference ref = new Reference(input.citeKey, input.title,
                    input.author, input.publisher, input.year);
            ref.address = input.address;
            ref.volume = input.volume;
            ref.edition = input.edition;

            Database.save(ref);

            return ReferenceList.show();
        }
    }
}
