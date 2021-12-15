package web.servlets;

import com.company.logic.EmployeeServlet;
import dp.module2.lab1.store.Genre;
import dp.module2.lab2.VideoStoreDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(
        name = "Genre Servlet",
        urlPatterns = "/genre"
)
public class GenreServlet extends HttpServlet {
    private final VideoStoreDAO videoStore;

    public GenreServlet() throws Exception {
        videoStore = new VideoStoreDAO("VideoStore", "localhost", 5432);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        System.out.println(method);
        if (method != null) {
            switch (method) {
                case "byId" -> searchGenreById(request, response);
                case "byName" -> searchGenreByName(request, response);
            }
        } else {
            forwardListGenres(request, response, videoStore.getGenres());
        }
    }

    private void searchGenreById(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Genre genre = null;
        try {
            genre = videoStore.getGenre(id);
        } catch (Exception ex) {
            Logger.getLogger(EmployeeServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        request.setAttribute("genre", genre);
        request.setAttribute("action", "update");
        String nextJSP = "/jsp/genre/create-genre.jsp";

        if(request.getParameter("operation").equals("update")){
            request.setAttribute("action", "update");
            nextJSP = "/jsp/genre/update-genre.jsp";
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        dispatcher.forward(request, response);
    }

    private void searchGenreByName(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("genreName");
        List<Genre> result = videoStore.getGenre(name);
        forwardListGenres(request, response, result);
    }

    private void forwardListGenres(HttpServletRequest request, HttpServletResponse response, List genres)
            throws ServletException, IOException {
        String nextJSP = "/jsp/genre/list-genres.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        request.setAttribute("genres", genres);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "create":
                createGenreAction(request, response);
                break;
            case "update":
                try {
                    updateGenreAction(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "delete":
                deleteGenreByName(request, response);
                break;
        }

    }

    private void createGenreAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int genreId = videoStore.addGenre(new Genre(request.getParameter("name")));

        request.setAttribute("genreId", genreId);
        String message = "The new genre has been successfully created.";
        request.setAttribute("message", message);
        forwardListGenres(request, response, videoStore.getGenres());
    }

    private void updateGenreAction(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String name = request.getParameter("name");
        int genreId = Integer.parseInt(request.getParameter("genreId"));

        boolean success = videoStore.updateGenre(videoStore.getGenre(genreId).getName(),
                                                new Genre(genreId, name));
        String message = null;
        if (success) {
            message = "The genre has been successfully updated.";
        }

        request.setAttribute("genreId", genreId);
        request.setAttribute("message", message);
        forwardListGenres(request, response, videoStore.getGenres());
    }

    private void deleteGenreByName(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int genreId = Integer.parseInt(request.getParameter("genreId"));
        boolean confirm = videoStore.deleteGenre(videoStore.getGenre(genreId).getName());

        if (confirm){
            String message = "The genre has been successfully deleted.";
            request.setAttribute("message", message);
        }

        forwardListGenres(request, response, videoStore.getGenres());
    }
}
