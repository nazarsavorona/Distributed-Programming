package dp.module2.lab3.client;

import dp.module2.lab1.store.Film;
import dp.module2.lab1.store.Genre;
import dp.module2.lab3.common.QueryType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    public Client(String ip, int port) throws IOException {
        socket = null;

        while (true) {
            try {
                System.out.println("Connecting to server...");
                socket = new Socket(ip, port);
                System.out.println("Connected");
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
        }

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public boolean addGenre(Genre genre) throws IOException {
        sendQuery(QueryType.ADD_GENRE, List.of(genre.getName()));
        return in.readInt() == 0;
    }

    public boolean deleteGenre(Genre genre) throws IOException {
        sendQuery(QueryType.DELETE_GENRE, List.of(genre.getName()));
        return in.readInt() == 0;
    }

    public boolean addFilm(Film film) throws IOException {
        sendQuery(QueryType.ADD_FILM, film.toList());
        return in.readInt() == 0;
    }

    public boolean deleteFilm(Film film) throws IOException {
        sendQuery(QueryType.ADD_FILM, film.toList());
        return in.readInt() == 0;
    }

    public boolean updateFilm(Film oldFilm, Film newFilm) throws IOException {
        List<String> arguments = oldFilm.toList();
        arguments.addAll(newFilm.toList());
        sendQuery(QueryType.UPDATE_FILM, arguments);

        return in.readInt() == 0;
    }

    public int countFilmsByGenre(Genre genre) throws IOException {
        sendQuery(QueryType.COUNT_FILMS_BY_GENRE, List.of(genre.getName()));
        int count = in.readInt();
        int status = in.readInt();

        return count;
    }

    public Film getFilmByName(Film film) throws IOException {
        sendQuery(QueryType.GET_FILM_BY_NAME, film.toList());
        Film resultFilm = Film.parseFilm(in);
        int status = in.readInt();
        return resultFilm;
    }

    public List<Film> getFilmsByGenre(Genre genre) throws IOException {
        sendQuery(QueryType.GET_FILMS_BY_GENRE, genre.toList());
        int count = in.readInt();

        List<Film> films = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            films.add(Film.parseFilm(in));
        }

        int status = in.readInt();

        return films;
    }

    public List<Genre> getGenres() throws IOException {
        sendQuery(QueryType.GET_GENRES, List.of());
        int count = in.readInt();

        List<Genre> genres = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            genres.add(Genre.parseGenre(in));
        }

        int status = in.readInt();

        return genres;
    }

    public void disconnect() throws IOException {
        System.out.println("Disconnected from server");
        socket.close();
    }

    private boolean sendQuery(QueryType type, List<String> arguments) throws IOException {
        out.writeUTF(type.name());

        for (String argument : arguments) {
            out.writeUTF(argument);
        }

        return true;
    }

    public static void main(String[] args) throws IOException {
        Client client = null;
        try {
            client = new Client("localhost", 2710);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);
        String genreName;
        String filmName;
        float duration;


        while (true) {
//            System.out.print("Enter a genre: ");
//            genreName = sc.nextLine();
//
//            if (Objects.equals(genreName, "stop")) {
//                break;
//            }
//
//            client.addGenre(new Genre(genreName));

            for(Genre genre : client.getGenres()) {
                System.out.println(genre);
            }

            System.out.print("Enter a film name: ");
            filmName = sc.nextLine();
//
            if (filmName == "stop")
                break;
//
//            System.out.print("Enter a film duration: ");
//            duration = Float.parseFloat(sc.nextLine());
//            System.out.print("Enter a film genre: ");
//            genreName = sc.nextLine();
//
//            client.addFilm(new Film(filmName, duration, genreName));



        }

        try {
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}