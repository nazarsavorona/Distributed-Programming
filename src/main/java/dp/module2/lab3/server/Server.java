package dp.module2.lab3.server;

import dp.module2.lab1.store.Film;
import dp.module2.lab1.store.Genre;
import dp.module2.lab2.VideoStoreDAO;
import dp.module2.lab3.common.QueryType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private VideoStoreDAO videoStore;

    private ServerSocket server;
    private Socket clientSocket;
    private DataOutputStream out = null;
    private DataInputStream in = null;

    public Server(int port, VideoStoreDAO videoStore) {
        this.server = null;
        this.out = null;
        this.in = null;
        this.videoStore = videoStore;

        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        while (true) {
            acceptClient();

            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            while (processQuery()) ;
        }
    }

    private boolean processQuery() throws IOException {
        try {
            QueryType type = QueryType.valueOf(in.readUTF());

            switch (type) {
                case ADD_GENRE -> videoStore.addGenre(new Genre(in.readUTF()));
                case DELETE_GENRE -> videoStore.deleteGenre(in.readUTF());
                case ADD_FILM -> addFilm();
                case DELETE_FILM -> deleteFilm();
                case UPDATE_FILM -> updateFilm();
                case COUNT_FILMS_BY_GENRE -> countFilmsByGenre();
                case GET_FILM_BY_NAME -> getFilmByName();
                case GET_FILMS_BY_GENRE -> getFilmsByGenre();
                case GET_GENRES -> getGenres();
                default -> throw new IllegalStateException("Unexpected value: " + type);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            out.writeInt(-1);
            return false;
        }
    }

    private void getGenres() throws IOException {
        List<Genre> genres = videoStore.getGenres();

        out.writeInt(0);
        out.writeInt(genres.size());

        for (Genre genre : genres) {
            for (String argument : genre.toList())
                out.writeUTF(argument);
        }
    }

    private void getFilmsByGenre() throws IOException {
        List<Film> films = videoStore.getFilmsByGenre(Genre.parseGenre(in).getName());

        out.writeInt(0);
        out.writeInt(films.size());

        for (Film film : films) {
            for (String argument : film.toList())
                out.writeUTF(argument);
        }
    }

    private void getFilmByName() throws IOException, SQLException {
        List<String> list = new ArrayList<>();

        out.writeInt(0);

        for (int i = 0, n = Film.listSize(); i < n; i++)
            list.add(in.readUTF());

        List<String> filmList = videoStore.getFilmByName(new Film(list).getName()).get(0).toList();

        for (int i = 0, n = Film.listSize(); i < n; i++)
            out.writeUTF(filmList.get(i));
    }

    private void countFilmsByGenre() throws IOException {
        out.writeInt(0);
        out.writeInt(videoStore.getNumberOfFilmsByGenre(Genre.parseGenre(in).getName()));
    }

    private void updateFilm() throws Exception {
        out.writeInt(0);
        videoStore.updateFilm(Film.parseFilm(in), Film.parseFilm(in));
    }

    private void deleteFilm() throws IOException {
        out.writeInt(0);
        videoStore.deleteFilm(Film.parseFilm(in).getName());
    }

    private void addFilm() throws IOException {
        out.writeInt(0);
        videoStore.addFilm(Film.parseFilm(in));
    }

    private void acceptClient() {
        clientSocket = null;
        int attempts = 5;

        while (attempts > 0) {
            try {
                System.out.println("Waiting for a client...");
                clientSocket = server.accept();
                System.out.println("Client connected");
                break;
            } catch (IOException e) {
                int timeout = 5000;
                System.out.printf("Failed. Waiting %d ms...%n", timeout);
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            --attempts;
        }
    }

    public static void main(String[] args) throws Exception {
        VideoStoreDAO videoStore = new VideoStoreDAO("VideoStore", "localhost", 5432);
        Server server = new Server(2710, videoStore);

        server.start();

        videoStore.stop();
    }
}
