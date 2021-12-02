package dp.module2.lab1.store;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Film {
    private int id;
    private String name;
    private Float duration;
    private Genre genre;

    public Float getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public Film(int id, String name, Float duration, Genre genre) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.genre = genre;
    }

    public Film(String name, Float duration, Genre genre) {
        this.name = name;
        this.duration = duration;
        this.genre = genre;
    }

    public Film (Element filmElement, Element genreElement) {
        this.id = Integer.parseInt(filmElement.getAttribute("id"));
        this.name = filmElement.getAttribute("name");
        this.duration = Float.parseFloat(filmElement.getAttribute("duration"));
        this.genre = new Genre(genreElement);
    }

    public Genre getGenre() {
        return genre;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", genre=" + genre +
                '}';
    }

    public Element getElement(Document document) {
        Element element = document.createElement("Film");

        element.setAttribute("id", Integer.toString(id));
        element.setAttribute("name", name);
        element.setAttribute("duration", Float.toString(duration));

        return element;
    }
}
