package dp.module1.lab4.a;

import java.io.*;
import java.util.ArrayList;

public class FileInteractor {
    public enum Field {
        PHONE,
        NAME
    }

    private String fileName;
    private ObjectOutputStream output;
    private final RWLock lock;

    public FileInteractor(String fileName) {
        this.fileName = fileName;
        this.lock = new RWLock();

        try {
            output = new ObjectOutputStream(new FileOutputStream(fileName, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(Writer writer) {
        try {
            lock.lockWrite();
            output.writeObject(writer);
            lock.unlockWrite();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public Writer findInFile(String key, Field field) {
        try {
            lock.lockRead();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        try (
                FileInputStream istream = new FileInputStream(fileName);
                ObjectInputStream input = new ObjectInputStream(istream)
        ) {

            boolean isFound = false;
            Writer answer = new Writer("", "");
            while (!isFound && istream.available() > 0) {
                Writer writer = (Writer) input.readObject();

                if ((field == Field.NAME && writer.getName().equals(key)) ||
                        (field == Field.PHONE && writer.getPhone().equals(key))) {
                    answer = writer;
                    isFound = true;
                }
            }
            lock.unlockRead();
            return answer;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void clearFile() throws IOException {
        PrintWriter writer = new PrintWriter(fileName);
        writer.print("");
        writer.close();
        output = new ObjectOutputStream(new FileOutputStream(fileName, true));
    }

    private ArrayList<Writer> getAllItems(FileInputStream istream, ObjectInputStream input) throws IOException, ClassNotFoundException {
        ArrayList<Writer> array = new ArrayList<>();
        while (istream.available() > 0) {
            Writer buffer = (Writer) input.readObject();
            array.add(buffer);
        }
        return array;
    }


    public void removeByKey(String key, Field field) {
        try {
            lock.lockWrite();
            FileInputStream istream = new FileInputStream(fileName);
            ObjectInputStream input = new ObjectInputStream(istream);

            ArrayList<Writer> array = getAllItems(istream, input);

            int index = -1;
            if (field == Field.NAME) {
                for (int i = 0; i < array.size(); ++i) {
                    if (array.get(i).getName().equals(key)) {
                        index = i;
                        break;
                    }
                }
            } else if (field == Field.PHONE) {
                for (int i = 0; i < array.size(); ++i) {
                    if (array.get(i).getPhone().equals(key)) {
                        index = i;
                        break;
                    }
                }
            }

            if (index == -1) {
                lock.unlockWrite();
                return;
            }
            array.remove(index);

            clearFile();

            for (Writer item : array) {
                output.writeObject(item);
            }
            lock.unlockWrite();
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public void printFile() throws IOException, ClassNotFoundException, InterruptedException {
        lock.lockRead();
        FileInputStream istream = new FileInputStream(fileName);
        ObjectInputStream input = new ObjectInputStream(istream);

        while (istream.available() > 0) {
            Writer buffer = (Writer) input.readObject();
            System.out.println(buffer);
        }
        lock.unlockRead();
    }
}
