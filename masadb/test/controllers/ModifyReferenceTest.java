package controllers;

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
import static controllers.ModifyReference.generoiViite;
import static org.junit.Assert.*;

public class ModifyReferenceTest {

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

    void testOptionalField(String name) {
        input.put(name, "");
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(SEE_OTHER);
        assertThat(Database.findAll().size()).isEqualTo(1);
    }

    @Test
    public void pageTypeIsCorrect() {
        Result result = callAction(controllers.routes.ref.ModifyReference.show(), fakeRequest());
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
        assertThat(refs.get(0).getType().toString()).isEqualTo(input.get("type"));
        assertThat(refs.get(0).getTitle()).isEqualTo(input.get("title"));
        assertThat(refs.get(0).getAuthor()).isEqualTo(input.get("author"));
        assertThat(refs.get(0).getYear()).isEqualTo(Integer.parseInt(input.get("year")));
        assertThat(refs.get(0).getMonth()).isEqualTo(input.get("month"));
        assertThat(refs.get(0).getVolume()).isEqualTo(Integer.parseInt(input.get("volume")));
        assertThat(refs.get(0).getNumber()).isEqualTo(Integer.parseInt(input.get("number")));
        assertThat(refs.get(0).getEdition()).isEqualTo(input.get("edition"));
        assertThat(refs.get(0).getPages()).isEqualTo(input.get("pages"));
        assertThat(refs.get(0).getBookTitle()).isEqualTo(input.get("bookTitle"));
        assertThat(refs.get(0).getPublisher()).isEqualTo(input.get("publisher"));
        assertThat(refs.get(0).getAddress()).isEqualTo(input.get("address"));
        assertThat(refs.get(0).getOrganization()).isEqualTo(input.get("organization"));
    }

    @Test
    public void failsIfTitleMissing() {
        input.put("title", "");
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Kenttä on pakollinen");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfAuthorMissing() {
        input.put("author", "");
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Kenttä on pakollinen");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfYearMissing() {
        input.put("year", "");
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Kenttä on pakollinen");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfYearNotNumeric() {
        input.put("year", "2000a");
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Virheellinen arvo");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfYearTooSmall() {
        input.put("year", "0");
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Arvon tulee olla vähintään");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfYearTooBig() {
        input.put("year", "2100");
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Arvo saa olla enintään");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfVolumeNotNumeric() {
        input.put("volume", "1000.0");
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Virheellinen arvo");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfVolumeTooSmall() {
        input.put("volume", "0");
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Arvon tulee olla vähintään");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfVolumeTooBig() {
        input.put("volume", "10000");
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Arvo saa olla enintään");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfNumberNotNumeric() {
        input.put("number", "1000.0");
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Virheellinen arvo");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfNumberTooSmall() {
        input.put("number", "0");
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Arvon tulee olla vähintään");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfNumberTooBig() {
        input.put("number", "10000");
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Arvo saa olla enintään");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void failsIfPagesInWrongFormat() {
        input.put("pages", "123--321a");
        Result result = postTestInput(input);
        assertThat(status(result)).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Anna sivut muodossa");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void monthIsOptional() {
        testOptionalField("month");
    }

    @Test
    public void volumeIsOptional() {
        testOptionalField("volume");
    }

    @Test
    public void numberIsOptional() {
        testOptionalField("number");
    }

    @Test
    public void editionIsOptional() {
        testOptionalField("edition");
    }

    @Test
    public void pagesIsOptional() {
        testOptionalField("pages");
    }

    @Test
    public void bookTitleIsOptional() {
        testOptionalField("bookTitle");
    }

    @Test
    public void publisherIsOptional() {
        testOptionalField("publisher");
    }

    @Test
    public void addressIsOptional() {
        testOptionalField("address");
    }

    @Test
    public void organizationIsOptional() {
        testOptionalField("organization");
    }

    // CITE KEY TESTS

    @Test
    public void finnishAlphabetsAreFixedCorrectlyInCiteKeys() {
        String cite;
        cite = generoiViite("Ååbacka, Jesse", 2011);
        assertThat(cite).isEqualTo("A11");
        cite = generoiViite("Äggänen, Jorma", 2011);
        assertThat(cite).isEqualTo("A11");
        cite = generoiViite("Heikki Öljy", 2011);
        assertThat(cite).isEqualTo("O11");
    }

    @Test
    public void smallFinnishAlphabetsAreFixedCorrectlyInCiteKeys() {
        String cite;
        cite = generoiViite("å Åkkelstein, Xavier", 2011);
        assertThat(cite).isEqualTo("aA11");
        cite = generoiViite("ä Äkkelstein, Xavier", 2011);
        assertThat(cite).isEqualTo("aA11");
        cite = generoiViite("ö Ökkelstein, Xavier", 2011);
        assertThat(cite).isEqualTo("oO11");
    }

    @Test
    public void citeKeyContainsMaximumOfFourLettersAndTwoDigits() {
        String cite;
        cite = generoiViite("Pekka O and Heikki J and Hilda J and Gunnar Å and Jorma P", 2011);
        assertThat(cite).isEqualTo("OJJA11");
    }

    // CITE KEY TESTS WITH POSTING

    @Test
    public void citeKeyContainsFirstLetterOfAuthorAndTwoLastDigitsOfYear() {
        input.put("author", "Urho Kekkonen");
        input.put("year", "2010");
        status(postTestInput(input));

        List<Reference> refs = Database.findAll();
        assertThat(refs.get(0).getCiteKey()).isEqualTo("K10");
    }

    @Test
    public void citeKeyIsGeneratedRightWhenNameIsSeparatedWithComma() {
        input.put("author", "Kekkonen, Urho");
        input.put("year", "2010");
        status(postTestInput(input));

        List<Reference> refs = Database.findAll();
        assertThat(refs.get(0).getCiteKey()).isEqualTo("K10");
    }

