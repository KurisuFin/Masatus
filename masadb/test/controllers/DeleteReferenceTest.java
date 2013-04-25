package controllers;

import db.Database;
import models.Reference;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import static org.fest.assertions.Assertions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;
import play.test.Helpers;
import static play.test.Helpers.*;
import static org.junit.Assert.*;


public class DeleteReferenceTest {
    FakeApplication fa;
    Map<String, String> input;
    Map<String, String> modifiedinput;

    @Before
    public void setUp() {
        fa = fakeApplication(inMemoryDatabase());
        start(fa);

        input = new TreeMap<String, String>();
        input.put("type", "Book");
        input.put("title", "Asd Fasdf Asf Asf");
        input.put("author", "A AD Dwe Da A");
        input.put("year", "2099");
        input.put("month", "jan");
        input.put("volume", "9999");
        input.put("number", "9999");
        input.put("edition", "3rd");
        input.put("pages", "123--321");
        input.put("bookTitle", "Asd Sdas Sasafd");
        input.put("publisher", "A Aasdaf Af");
        input.put("address", "Street 123 012354 City");
        input.put("organization", "A Sdasf Sas Asad");

        // Muokattu data
        modifiedinput = new TreeMap<String, String>();
        modifiedinput.put("type", "Article");
        modifiedinput.put("title", "Asd Fasdf Asf Asf2");
        modifiedinput.put("author", "A AD Dwe Da A2");
        modifiedinput.put("year", "2033");
        modifiedinput.put("month", "feb");
        modifiedinput.put("volume", "9998");
        modifiedinput.put("number", "9998");
        modifiedinput.put("edition", "2rd");
        modifiedinput.put("pages", "123--333");
        modifiedinput.put("bookTitle", "Asd Sdas Sasafd2");
        modifiedinput.put("publisher", "A Aasdaf Af2");
        modifiedinput.put("address", "Street 123 012354 City2");
        modifiedinput.put("organization", "A Sdasf Sas Asad2");

    }

    @After
    public void tearDown() {
        stop(fa);
    }

    Result postTestInput(Map<String, String> input) {
        FakeRequest fr = fakeRequest(POST, "/add").withFormUrlEncodedBody(input);
        return callAction(controllers.routes.ref.ModifyReference.save(), fr);
    }

    Result deleteTestInput(String id) {
        return deleteTestInput(Integer.parseInt(id));
    }

    Result deleteTestInput(int id) {
        FakeRequest fr = fakeRequest(GET, "/delete/"+id);
        return callAction(controllers.routes.ref.DeleteReference.delete(id), fr);
    }

    @Test
    public void databaseContainsAllAddedReferences() {
        status(postTestInput(input));
        status(postTestInput(input));
        status(postTestInput(input));

        List<Reference> refs = Database.findAll();
        assertThat(refs.size()).isEqualTo(3);
    }

    @Test
    public void databaseDoesntContainDeletedReference() {
        status(postTestInput(input));
        status(postTestInput(input));
        status(postTestInput(input));

        status(deleteTestInput(2));

        List<Reference> refs = Database.findAll();
        for (Reference ref : refs)
            assertThat(ref.getId()).isNotEqualTo(2);
    }

    @Test
    public void databaseIsEmptyAfterDeletingAllReferences() {
        for (int i=0; i<10; ++i)
            status(postTestInput(input));

        for (int i=1; i<=10; ++i)
            status(deleteTestInput(i));

        List<Reference> refs = Database.findAll();
        assertThat(refs.size()).isEqualTo(0);
    }
}
