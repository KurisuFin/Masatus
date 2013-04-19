package controllers;

import db.Database;
import java.util.List;
import models.Reference;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.list;

/**
 * Sovelluslogiikka viitelistaa varten.
 */
public class ReferenceList extends Controller {

    /**
     * Generoi sivun joka näyttää listan viitteistä. Jos query string sisältää
     * kentän "filter" niin listaa rajoitetaan annetun tekijän mukaan.
     *
     * @return Sivun sisältö.
     */
    public static Result show() {
        List<Reference> refs;

        String filter = request().getQueryString("filter");
        if (filter == null) {
            refs = Database.findAll();
        } else {
            refs = Database.findByAuthor(filter);
        }

        return ok(list.render(refs));
    }
}
