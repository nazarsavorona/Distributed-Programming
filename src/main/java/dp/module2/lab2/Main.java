package dp.module2.lab2;

import dp.module2.lab1.store.Film;
import dp.module2.lab1.store.Genre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws Exception {
        VideoStoreDAO videoStore = new VideoStoreDAO("VideoStore", "localhost", 5432);

//        System.out.println("Before");
//        videoStore.showGenres();
//        videoStore.deleteGenre("Action");
//        videoStore.updateGenre("Fantasy", new Genre("Documentary"));
//
//        System.out.println("After");
//        videoStore.showGenres();

//        videoStore.showGenres();
//        videoStore.showFilms();

//        Genre documentary =  new Genre(2, "Documentary");

//        videoStore.addGenre(documentary);

//        videoStore.addFilm(new Film("Harry", 1.4f, documentary));
//        videoStore.addFilm(new Film("Harry1", 1.4f, documentary));
//        videoStore.addFilm(new Film("Harry2", 1.4f, documentary));

       // videoStore.deleteGenre("Documentary");
        videoStore.showGenres();
        videoStore.showFilms();

        System.out.println( videoStore.getNumberOfFilmsByGenre("Documentary"));

        for(Film film : videoStore.getFilmsByGenre("Documentary")) {
            System.out.println(film);
        }

        videoStore.showGenres();

        videoStore.stop();
    }
}
