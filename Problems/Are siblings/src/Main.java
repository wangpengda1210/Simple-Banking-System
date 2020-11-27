import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Siblings {

    public static boolean areSiblings(File f1, File f2) {
        return f1.getParent().equals(f2.getParent());
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(new File("dataset4.txt"));
            long largestIncrease = 0;
            int largestYear = 0;
            scanner.nextLine();
            scanner.nextInt();
            long lastPopulation = Long.parseLong(scanner.next()
                    .replaceAll(",", ""));
            while (scanner.hasNextLine()) {
                if (!scanner.hasNextInt()) {
                    break;
                }
                int year = scanner.nextInt();
                long thisPopulation = Long.parseLong(scanner.next()
                        .replaceAll(",", ""));
                if (thisPopulation - lastPopulation > largestIncrease) {
                    largestIncrease = thisPopulation - lastPopulation;
                    largestYear = year;
                }
                lastPopulation = thisPopulation;
            }
            System.out.println(largestYear);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}