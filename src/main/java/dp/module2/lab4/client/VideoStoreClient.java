package dp.module2.lab4.client;

import dp.module2.lab1.store.Film;
import dp.module2.lab1.store.Genre;
import dp.module2.lab4.common.VideoStoreRemote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class VideoStoreClient {
    public static void main(String[] args) throws RemoteException {
        if (args.length != 1) {
            System.out.println("Usage: <SERVER IP>");
            System.exit(1);
        }
        VideoStoreRemote videoStore = null;
        try {
             videoStore = (VideoStoreRemote) Naming.lookup("//" + args[0] + "/VideoStoreServer");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }

        for(var genre : videoStore.getGenres()) {
            System.out.println(genre);

            for(var film : videoStore.getFilmsByGenre(genre.getName())) {
                System.out.println(film);
            }
        }

//        videoStore.addGenre(new Genre("Tragicomedy"));
//        videoStore.addFilm(new Film("Dog that died", 2.3f, "Tragicomedy"));
//
//        videoStore.updateFilm(new Film("Dog that died"), new Film("Dog Healed", 2.6f, "Comedy"));

        //videoStore.deleteGenre("Tragicomedy");
        System.out.println("\n");

        //System.out.println("\n" + videoStore.getFilmByName("Mission Impossible"));

        for(var genre : videoStore.getGenres()) {
            System.out.println(genre);

            for(var film : videoStore.getFilmsByGenre(genre.getName())) {
                System.out.println(film);
            }
        }
    }
}
