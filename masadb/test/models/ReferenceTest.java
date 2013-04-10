package models;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.fest.assertions.Assertions.*;

public class ReferenceTest {

    Reference ref;

    @Before
    public void setUp() {
        ref = new Reference("M12", "The Title", "Masa",
                "Masa Publishing", 2012);
    }

    @Test
    public void constructorSetsRequiredValues() {
        assertEquals("M12", ref.citeKey);
        assertEquals("The Title", ref.title);
        assertEquals("Masa", ref.author);
        assertEquals("Masa Publishing", ref.publisher);
        assertEquals(2012, (int) ref.year);
    }

    @Test
    public void optionalValuesAreNull() {
        assertNull(ref.address);
        assertNull(ref.edition);
        assertNull(ref.volume);
    }

    @Test
    public void bibtexWhenNoOptionalTags() {
        assertEquals("@book{M12,\n"
                + "title = {The Title},\n"
                + "author = {Masa},\n"
                + "publisher = {Masa Publishing},\n"
                + "year = {2012},\n"
                + "}",
                ref.generateBibtexEntry());
    }

    @Test
    public void bibtexWithOptionalTags() {
        ref.address = "Masala";
        ref.edition = "Second";
        ref.volume = 7;
        assertEquals("@book{M12,\n"
                + "title = {The Title},\n"
                + "author = {Masa},\n"
                + "publisher = {Masa Publishing},\n"
                + "year = {2012},\n"
                + "address = {Masala},\n"
                + "edition = {Second},\n"
                + "volume = {7},\n"
                + "}",
                ref.generateBibtexEntry());
    }

    @Test
    public void bibtexWithSpecialCharacters() {
        ref.title = "åäöÅÄÖ";
        assertEquals("@book{M12,\n"
                + "title = {\\r{a}\\\"{a}\\\"{o}{\\r{A}}{\\\"{A}}{\\\"{O}}},\n"
                + "author = {Masa},\n"
                + "publisher = {Masa Publishing},\n"
                + "year = {2012},\n"
                + "}",
                ref.generateBibtexEntry());
    }

    @Test
    public void bibtexWithAcronyms() {
        ref.title = "AB cde FGhi jk Lmno pqRstu VW XYZ";
        assertEquals("@book{M12,\n"
                + "title = {{A}{B} cde {F}{G}hi jk Lmno pqRstu {V}{W} {X}{Y}{Z}},\n"
                + "author = {Masa},\n"
                + "publisher = {Masa Publishing},\n"
                + "year = {2012},\n"
                + "}",
                ref.generateBibtexEntry());
    }
}
