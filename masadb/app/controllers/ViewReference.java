package controllers;

import db.Database;
import models.Reference;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import views.html.main;
import views.html.view;

/**
 * Sovelluslogiikka viitteiden tarkastelua varten.
 */
public class ViewReference extends Controller {

    /**
     * Generoi sivun sisällön.
     *
     * @return Sivun sisältö.
     */
    public static Result show() {
        int id;
        try {
            id = Integer.parseInt(Controller.request().getQueryString("id"));
        } catch (NumberFormatException e) {
            return badRequest("Virheellinen viitetunnus.");
        }

        Reference ref = Database.find(id);
        if (ref == null) {
            return badRequest("Viitettä ei löytynyt.");
        }
        return ok(view.render(ref));
    }
}
