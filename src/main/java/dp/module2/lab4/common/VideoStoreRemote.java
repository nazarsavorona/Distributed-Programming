package dp.module2.lab4.common;

import dp.module2.lab1.store.Genre;
import dp.module2.lab1.store.Film;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface VideoStoreRemote extends Remote {
    void addGenre(Genre genre) throws RemoteException;

    void deleteGenre(String name) throws RemoteException;

    void addFilm(Film film) throws RemoteException;

    void deleteFilm(String name) throws RemoteException;

    void updateFilm(Film old, Film other) throws RemoteException;

    int countFilmsByGenre(String name) throws RemoteException;

    Film getFilmByName(String name) throws RemoteException;

    List<Film> getFilmsByGenre(String name) throws RemoteException;

    List<Genre> getGenres() throws RemoteException;
}
