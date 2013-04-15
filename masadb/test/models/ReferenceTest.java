package models;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.fest.assertions.Assertions.*;
import static models.ReferenceType.*;

public class ReferenceTest {

    Reference ref;

    @Before
    public void setUp() {
        ref = new Reference(Book, "M12", "The Title", "Masa", 2012);
    }

    @Test
    public void constructorSetsRequiredValues() {
        assertEquals(Book, ref.getType());
        assertEquals("M12", ref.getCiteKey());
        assertEquals("The Title", ref.getTitle());
        assertEquals("Masa", ref.getAuthor());
        assertEquals(2012, (int) ref.getYear());
    }

    @Test
    public void optionalValuesAreNull() {
        assertNull(ref.getMonth());
        assertNull(ref.getVolume());
        assertNull(ref.getNumber());
        assertNull(ref.getEdition());
        assertNull(ref.getPages());
        assertNull(ref.getBookTitle());
        assertNull(ref.getPublisher());
        assertNull(ref.getAddress());
        assertNull(ref.getOrganization());
    }

    @Test
    public void bibtexWhenNoOptionalTags() {
        assertEquals("@book{M12,\n"
                + "title = {The Title},\n"
                + "author = {Masa},\n"
                + "year = {2012},\n"
                + "}",
                ref.generateBibtexEntry());
    }

    @Test
    public void bibtexWithOptionalTags() {
        ref.setMonth("jan");
        ref.setVolume(7);
        ref.setNumber(8);
        ref.setEdition("Second");
        ref.setPages("123--321");
        ref.setBookTitle("Book Title");
        ref.setPublisher("Masa Publishing");
        ref.setAddress("Masala");
        ref.setOrganization("Masala University");
        assertEquals("@book{M12,\n"
                + "title = {The Title},\n"
                + "author = {Masa},\n"
                + "year = {2012},\n"
                + "month = {jan},\n"
                + "volume = {7},\n"
                + "number = {8},\n"
                + "edition = {Second},\n"
                + "pages = {123--321},\n"
                + "booktitle = {Book Title},\n"
                + "publisher = {Masa Publishing},\n"
                + "address = {Masala},\n"
                + "organization = {Masala University},\n"
                + "}",
                ref.generateBibtexEntry());
    }

    @Test
    public void bibtexWithSpecialCharacters() {
        ref.setTitle("åäöÅÄÖ");
        assertEquals("@book{M12,\n"
                + "title = {\\r{a}\\\"{a}\\\"{o}{\\r{A}}{\\\"{A}}{\\\"{O}}},\n"
                + "author = {Masa},\n"
                + "year = {2012},\n"
                + "}",
                ref.generateBibtexEntry());
    }

    @Test
    public void bibtexWithAcronyms() {
        ref.setTitle("AB cde FGhi jk Lmno pqRstu VW XYZ");
        assertEquals("@book{M12,\n"
                + "title = {{A}{B} cde {F}{G}hi jk Lmno pqRstu {V}{W} {X}{Y}{Z}},\n"
                + "author = {Masa},\n"
                + "year = {2012},\n"
                + "}",
                ref.generateBibtexEntry());
    }

    @Test
    public void bibtexForArticle() {
        ref.setType(Article);
        assertEquals("@article{M12,\n"
                + "title = {The Title},\n"
                + "author = {Masa},\n"
                + "year = {2012},\n"
                + "}",
                ref.generateBibtexEntry());
    }
}
