package controllers;

import com.avaje.ebean.Ebean;
import java.util.List;
import models.Reference;

public class Database {

    public static void save(Reference entry) {
        Ebean.save(entry);
    }

    public static List<Reference> findAll() {
        return Ebean.find(Reference.class).findList();
    }

    public static boolean delete(int id) {
        return Ebean.delete(Reference.class, id) != 0;
    }
}
