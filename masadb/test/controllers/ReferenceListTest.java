package controllers;

import db.Database;
import java.util.Map;
import java.util.TreeMap;
import models.Reference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import play.test.FakeApplication;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import static models.ReferenceType.*;
import play.test.FakeRequest;

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

    Result request(String filter) {
        String query = filter != null ? "/?filter=" + filter : "";
        FakeRequest fr = fakeRequest(GET, query);
        return callAction(controllers.routes.ref.ReferenceList.show(), fr);
    }

    @Test
    public void pageTypeIsCorrect() {
        Result result = request(null);
        assertThat(contentType(result)).isEqualTo("text/html");
        assertThat(status(result)).isEqualTo(OK);
    }

    @Test
    public void showMessageWhenDatabaseEmpty() {
        Result result = request(null);
        assertThat(contentAsString(result)).contains("Haulla ei löytynyt yhtään viitettä.");
    }

    @Test
    public void listContainsDatabaseEntries() {
        Database.save(new Reference(Book, "a", "title 1", "author 1", 1001));
        Database.save(new Reference(Book, "b", "title 2", "author 2", 1002));

        Result result = request(null);

        assertThat(contentAsString(result)).contains("title 1");
        assertThat(contentAsString(result)).contains("author 1");
        assertThat(contentAsString(result)).contains("1001");

        assertThat(contentAsString(result)).contains("title 2");
        assertThat(contentAsString(result)).contains("author 2");
        assertThat(contentAsString(result)).contains("1002");
    }

    @Test
    public void listContainsLinks() {
        Reference ref1 = new Reference(Book, "a", "title 1", "author 1", 1001);
        Reference ref2 = new Reference(Book, "b", "title 2", "author 2", 1002);
        Database.save(ref1);
        Database.save(ref2);

        Result result = request(null);

        assertThat(contentAsString(result)).contains("href=\"view?id=" + ref1.getId() + "\"");
        assertThat(contentAsString(result)).contains("href=\"view?id=" + ref2.getId() + "\"");
    }

    @Test
    public void searchReturnsMatchingEntries() {
        Reference ref1 = new Reference(Book, "a", "title 1", "author 1", 1001);
        Reference ref2 = new Reference(Book, "b", "title 2", "author foo 2", 1002);
        Reference ref3 = new Reference(Book, "c", "title 3", "author foo 3", 1003);
        Database.save(ref1);
        Database.save(ref2);
        Database.save(ref3);

        Result result = request("foo");

        assertThat(contentAsString(result)).contains(ref2.getTitle());
        assertThat(contentAsString(result)).contains(ref3.getTitle());
    }

    @Test
    public void searchDoesNotReturnOtherEntries() {
        Reference ref1 = new Reference(Book, "a", "title 1", "author 1", 1001);
        Reference ref2 = new Reference(Book, "b", "title 2", "author foo 2", 1002);
        Reference ref3 = new Reference(Book, "c", "title 3", "author foo 3", 1003);
        Database.save(ref1);
        Database.save(ref2);
        Database.save(ref3);

        Result result = request("foo");

        assertThat(contentAsString(result)).doesNotContain(ref1.getTitle());
    }

    @Test
    public void showMessageWhenNoResults() {
        Reference ref1 = new Reference(Book, "a", "title 1", "author 1", 1001);
        Reference ref2 = new Reference(Book, "b", "title 2", "author foo 2", 1002);
        Database.save(ref1);
        Database.save(ref2);

        Result result = request("abcdefghij");

        assertThat(contentAsString(result)).contains("Haulla ei löytynyt yhtään viitettä.");
    }
}
