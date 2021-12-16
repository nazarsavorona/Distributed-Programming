package dp.exam.task2;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientRmiTask2 {
    public static void main(String[] args) {
        try {
           CustomerManagerRemote managerRemote =
                    (CustomerManagerRemote) Naming.lookup("//localhost/CustomerManager");
           System.out.println(managerRemote.getCustomers());
            System.out.println(managerRemote.getSortedCustomers());
            System.out.println(managerRemote.getCustomersByCard(100,5000));
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
