package banking;

import java.util.Random;

public class Util {

    public static String generatePIN() {
        Random random = new Random();

        StringBuilder pin = new StringBuilder();
        for (int i = 1; i <= 4; i++) {
            pin.append(random.nextInt(10));
        }

        return pin.toString();
    }

    public static String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder("400000");
        Random random = new Random();
        int[] digits = new int[9];
        for (int i = 0; i < 9; i++) {
            int next = random.nextInt(10);
            digits[i] = next;
            cardNumber.append(next);
        }

        for (int i = 0; i < 9; i++) {
            if ((i + 1) % 2 == 1) {
                digits[i] *= 2;
            }
        }

        for (int i = 0; i < 9; i++) {
            if (digits[i] > 9) {
                digits[i] -= 9;
            }
        }

        int sum = 8;
        for (int digit : digits) {
            sum += digit;
        }

        int reminder = sum % 10;
        if (reminder != 0) {
            cardNumber.append(10 - reminder);
        } else {
            cardNumber.append(0);
        }

        return cardNumber.toString();
    }

    public static boolean isValidCardNumber(String cardNumber) {
        int sum = 0;

        for (int i = 0; i < cardNumber.length() - 1; i++) {
            int digit = Integer.parseInt(cardNumber.substring(i, i + 1));

            if ((i + 1) % 2 == 1) {
                digit = digit * 2;
            }

            if (digit > 9) {
                digit -= 9;
            }

            sum += digit;
        }

        sum += Integer.parseInt(cardNumber.substring(cardNumber.length() - 1));

        return sum % 10 == 0;
    }

}
