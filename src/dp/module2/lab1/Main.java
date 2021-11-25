package dp.module2.lab1;

import dp.module2.lab1.store.Film;
import dp.module2.lab1.store.Genre;
import dp.module2.lab1.store.VideoStore;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException, SAXException {
        String xml = "src/dp/module2/lab1/videoStore.xml";
        String xml_mod = "src/dp/module2/lab1/videoStoreMod.xml";
        String xsd = "src/dp/module2/lab1/mapVideoStore.xsd";

        VideoStore videoStore = new VideoStore(xml, xsd);

//        List<Genre> genres = new ArrayList<>(Arrays.asList(new Genre(1,"Action"),
//                new Genre(2,"Drama"),
//                new Genre(3,"Horror"),
//                new Genre(4,"Fantasy")));
//
//        genres.forEach(genre -> videoStore.addGenre(genre));
//
//        videoStore.addFilm(new Film(1, "James Bond", 1.56f, genres.get(0)));
//        videoStore.addFilm(new Film(2, "Mission: Impossible", 2.34f, genres.get(0)));
//        videoStore.addFilm(new Film(3, "First Reformed", 1.2f, genres.get(1)));
//        videoStore.addFilm(new Film(4, "Harry Potter", 1.2f, genres.get(3)));

        videoStore.addFilm(new Film(4, "Imposter", 1.2f, new Genre(100,"Imposter")));
        videoStore.printCurrentState("Before modifying");
        videoStore.deleteFilm(1);
        videoStore.deleteGenre(4);
        videoStore.editFilm(2,
                new Film(2, "Mission: Impossible", 2.34f, videoStore.getGenre(2)));
        videoStore.printCurrentState("After modifying");

        videoStore.saveToFile(xml_mod);
    }

    public static void log(String message) {
        logger.log(Level.INFO, message);
    }
}
