package dp.exam.task2;

import dp.exam.Customer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerRmiTask2 extends UnicastRemoteObject implements CustomerManagerRemote {
    private final CustomerManager manager;
    public ServerRmiTask2(int size) throws RemoteException {
        manager = new CustomerManager(size);
    }

    @Override
    public List<Customer> getCustomers() throws RemoteException {
        return manager.getCustomers();
    }

    @Override
    public List<Customer> getSortedCustomers() throws RemoteException {
        return manager.getSortedCustomers();
    }

    @Override
    public List<Customer> getCustomersByCard(int start, int end) throws RemoteException {
        return manager.getCustomersByCard(start,end);
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("CustomerManager", new ServerRmiTask2(20));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
