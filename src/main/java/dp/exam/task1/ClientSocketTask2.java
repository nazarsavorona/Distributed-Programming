package dp.exam.task1;

import dp.exam.Customer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientSocketTask2 {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientSocketTask2(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<Customer> getCustomers() {
        try {
            out.writeObject("get customers");
            return (List<Customer>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    List<Customer> getSortedCustomers() {
        try {
            out.writeObject("get sorted customers");
            return (List<Customer>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    List<Customer> getCustomersByCard(int start, int end) {
        try {
            out.writeObject("get customers by card");
            out.writeObject(start);
            out.writeObject(end);
            return (List<Customer>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        ClientSocketTask2 client = new ClientSocketTask2("localhost", 25565);
        System.out.println(client.getCustomers());
        System.out.println(client.getSortedCustomers());
        System.out.println(client.getCustomersByCard(4000, 9000));
    }
}
