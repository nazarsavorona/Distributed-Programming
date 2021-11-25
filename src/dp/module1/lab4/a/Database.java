package dp.module1.lab4.a;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database {
    public static void main(String[] args) throws IOException, InterruptedException {
        FileInteractor interactor = new FileInteractor("WritersDatabase.bin");
        interactor.clearFile();

        Thread managerThread = new Thread(() -> {
            List<String> names = new ArrayList<>(
                    Arrays.asList("Vasya", "Petya", "Vova", "Grysha",
                            "Semen", "Fedor", "Ivan", "Alexandr"));

            List<String> phones = new ArrayList<>(
                    Arrays.asList("380504440000", "380504441111", "380505550000", "380505551111", "380501234567",
                            "380509871890", "380090123455", "380965858444"));

            for (int i = 0, n = names.size(); i < n; i++) {
                interactor.writeToFile(new Writer(names.get(i), phones.get(i)));
            }

            interactor.removeByKey("Fedor", FileInteractor.Field.NAME);
            interactor.removeByKey("Semen", FileInteractor.Field.NAME);
            interactor.removeByKey("Vova", FileInteractor.Field.NAME);
            interactor.removeByKey("380504441111", FileInteractor.Field.PHONE);
            interactor.writeToFile(new Writer("Anton", "45684968896846"));
            interactor.removeByKey("380965858444", FileInteractor.Field.PHONE);
            interactor.removeByKey("380090123455", FileInteractor.Field.PHONE);
            interactor.writeToFile(new Writer("Stepan", "5745754685468"));
        });
        managerThread.start();
        Thread.sleep(1);

        Thread nameFinderThread = new Thread(() -> {
            List<String> names = new ArrayList<>(
                    Arrays.asList("Vova", "Grysha", "Fedor", "Alexandr"));
            for (String currentName : names) {
                System.out.printf("By name %s: %s%n", currentName,
                        interactor.findInFile(currentName, FileInteractor.Field.NAME));
            }
        });

        Thread phoneFinderThread = new Thread(() -> {
            List<String> phones = new ArrayList<>(
                    Arrays.asList("380504441111", "380504440000", "380509871890", "380090123455"));
            for (String currentPhone : phones) {
                System.out.printf("By phone %s: %s%n", currentPhone,
                        interactor.findInFile(currentPhone, FileInteractor.Field.PHONE));
            }
        });

        nameFinderThread.start();
        phoneFinderThread.start();
    }
}
