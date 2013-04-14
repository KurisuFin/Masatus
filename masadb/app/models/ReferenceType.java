package models;

/**
 * Viitteen tyyppi. Sisältää BibTexin määrittelemät standardityypit.
 */
public enum ReferenceType {

    Article,
    Book,
    Booklet,
    InBook,
    InCollection,
    InPoceedings,
    Manual,
    MastersThesis,
    Misc,
    PhdThesis,
    Proceedings,
    TechReport,
    Unpublished;

    @Override
    public String toString() {
        switch (this) {
            case Article:
                return "Artikkeli";
            case Book:
                return "Kirja";
            case Booklet:
                return "Kirjanen";
            case InBook:
                return "Kirjan osa";
            case InCollection:
                return "Kokoelman osa";
            case InPoceedings:
                return "Konferenssijulkaisun osa";
            case Manual:
                return "Manuaali";
            case MastersThesis:
                return "Opinnäytetyö";
            case Misc:
                return "Sekalainen";
            case PhdThesis:
                return "Väitöskirja";
            case Proceedings:
                return "Konferenssijulkaisu";
            case TechReport:
                return "Tekninen raportti";
            case Unpublished:
                return "Julkaisematon";
            default:
                throw new IllegalArgumentException();
        }
    }
}
