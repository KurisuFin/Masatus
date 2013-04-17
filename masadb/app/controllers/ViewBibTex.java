package controllers;

import db.Database;
import java.lang.StringBuffer;
import models.Reference;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import views.html.main;
import views.html.view;
import java.util.List;
import java.util.Iterator;
import java.lang.StringBuffer;

/**
 * Generoi tekstimuotoisen bibtext listan ja tuottaa sen käyttäjälle tiedostona
 */
public class ViewBibTex extends Controller {

    /**
     *
     *
     * @return Bibtext file.
     */
    public static Result show() {
        /* Asetetaan headeri tiedoston lataukselle */
        response().setHeader("Content-Disposition", "attachment; filename=masadb.bib");

        List<Reference> refs = Database.findAll();
        StringBuffer buf = new StringBuffer(1024*refs.size());
        Iterator<Reference> iterator = refs.iterator();
        while (iterator.hasNext()) {
            buf.append(iterator.next().generateBibtexEntry() + "\n\n");
        }

        return ok(buf.toString());
    }
}
