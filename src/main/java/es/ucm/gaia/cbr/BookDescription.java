package es.ucm.gaia.cbr;

import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CaseComponent;

/**
 * Implement the book description class.
 *
 * @author Jose L. Jorro-Aragoneses
 * @version 1.0
 */
public class BookDescription implements CaseComponent {

    private long isbn;
    private String title;
    private String author;
    private int yearOfPublication;

    @Override
    public Attribute getIdAttribute() {
        return new Attribute("isbn", BookDescription.class);
    }

    public BookDescription() {}

    public BookDescription(String[] csvRow) {
        this.isbn = Long.valueOf(csvRow[0]);
        this.title = csvRow[1];
        this.author = csvRow[2];
        this.yearOfPublication = Integer.valueOf(csvRow[3]);
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    @Override
    public String toString() {
        return "BookDescription{" +
                "isbn=" + isbn +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", yearOfPublication=" + yearOfPublication +
                '}';
    }
}
