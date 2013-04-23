package controllers;

import db.Database;
import models.Reference;
import play.mvc.Controller;
import play.mvc.Result;

public class DeleteReference extends Controller {

	public static Result delete(Integer id) {
		Database.delete(id);
		return redirect(routes.ReferenceList.show());
	}
}