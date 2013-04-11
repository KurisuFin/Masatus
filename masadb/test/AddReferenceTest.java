
import db.Database;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import models.Reference;
import static org.fest.assertions.Assertions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;
import play.test.Helpers;
import static play.test.Helpers.*;

public class AddReferenceTest {

    FakeApplication fa;
    Map<String, String> input;

    @Before
    public void setUp() {
        fa = fakeApplication(inMemoryDatabase());
        start(fa);

        input = new TreeMap<String, String>();
        input.put("title", "Asd Fasdf Asf Asf");
        input.put("author", "A AD Dwe Da A");
        input.put("publisher", "A Aasdaf Af");
        input.put("year", "2099");
        input.put("citeKey", "abc_123_def-2");
        input.put("address", "Street 123 012354 City");
        input.put("edition", "3rd");
        input.put("volume", "9999");
    }

    @After
    public void tearDown() {
        stop(fa);
    }

    Result postTestInput(Map<String, String> input) {
        FakeRequest fr = fakeRequest(POST, "/add").withFormUrlEncodedBody(input);
        return callAction(controllers.routes.ref.AddReference.save(), fr);
    }

    @Test
    public void pageTypeIsCorrect() {
        Result result = callAction(controllers.routes.ref.AddReference.show(), fakeRequest());
        assertThat(contentType(result)).isEqualTo("text/html");
        assertThat(status(result)).isEqualTo(OK);
    }

    @Test
    public void postingTheFormStoresEntryInDatabase() {
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(SEE_OTHER);
        assertThat(Database.findAll().size()).isEqualTo(1);
    }

    @Test
    public void returnToMainPageAfterSuccess() {
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(SEE_OTHER);
        assertThat(header("Location", result)).isEqualTo("/");
    }

    @Test
    public void correctDataIsStoredInDatabase() {
        status(postTestInput(input));
        List<Reference> refs = Database.findAll();
        assertThat(Database.findAll().size()).isEqualTo(1);
        assertThat(refs.get(0).getTitle()).isEqualTo(input.get("title"));
        assertThat(refs.get(0).getAuthor()).isEqualTo(input.get("author"));
        assertThat(refs.get(0).getPublisher()).isEqualTo(input.get("publisher"));
        assertThat(refs.get(0).getYear()).isEqualTo(Integer.parseInt(input.get("year")));
        assertThat(refs.get(0).getCiteKey()).isEqualTo(input.get("citeKey"));
        assertThat(refs.get(0).getAddress()).isEqualTo(input.get("address"));
        assertThat(refs.get(0).getEdition()).isEqualTo(input.get("edition"));
        assertThat(refs.get(0).getVolume()).isEqualTo(Integer.parseInt(input.get("volume")));
    }

    @Test
    public void failsIfTitleMissing() {
        input.put("title", "");
        Result result = postTestInput(input);
        assertThat(status(postTestInput(input))).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Kenttä on pakollinen");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfAuthorMissing() {
        input.put("author", "");
        Result result = postTestInput(input);
        assertThat(status(postTestInput(input))).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Kenttä on pakollinen");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfPublisherMissing() {
        input.put("publisher", "");
        Result result = postTestInput(input);
        assertThat(status(postTestInput(input))).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Kenttä on pakollinen");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfYearMissing() {
        input.put("year", "");
        Result result = postTestInput(input);
        assertThat(status(postTestInput(input))).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Kenttä on pakollinen");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfYearNotNumeric() {
        input.put("year", "2000a");
        Result result = postTestInput(input);
        assertThat(status(postTestInput(input))).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Virheellinen arvo");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfYearTooSmall() {
        input.put("year", "0");
        Result result = postTestInput(input);
        assertThat(status(postTestInput(input))).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Arvon tulee olla vähintään");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfYearTooBig() {
        input.put("year", "2100");
        Result result = postTestInput(input);
        assertThat(status(postTestInput(input))).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Arvo saa olla enintään");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfCiteKeyMissing() {
        input.put("citeKey", "");
        Result result = postTestInput(input);
        assertThat(status(postTestInput(input))).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Kenttä on pakollinen");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfCiteKeyStartsWithNumber() {
        input.put("citeKey", "0abc");
        Result result = postTestInput(input);
        assertThat(status(postTestInput(input))).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Voi sisältää vain merkkejä ");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfCiteKeyHasSpaces() {
        input.put("citeKey", "a bc");
        Result result = postTestInput(input);
        assertThat(status(postTestInput(input))).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Voi sisältää vain merkkejä ");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfCiteKeyHasIllegalCharacters() {
        input.put("citeKey", "a.bc");
        Result result = postTestInput(input);
        assertThat(status(postTestInput(input))).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Voi sisältää vain merkkejä ");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfVolumeNotNumeric() {
        input.put("volume", "1000.0");
        Result result = postTestInput(input);
        assertThat(status(postTestInput(input))).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Virheellinen arvo");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfVolumeTooSmall() {
        input.put("year", "0");
        Result result = postTestInput(input);
        assertThat(status(postTestInput(input))).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Arvon tulee olla vähintään");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfVolumeTooBig() {
        input.put("year", "10000");
        Result result = postTestInput(input);
        assertThat(status(postTestInput(input))).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Arvo saa olla enintään");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void addressIsOptional() {
        input.put("address", "");
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(SEE_OTHER);
        assertThat(Database.findAll().size()).isEqualTo(1);
    }

    @Test
    public void editionIsOptional() {
        input.put("edition", "");
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(SEE_OTHER);
        assertThat(Database.findAll().size()).isEqualTo(1);
    }

    @Test
    public void volumeIsOptional() {
        input.put("volume", "");
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(SEE_OTHER);
        assertThat(Database.findAll().size()).isEqualTo(1);
    }
}
