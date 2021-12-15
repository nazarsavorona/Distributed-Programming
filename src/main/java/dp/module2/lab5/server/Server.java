package dp.module2.lab5.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import dp.module2.lab1.store.Film;
import dp.module2.lab1.store.Genre;
import dp.module2.lab2.VideoStoreDAO;
import dp.module2.lab3.common.QueryType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

public class Server {
    private static final String splitter = "#";
    private final VideoStoreDAO videoStore;
    private Connection connection;
    private ServerSocket server;
    private Socket clientSocket;
    private List<String> currentArguments;
    private String response;

    public Server(VideoStoreDAO videoStore) {
        this.videoStore = videoStore;
    }

    public void start() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();

        Channel channelFromClient = connection.createChannel();
        Channel channelToClient = connection.createChannel();
        channelFromClient.queueDeclare("fromClient", false, false, false, null);
        channelToClient.queueDeclare("toClient", false, false, false, null);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String query = new String(delivery.getBody(), StandardCharsets.UTF_8);

            System.out.println(" [x] Received '" + query + "'\n");
            String response = processQuery(query);

            channelToClient.basicPublish("", "toClient", null, response.getBytes(StandardCharsets.UTF_8));
        };
        channelFromClient.basicConsume("fromClient", true, deliverCallback, consumerTag -> {
        });

    }

    private String processQuery(String query) throws IOException {
        response = "0";
        try {
            currentArguments = new ArrayList<>(Arrays.stream(query.split(splitter)).toList());

            QueryType type = QueryType.valueOf(popArgument());

            switch (type) {
                case ADD_GENRE -> videoStore.addGenre(new Genre(popArgument()));
                case DELETE_GENRE -> videoStore.deleteGenre(popArgument());
                case ADD_FILM -> addFilm();
                case UPDATE_FILM -> updateFilm();
                case DELETE_FILM -> deleteFilm();
                case COUNT_FILMS_BY_GENRE -> countFilmsByGenre(response);
                case GET_FILM_BY_NAME -> getFilmByName(response);
                case GET_FILMS_BY_GENRE -> getFilmsByGenre(response);
                case GET_GENRES -> getGenres(response);
                default -> throw new IllegalStateException("Unexpected value: " + type);
            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return Integer.toString(1);
        }
    }

    private String popArgument() {
        String argument = currentArguments.get(0);
        currentArguments.remove(0);

        return argument;
    }

    private List<String> popArguments(int size) {
        List<String> arguments = new ArrayList<>(currentArguments.subList(0, size));
        for (int i = 0; i < size; i++) {
            currentArguments.remove(0);
        }

        return arguments;
    }

    private void addToResponse(String argument) {
        response += splitter + argument;
    }

    private void getGenres(String response) throws IOException {
        List<Genre> genres = videoStore.getGenres();

        addToResponse(Integer.toString(genres.size()));

        for (Genre genre : genres) {
            for (String argument : genre.toList())
                addToResponse(argument);
        }
    }

    private void getFilmsByGenre(String response) throws IOException {
        List<Film> films = videoStore.getFilmsByGenre(new Genre(popArguments(Genre.listSize())).getName());

        addToResponse(Integer.toString(films.size()));

        for (Film film : films) {
            for (String argument : film.toList())
                addToResponse(argument);
        }
    }

    private void getFilmByName(String response) throws IOException, SQLException {
        List<String> filmList =
                videoStore.getFilmByName(new Film(popArguments(Film.listSize())).getName()).get(0).toList();

        for (int i = 0, n = Film.listSize(); i < n; i++) {
            addToResponse(filmList.get(i));
        }
    }

    private void countFilmsByGenre(String response) throws IOException {
        addToResponse(Integer.toString(videoStore.getNumberOfFilmsByGenre(new Genre(currentArguments.subList(0,
                Genre.listSize())).getName())));
    }

    private void updateFilm() {
        videoStore.updateFilm(new Film(popArguments(Film.listSize())),
                new Film(popArguments(Film.listSize())));
    }

    private void deleteFilm() {
        videoStore.deleteFilm(new Film(popArguments(Film.listSize())).getName());
    }

    private void addFilm() {
        videoStore.addFilm(new Film(popArguments(Film.listSize())));
    }

    public void close() {
        try {
            connection.close();
            videoStore.stop();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(new VideoStoreDAO("VideoStore", "localhost", 5432));

        server.start();
        // server.close();
    }
}
