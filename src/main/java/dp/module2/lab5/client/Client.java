package dp.module2.lab5.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import dp.module2.lab1.store.Film;
import dp.module2.lab1.store.Genre;
import dp.module2.lab3.common.QueryType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Client {
    private static final String splitter = "#";
    private Connection connection;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    Channel channelFromClient;
    Channel channelToClient;
    private String responseString;

    public Client() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.newConnection();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channelFromClient = connection.createChannel();
        channelToClient = connection.createChannel();
        channelFromClient.queueDeclare("fromClient", false, false, false, null);
        channelToClient.queueDeclare("toClient", false, false, false, null);
    }

    public boolean addGenre(Genre genre) throws IOException {
        List<String> response = new ArrayList<>(sendQuery(QueryType.ADD_GENRE, List.of(genre.getName())));
        int status = Integer.parseInt(popArgument(response));

        return status == 0;
    }

    public boolean deleteGenre(Genre genre) throws IOException {
        List<String> response = new ArrayList<>(sendQuery(QueryType.DELETE_GENRE, List.of(genre.getName())));
        int status = Integer.parseInt(popArgument(response));

        return status == 0;
    }

    public boolean addFilm(Film film) throws IOException {
        List<String> response = new ArrayList<>(sendQuery(QueryType.ADD_FILM, film.toList()));
        int status = Integer.parseInt(popArgument(response));

        return status == 0;
    }

    public boolean deleteFilm(Film film) throws IOException {
        List<String> response = new ArrayList<>(sendQuery(QueryType.DELETE_FILM, film.toList()));
        int status = Integer.parseInt(popArgument(response));

        return status == 0;
    }

    public boolean updateFilm(Film oldFilm, Film newFilm) throws IOException {
        List<String> arguments = oldFilm.toList();
        arguments.addAll(newFilm.toList());

        List<String> response = new ArrayList<>(sendQuery(QueryType.UPDATE_FILM, arguments));
        int status = Integer.parseInt(popArgument(response));

        return status == 0;
    }

    public int countFilmsByGenre(Genre genre) throws IOException {
        List<String> response = new ArrayList<>(sendQuery(QueryType.COUNT_FILMS_BY_GENRE, genre.toList()));
        int status = Integer.parseInt(popArgument(response));

        if (status == 0) {
            return Integer.parseInt(popArgument(response));
        }

        throw new RuntimeException("Error while counting films");
    }

    public Film getFilmByName(Film film) throws IOException {
        List<String> response = new ArrayList<>(sendQuery(QueryType.GET_FILM_BY_NAME, film.toList()));
        int status = Integer.parseInt(popArgument(response));

        if (status == 0) {
            return new Film(popArguments(response, Film.listSize()));
        }

        throw new RuntimeException("Error while getting film");
    }

    public List<Film> getFilmsByGenre(Genre genre) throws IOException {
        List<String> response = new ArrayList<>(sendQuery(QueryType.GET_FILMS_BY_GENRE, genre.toList()));
        int status = Integer.parseInt(popArgument(response));

        if (status == 0) {
            int count = Integer.parseInt(popArgument(response));

            List<Film> films = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                films.add(new Film(popArguments(response, Film.listSize())));
            }

            return films;
        }

        throw new RuntimeException("Error while getting films by genre");
    }

    public List<Genre> getGenres() throws IOException {
        List<String> response = new ArrayList<>(sendQuery(QueryType.GET_GENRES, List.of()));
        int status = Integer.parseInt(popArgument(response));

        if (status == 0) {
            List<Genre> genres = new ArrayList<>();
            int count = Integer.parseInt(popArgument(response));

            for (int i = 0; i < count; i++) {
                genres.add(new Genre(popArguments(response, Genre.listSize())));
            }
            return genres;
        }

        throw new RuntimeException("Error while getting genres. Error code: " + response);
    }

    private String popArgument(List<String> args) {
        String argument = args.get(0);
        args.remove(0);

        return argument;
    }

    private List<String> popArguments(List<String> args, int size) {
        List<String> arguments = new ArrayList<>(args.subList(0, size));
        for (int i = 0; i < size; i++) {
            args.remove(0);
        }

        return arguments;
    }

    private List<String> sendQuery(QueryType type, List<String> arguments) throws IOException {
        responseString = "";
        String query = type.name();

        if (arguments.size() > 0) {
            query += splitter + String.join(splitter, arguments);
        }

        try {
            channelFromClient.basicPublish("", "fromClient", null, query.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + query + "'\n");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                responseString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            };

            while (responseString.equals("")) {
                channelToClient.basicConsume("toClient", true, deliverCallback, consumerTag -> {
                });
            }

            return Arrays.stream(responseString.split(splitter)).toList();
        } catch (Exception e) {
            System.out.println(">>     " + e.getMessage());
        }

        throw new RuntimeException("Error while sending query");
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        Client client = new Client();

        for (Genre genre : client.getGenres()) {
            System.out.println(genre);
            System.out.printf("Count of films: %s%n", client.countFilmsByGenre(genre));

            for (Film film : client.getFilmsByGenre(genre)) {
                System.out.println(film);
                System.out.printf("Got by name: %s%n", client.getFilmByName(film));
            }
        }
    }
}
