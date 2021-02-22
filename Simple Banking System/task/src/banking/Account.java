package banking;

import java.util.Random;

public class Account {
    private String cardNumber;
    private String pinCode;
    private int balance;

    private static String iin;
    private static int customerAccNumLen;
    private static int pinLength;

    static {
        iin = "400000";
        customerAccNumLen = 9;
        pinLength = 4;
    }

    public Account() {
        balance = 0;
        this.cardNumber = generateCardNumber();
        this.pinCode = generateSequence(pinLength);
    }

    public Account(String cardNumber, String pinCode, int balance) {
        this.cardNumber = cardNumber;
        this.pinCode = pinCode;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPinCode() {
        return pinCode;
    }

    public int getBalance() {
        return balance;
    }

    private String generateCardNumber() {
        StringBuilder result = new StringBuilder(iin);
        return result.append(
                Utils.generateLuhnCheckSum(
                        result.append(generateSequence(customerAccNumLen)).toString()
                )
        ).toString();
    }

    private String generateSequence(int amount) {
        Random rnd = new Random();
        StringBuilder sequence = new StringBuilder();
        for (int i = 0; i < amount; ++i) {
            sequence.append(rnd.nextInt(10));
        }
        return sequence.toString();
    }
}
