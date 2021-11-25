package dp.module1.lab5.b;

public class StringWithCounting {
    private String string;
    private int As = 0;
    private int Bs = 0;

    StringWithCounting(String string) {
        this.string = string;

        setNumbers();
    }

    public boolean areABEqual() {
        return As == Bs;
    }

    public void setCharAtPos(char ch, int index) {
        char[] charArray = string.toCharArray();

        if (charArray[index] == 'A')
            As--;
        else if (charArray[index] == 'B')
            Bs--;

        if (ch == 'A')
            As++;
        else if (ch == 'B')
            Bs++;

        charArray[index] = ch;
        string = String.valueOf(charArray);
    }

    public char getCharAtPos(int index) {
        return string.toCharArray()[index];
    }

    private void setNumbers() {
        As = Bs = 0;

        for (char ch : string.toCharArray()) {
            if (ch == 'A') As++;
            else if (ch == 'B') Bs++;
        }
    }

    public int getAs() {
        return As;
    }

    public int getBs() {
        return Bs;
    }

    public String getString() {
        return string;
    }
}
