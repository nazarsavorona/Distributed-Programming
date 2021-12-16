package dp.exam;

import java.io.Serializable;
import java.util.Random;

public class Customer implements Serializable {
    private int id;
    private static int currentId = 0;
    private String name;
    private String address;
    private int cardNumber;

    public Customer(String name, String address, int cardNumber) {
        this.id = currentId++;
        this.name = name;
        this.address = address;
        this.cardNumber = cardNumber;
    }

    public Customer() {
    }
    public static String randString(){
        Random random = new Random();
        char[] word = new char[random.nextInt(8)+3];
        char[] first = new char[1];
        first[0] = (char)('A' + random.nextInt(26));
        for(int j = 0; j < word.length; j++)
        {
            word[j] = (char)('a' + random.nextInt(26));
        }
        return new String(first) + new String(word);
    }
    public static Customer rand(){
        Random random = new Random();
        return new Customer(randString(),randString(), random.nextInt(10000));
    }
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", cardNumber=" + cardNumber +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }
}
