package controllers;

import db.Database;
import models.Reference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import static models.ReferenceType.*;

public class ViewReferenceTest {

    FakeApplication fa;
    Reference ref1;

    @Before
    public void setUp() {
        fa = fakeApplication(inMemoryDatabase());
        start(fa);

        ref1 = new Reference(Book, "M12", "The Title", "Masa", 2012);
        ref1.setMonth("jan");
        ref1.setVolume(7);
        ref1.setNumber(8);
        ref1.setEdition("Second");
        ref1.setPages("123--321");
        ref1.setBookTitle("Book Title");
        ref1.setPublisher("Masa Publishing");
        ref1.setAddress("Masala");
        ref1.setOrganization("Masala University");
        Database.save(ref1);
    }

    @After
    public void tearDown() {
        stop(fa);
    }

    Result request(String id) {
        FakeRequest fr = fakeRequest(GET, "/view?id=" + id);
        return callAction(controllers.routes.ref.ViewReference.show(), fr);
    }

    String td(String id, String text) {
        return "<td id=\"" + id + "\">" + text + "</td>";
    }

    @Test
    public void pageTypeIsCorrect() {
        Result result = request(Integer.toString(ref1.getId()));
        assertThat(contentType(result)).isEqualTo("text/html");
        assertThat(status(result)).isEqualTo(OK);
    }

    @Test
    public void errorWhenIdIncorrect() {
        Result result = request(Integer.toString(1234567890));
        assertThat(status(result)).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Viitettä ei löytynyt");
    }

    @Test
    public void errorWhenIdMalformed() {
        Result result = request(Integer.toString(ref1.getId()) + 'a');
        assertThat(status(result)).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Virheellinen viitetunnus");
    }

    @Test
    public void pageHasCorrectData() {
        Result result = request(Integer.toString(ref1.getId()));
        assertThat(contentAsString(result)).contains(td("type", ref1.getType().getDescription()));
        assertThat(contentAsString(result)).contains(td("citeKey", ref1.getCiteKey()));
        assertThat(contentAsString(result)).contains(td("title", ref1.getTitle()));
        assertThat(contentAsString(result)).contains(td("author", ref1.getAuthor()));
        assertThat(contentAsString(result)).contains(td("year", Integer.toString(ref1.getYear())));
        assertThat(contentAsString(result)).contains(td("month", ref1.getMonth()));
        assertThat(contentAsString(result)).contains(td("volume", Integer.toString(ref1.getVolume())));
        assertThat(contentAsString(result)).contains(td("number", Integer.toString(ref1.getNumber())));
        assertThat(contentAsString(result)).contains(td("edition", ref1.getEdition()));
        assertThat(contentAsString(result)).contains(td("pages", ref1.getPages()));
        assertThat(contentAsString(result)).contains(td("bookTitle", ref1.getBookTitle()));
        assertThat(contentAsString(result)).contains(td("publisher", ref1.getPublisher()));
        assertThat(contentAsString(result)).contains(td("address", ref1.getAddress()));
        assertThat(contentAsString(result)).contains(td("organization", ref1.getOrganization()));
    }

    @Test
    public void emptyAttributesNotDisplayed() {
        Reference ref2 = new Reference(Book, "a", "b", "c", 123);
        Database.save(ref2);
        Result result = request(Integer.toString(ref2.getId()));
        assertThat(contentAsString(result)).doesNotContain(td("month", ""));
        assertThat(contentAsString(result)).doesNotContain(td("volume", ""));
        assertThat(contentAsString(result)).doesNotContain(td("number", ""));
        assertThat(contentAsString(result)).doesNotContain(td("edition", ""));
        assertThat(contentAsString(result)).doesNotContain(td("pages", ""));
        assertThat(contentAsString(result)).doesNotContain(td("bookTitle", ""));
        assertThat(contentAsString(result)).doesNotContain(td("publisher", ""));
        assertThat(contentAsString(result)).doesNotContain(td("address", ""));
        assertThat(contentAsString(result)).doesNotContain(td("organization", ""));
    }

    @Test
    public void pageContainsTheBibTexCode() {
        Result result = request(Integer.toString(ref1.getId()));
        // Testi vaatii ettei bibtex-koodissa ole erikoismerkkejä
        // jotka html enkoodaa eri tavalla!
        assertThat(contentAsString(result)).contains(ref1.generateBibtexEntry());
    }

    @Test
    public void specialCharactersAreEncoded() {
        ref1.setTitle("<>\"'&");
        Database.save(ref1);
        Result result = request(Integer.toString(ref1.getId()));
        assertThat(contentAsString(result)).contains(td("title", "&lt;&gt;&quot;&#x27;&amp;"));
    }
}