    @Test
    public void citeKeyContainsSmallLetterOfLastName() {
        input.put("author", "von Richthofen, Manfred");
        input.put("year", "2013");
        status(postTestInput(input));

        List<Reference> refs = Database.findAll();
        assertThat(refs.get(0).getCiteKey()).isEqualTo("vR13");
    }

    @Test
    public void citeKeyContainsFirstLettersOfAuthors() {
        input.put("author", "Kekkonen, Urho and von Richthofen, Manfred and Jeesus Nasaretilainen");
        input.put("year", "2013");
        status(postTestInput(input));

        List<Reference> refs = Database.findAll();
        assertThat(refs.get(0).getCiteKey()).isEqualTo("KvRN13");
    }

    public void citeKeyContainsMaximumOfFourLetters() {
        input.put("author", "Kekkonen, Urho and von Richthofen, Manfred and Jeesus Nasaretilainen and Descartes, Rene");
        input.put("year", "2013");
        status(postTestInput(input));

        List<Reference> refs = Database.findAll();
        assertThat(refs.get(0).getCiteKey()).isEqualTo("KvRN13");
    }

    @Test
    public void aLetterIsAddedInCaseOfSameCiteKey() {
        input.put("author", "Kekkonen, Urho");
        input.put("year", "2013");
        status(postTestInput(input));

        input.put("author", "Koivisto, Mauno");
        input.put("year", "2013");
        status(postTestInput(input));

        input.put("author", "Kallio, Kyösti");
        input.put("year", "1913");
        status(postTestInput(input));

        List<Reference> refs = Database.findAll();
        assertThat(refs.get(0).getCiteKey()).isEqualTo("K13");
        assertThat(refs.get(1).getCiteKey()).isEqualTo("K13a");
        assertThat(refs.get(2).getCiteKey()).isEqualTo("K13b");
    }

	@Test
	public void yearsAreProcessedCorrectlyForCiteKey() {
        input.put("author", "Zorro, Diego");
        input.put("year", "213");
        status(postTestInput(input));

        input.put("author", "Xonar, Asus");
        input.put("year", "23");
        status(postTestInput(input));

        input.put("author", "Yttrium, Lanthanide");
        input.put("year", "1");
        status(postTestInput(input));

        List<Reference> refs = Database.findAll();
        assertThat(refs.get(0).getCiteKey()).isEqualTo("Z13");
        assertThat(refs.get(1).getCiteKey()).isEqualTo("X23");
        assertThat(refs.get(2).getCiteKey()).isEqualTo("Y1");
    }

    @Test
    public void allCiteKeysAreUnique() {
        int loop = 50;

        for (int i = 0; i < loop; ++i)
            status(postTestInput(input));

        List<Reference> refs = Database.findAll();

        for (int i = 0; i < loop-1; ++i)
            for (int j = i+1; j < loop; ++j)
                assertThat(refs.get(i).getCiteKey()).isNotEqualTo(refs.get(j).getCiteKey());
    }

    @Test
    public void pageTypeIsCorrectForEdit() {
        status(postTestInput(input));
        Result result = callAction(controllers.routes.ref.ModifyReference.edit(1), fakeRequest());
        assertThat(contentType(result)).isEqualTo("text/html");
        assertThat(status(result)).isEqualTo(OK);
    }

    Result editPostTestInput(Map<String, String> input) {
        FakeRequest fr = fakeRequest(POST, "/edit/1").withFormUrlEncodedBody(input);
        return callAction(controllers.routes.ref.ModifyReference.update(1), fr);
    }

    @Test
    public void dataIsModifiedInDatabase() {

        status(postTestInput(input));

        status(editPostTestInput(modifiedinput));

        List<Reference> refs = Database.findAll();
        assertThat(Database.findAll().size()).isEqualTo(1);
        assertThat(refs.get(0).getType().toString()).isEqualTo(modifiedinput.get("type"));
        assertThat(refs.get(0).getTitle()).isEqualTo(modifiedinput.get("title"));
        assertThat(refs.get(0).getAuthor()).isEqualTo(modifiedinput.get("author"));
        assertThat(refs.get(0).getYear()).isEqualTo(Integer.parseInt(modifiedinput.get("year")));
        assertThat(refs.get(0).getMonth()).isEqualTo(modifiedinput.get("month"));
        assertThat(refs.get(0).getVolume()).isEqualTo(Integer.parseInt(modifiedinput.get("volume")));
        assertThat(refs.get(0).getNumber()).isEqualTo(Integer.parseInt(modifiedinput.get("number")));
        assertThat(refs.get(0).getEdition()).isEqualTo(modifiedinput.get("edition"));
        assertThat(refs.get(0).getPages()).isEqualTo(modifiedinput.get("pages"));
        assertThat(refs.get(0).getBookTitle()).isEqualTo(modifiedinput.get("bookTitle"));
        assertThat(refs.get(0).getPublisher()).isEqualTo(modifiedinput.get("publisher"));
        assertThat(refs.get(0).getAddress()).isEqualTo(modifiedinput.get("address"));
        assertThat(refs.get(0).getOrganization()).isEqualTo(modifiedinput.get("organization"));
    }

    @Test
    public void editFormfailsIfYearTooSmall() {
        input.put("year", "0");
        Result result = editPostTestInput(input);
        assertThat(status(result)).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Arvon tulee olla vähintään");
        assertThat(Database.findAll().isEmpty());
    }

    @Test
    public void generoiViiteTest() {
        assertEquals("AK99", generoiViite("Äijä, öö ää and Kekkonen, Urho Kaleva", 1999));
    }

}
