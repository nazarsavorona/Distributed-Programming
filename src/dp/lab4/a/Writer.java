package dp.lab4.a;

import java.io.Serializable;
import java.util.StringJoiner;

public record Writer(String name, String phone) implements Serializable {

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Writer.class.getSimpleName() + " [", "]")
                .add(name)
                .add(phone)
                .toString();
    }
}
