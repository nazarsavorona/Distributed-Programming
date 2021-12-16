package dp.exam.task1;

import dp.exam.task2.CustomerManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServerSocketTask2 {
    private ServerSocket serverSocket;
    private final Executor executor;
    private final CustomerManager manager;
    public ServerSocketTask2(int port, int size) {
        executor = Executors.newSingleThreadExecutor();
        manager = new CustomerManager(size);
        try {
            serverSocket = new ServerSocket(port, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServerSocketTask2 server = new ServerSocketTask2(25565, 30);
        server.run();
    }
    public void run() {
        while (true){
            try {
                var client = serverSocket.accept();
                System.out.println(client + " connected!");
                executor.execute(new ClientRunnable(manager,client));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static class ClientRunnable implements Runnable{
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private final CustomerManager manager;
        private final Socket client;
        public ClientRunnable(CustomerManager manager, Socket client) {
            this.manager = manager;
            this.client = client;
            try {
                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private void getCustomers() throws IOException {
            var customers = manager.getCustomers();
            out.writeObject(Objects.requireNonNullElseGet(customers, ArrayList::new));
        }
        private void getSortedCustomers() throws IOException {
            var customers = manager.getSortedCustomers();
            out.writeObject(Objects.requireNonNullElseGet(customers, ArrayList::new));
        }
        private void getCustomersByCard() throws IOException, ClassNotFoundException {
            Integer start = (Integer) in.readObject();
            Integer end = (Integer) in.readObject();
            var customers = manager.getCustomersByCard(start,end);
            out.writeObject(Objects.requireNonNullElseGet(customers, ArrayList::new));
        }
        @Override
        public void run() {
            while (!Thread.interrupted()){
                try {
                    if(client.isClosed() || !client.isConnected()) break;
                    String code = (String)in.readObject();
                    System.out.println(">" + code);
                    switch (code) {
                        case "get customers" -> getCustomers();
                        case "get sorted customers" -> getSortedCustomers();
                        case "get customers by card" -> getCustomersByCard();
                        default -> out.writeObject(new ArrayList<>());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println(client + " disconnected!");
                    return;
                }
            }
        }
    }
}
