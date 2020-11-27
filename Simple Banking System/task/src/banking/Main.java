package banking;

import java.util.HashMap;
import java.util.Scanner;

public class Main {

    static boolean exit = false;
    static Database database;

    public static void main(String[] args) {

        if (args.length != 2) {
            return;
        }

        database = new Database(args[1]);

        Scanner scanner = new Scanner(System.in);

        while (!exit) {
            System.out.println("\n1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit");

            switch (scanner.nextInt()) {
                case 0:
                    exit = true;
                    database.closeConnection();
                    break;
                case 1:
                    createAccount();
                    break;
                case 2:
                    logIn(scanner);
                    break;
            }
        }

        System.out.println("\nBye!");

    }

    private static void createAccount() {

        String cardNumber = Util.generateCardNumber();

        String pin = Util.generatePIN();

        System.out.printf("\nYour card has been created\n" +
                "Your card number:\n" +
                "%s\n" +
                "Your card PIN:\n" +
                "%s\n", cardNumber, pin);

        database.insert(cardNumber, pin, 0);

    }

    private static void logIn(Scanner scanner) {
        System.out.println("\nEnter your card number:");
        String cardNumber = String.valueOf(scanner.nextLong());

        System.out.println("Enter your PIN:");
        StringBuilder pin = new StringBuilder(String.valueOf(scanner.nextInt()));
        if (pin.length() < 4) {
            for (int i = 0; i < 4 - pin.length(); i++) {
                pin.insert(0, 0);
            }
        }

        String correctPin = database.queryPin(cardNumber);
        if (correctPin == null || !correctPin.equals(pin.toString())) {
            System.out.println("\nWrong card number or PIN!");
        } else {
            System.out.println("\nYou have successfully logged in!");

            outerLoop:while (true) {
                System.out.println("\n1. Balance\n" +
                        "2. Add income\n" +
                        "3. Do transfer\n" +
                        "4. Close account\n" +
                        "5. Log out\n" +
                        "0. Exit");

                switch (scanner.nextInt()) {
                    case 1:
                        System.out.println("\nBalance: " + getBalance(cardNumber));
                        break;
                    case 2:
                        System.out.println("\nEnter income:");
                        database.addIncome(cardNumber, scanner.nextInt());
                        System.out.println("Income was added!");
                        break;
                    case 3:
                        System.out.println("\nTransfer\nEnter card number:");
                        String toCardNumber = String.valueOf(scanner.nextLong());
                        if (!Util.isValidCardNumber(toCardNumber)) {
                            System.out.println("Probably you made mistake in the card number. " +
                                    "Please try again!");
                        } else if (!database.isCardNumberExists(toCardNumber)) {
                            System.out.println("Such a card does not exist.");
                        } else {
                            System.out.println("Enter how much money you want to transfer:");
                            if (!database.transfer(cardNumber, toCardNumber, scanner.nextInt())) {
                                System.out.println("Not enough money!");
                            } else {
                                System.out.println("Success!");
                            }
                        }
                        break;
                    case 4:
                        database.closeAccount(cardNumber);
                        System.out.println("\nThe account has been closed!");
                        break outerLoop;
                    case 5:
                        System.out.println("\nYou have successfully logged out!");
                        break outerLoop;
                    case 0:
                        exit = true;
                        database.closeConnection();
                        break outerLoop;
                }
            }
        }
    }

    private static int getBalance(String cardNumber) {
        int balance = database.queryBalance(cardNumber);
        return balance == -1 ? 0 : balance;
    }

}

