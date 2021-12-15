package web.servlets;

import com.company.logic.EmployeeServlet;
import dp.module2.lab1.store.Film;
import dp.module2.lab1.store.Genre;
import dp.module2.lab2.VideoStoreDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(
        name = "Film Servlet",
        urlPatterns = "/film"
)
public class FilmServlet extends HttpServlet {
    private final VideoStoreDAO videoStore;

    public FilmServlet() throws Exception {
        videoStore = new VideoStoreDAO("VideoStore", "localhost", 5432);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        System.out.println(method);
        if (method != null) {
            switch (method) {
                case "byId" -> searchFilmById(request, response);
                case "byName" -> searchFilmByName(request, response);
            }
        } else {
            forwardListFilms(request, response, videoStore.getFilms());
        }
    }

    private void searchFilmById(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        int filmId = Integer.parseInt(request.getParameter("filmId"));
        Film film = null;
        try {
            film = videoStore.getFilm(filmId);
        } catch (Exception ex) {
            Logger.getLogger(EmployeeServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        request.setAttribute("film", film);
        request.setAttribute("action", "update");
        String nextJSP = "/jsp/film/create-film.jsp";

        if (request.getParameter("operation").equals("update")) {
            request.setAttribute("action", "update");
            nextJSP = "/jsp/film/update-film.jsp";
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        dispatcher.forward(request, response);
    }

    private void searchFilmByName(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("filmName");
        List<Film> result = null;
        try {
            result = videoStore.getFilmByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        forwardListFilms(request, response, result);
    }

    private void forwardListFilms(HttpServletRequest request, HttpServletResponse response, List films)
            throws ServletException, IOException {
        String nextJSP = "/jsp/film/list-films.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        request.setAttribute("films", films);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "create":
                createFilmAction(request, response);
                break;
            case "update":
                try {
                    updateFilmAction(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "delete":
                deleteFilmByName(request, response);
                break;
        }

    }

    private void createFilmAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int filmId = Integer.parseInt(request.getParameter("filmId"));
        String name = request.getParameter("name");
        float duration = Float.parseFloat(request.getParameter("duration"));
        String genre = request.getParameter("genre");
        boolean success = videoStore.addFilm(new Film(name, duration, genre));

        request.setAttribute("filmId", filmId);
        String message = "";
        if (success)
            message = "The new film has been successfully created.";
        request.setAttribute("message", message);
        forwardListFilms(request, response, videoStore.getFilms());
    }

    private void updateFilmAction(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int filmId = Integer.parseInt(request.getParameter("filmId"));
        String name = request.getParameter("name");
        float duration = Integer.parseInt(request.getParameter("duration"));
        String genre = request.getParameter("genre");

        String newName = request.getParameter("newName");
        float newDuration = Integer.parseInt(request.getParameter("newDuration"));
        String newGenre = request.getParameter("newGenre");

        Film oldFilm = new Film(name, duration, genre);
        Film newFilm = new Film(newName, newDuration, newGenre);

        boolean success = videoStore.updateFilm(oldFilm, newFilm);
        String message = null;
        if (success) {
            message = "The film has been successfully updated.";
        }

        request.setAttribute("filmId", filmId);
        request.setAttribute("message", message);
        forwardListFilms(request, response, videoStore.getFilms());
    }

    private void deleteFilmByName(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");

        boolean confirm = videoStore.deleteFilm(name);

        if (confirm) {
            String message = "The film has been successfully deleted.";
            request.setAttribute("message", message);
        }

        forwardListFilms(request, response, videoStore.getFilms());
    }
}

