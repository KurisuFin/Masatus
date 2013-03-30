
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
     * Generoi sivun sisällön.
     * @return Sivun sisältö.
     */
    public static Result show() {
        List<Reference> refs = Database.findAll();
        return ok(list.render(refs));
    }
}
