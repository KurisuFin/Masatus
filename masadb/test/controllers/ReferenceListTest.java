package controllers;

import db.Database;
import models.Reference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import play.test.FakeApplication;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class ReferenceListTest {

    FakeApplication fa;

    @Before
    public void setUp() {
        fa = fakeApplication(inMemoryDatabase());
        start(fa);
    }

    @After
    public void tearDown() {
        stop(fa);
    }

    @Test
    public void pageTypeIsCorrect() {
        Result result = callAction(controllers.routes.ref.ReferenceList.show(), fakeRequest());
        assertThat(contentType(result)).isEqualTo("text/html");
        assertThat(status(result)).isEqualTo(OK);
    }

    @Test
    public void showMessageWhenDatabaseEmpty() {
        Result result = callAction(controllers.routes.ref.ReferenceList.show(), fakeRequest());
        assertThat(contentAsString(result)).contains("Tietokantaan ei ole lisätty vielä yhtään viitettä.");
    }

    @Test
    public void listContainsDatabaseEntries() {
        Database.save(new Reference("a", "title 1", "author 1", "publ 1", 1001));
        Database.save(new Reference("b", "title 2", "author 2", "publ 2", 1002));

        Result result = callAction(controllers.routes.ref.ReferenceList.show(), fakeRequest());

        assertThat(contentAsString(result)).contains("title 1");
        assertThat(contentAsString(result)).contains("author 1");
        assertThat(contentAsString(result)).contains("1001");

        assertThat(contentAsString(result)).contains("title 2");
        assertThat(contentAsString(result)).contains("author 2");
        assertThat(contentAsString(result)).contains("1002");
    }

    @Test
    public void listContainsLinks() {
        Reference ref1 = new Reference("a", "title 1", "author 1", "publ 1", 1001);
        Reference ref2 = new Reference("b", "title 2", "author 2", "publ 2", 1002);
        Database.save(ref1);
        Database.save(ref2);

        Result result = callAction(controllers.routes.ref.ReferenceList.show(), fakeRequest());

        assertThat(contentAsString(result)).contains("href=\"view?id=" + ref1.getId() + "\"");
        assertThat(contentAsString(result)).contains("href=\"view?id=" + ref2.getId() + "\"");
    }
}
