package dp.exam.task2;

import dp.exam.Customer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CustomerManager  {
    private final List<Customer> customers;

    public CustomerManager(int size) {
        customers = new ArrayList<>();
        for(int i = 0; i < size; i++){
            customers.add(Customer.rand());
        }
    }
    public synchronized void addCustomer(Customer customer){
        customers.add(customer);
    }
    public synchronized List<Customer> getCustomers() {
        return customers;
    }
    public synchronized List<Customer> getSortedCustomers() {
        List<Customer> res = new ArrayList<>(customers);
        res.sort(Comparator.comparing(Customer::getName));
        return res;
    }
    public synchronized List<Customer> getCustomersByCard(int start, int end) {
        List<Customer> res = new ArrayList<>();
        customers.forEach(customer -> {
            if (customer.getCardNumber() >= start && customer.getCardNumber() <= end)
                res.add(customer);
        });
        return res;
    }
}
