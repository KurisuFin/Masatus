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

public class ViewReferenceTest {

    FakeApplication fa;
    Reference ref1, ref2;

    @Before
    public void setUp() {
        fa = fakeApplication(inMemoryDatabase());
        start(fa);

        ref1 = new Reference("M12", "The Title", "Masa",
                "Masa Publishing", 2012);
        ref1.setAddress("Masala");
        ref1.setEdition("Second");
        ref1.setVolume(7);
        Database.save(ref1);

        ref2 = new Reference("M13", "The Title II", "Masa II",
                "Masa Publishing", 2013);
        ref2.setAddress("Masala");
        ref2.setEdition(null);
        ref2.setVolume(7);
        Database.save(ref2);
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
        assertThat(contentAsString(result)).contains(td("title", ref1.getTitle()));
        assertThat(contentAsString(result)).contains(td("author", ref1.getAuthor()));
        assertThat(contentAsString(result)).contains(td("publisher", ref1.getPublisher()));
        assertThat(contentAsString(result)).contains(td("year", Integer.toString(ref1.getYear())));
        assertThat(contentAsString(result)).contains(td("citeKey", ref1.getCiteKey()));
        assertThat(contentAsString(result)).contains(td("address", ref1.getAddress()));
        assertThat(contentAsString(result)).contains(td("edition", ref1.getEdition()));
        assertThat(contentAsString(result)).contains(td("volume", Integer.toString(ref1.getVolume())));
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
