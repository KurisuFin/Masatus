package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.view;

/**
 * Sovelluslogiikka viitteiden tarkastelua varten.
 */
public class ViewReference extends Controller {

    /**
     * Generoi sivun sisällön.
     * @return Sivun sisältö.
     */
    public static Result show() {
        return ok(view.render());
    }

}
