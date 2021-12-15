package dp.module2.lab4.server;

import dp.module2.lab1.store.Film;
import dp.module2.lab1.store.Genre;
import dp.module2.lab2.VideoStoreDAO;
import dp.module2.lab4.common.VideoStoreRemote;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class VideoStoreServer extends UnicastRemoteObject implements VideoStoreRemote {
    private final VideoStoreDAO videoStore;

    protected VideoStoreServer(VideoStoreDAO videoStore) throws Exception {
        this.videoStore = videoStore;
    }

    @Override
    public void addGenre(Genre genre) throws RemoteException {
        synchronized (videoStore){
            videoStore.addGenre(genre);
        }
    }

    @Override
    public void deleteGenre(String genre) throws RemoteException {
        synchronized (videoStore){
            videoStore.deleteGenre(genre);
        }
    }

    @Override
    public void addFilm(Film film) throws RemoteException {
        synchronized (videoStore){
            videoStore.addFilm(film);
        }
    }

    @Override
    public void deleteFilm(String name) throws RemoteException {
        synchronized (videoStore){
            videoStore.deleteFilm(name);
        }
    }

    @Override
    public void updateFilm(Film old, Film other) throws RemoteException {
        synchronized (videoStore){
            videoStore.updateFilm(old, other);
        }
    }

    @Override
    public int countFilmsByGenre(String name) throws RemoteException {
        synchronized (videoStore){
            return videoStore.getNumberOfFilmsByGenre(name);
        }
    }

    @Override
    public Film getFilmByName(String name) throws RemoteException, SQLException {
        synchronized (videoStore){
            return videoStore.getFilmByName(name).get(0);
        }
    }

    @Override
    public List<Film> getFilmsByGenre(String name) throws RemoteException {
        synchronized (videoStore){
            return videoStore.getFilmsByGenre(name);
        }
    }

    @Override
    public List<Genre> getGenres() throws RemoteException {
        synchronized (videoStore){
            return videoStore.getGenres();
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            Registry r = LocateRegistry.createRegistry(1099);

            r.rebind("VideoStoreServer", new VideoStoreServer(new VideoStoreDAO("VideoStore", "localhost", 5432)));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
