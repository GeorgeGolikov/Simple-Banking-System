package banking;

public class Utils {
    public static boolean checkLuhnCheckSum(String number) {
        if (!number.matches("\\d{2,}")) {
            throw new RuntimeException("Input is not a number!");
        }
        int initialLength = number.length();
        int initialLastDig = Integer.parseInt(number.substring(initialLength - 1));
        number = number.substring(0, initialLength - 1);

        return initialLastDig == generateLuhnCheckSum(number);
    }

    public static int generateLuhnCheckSum(String number) {
        if (!number.matches("\\d+")) {
            throw new RuntimeException("Input is not a number!");
        }

        int counter = number.length();
        long num = Long.parseLong(number);
        int sum = 0;
        long lastDigit = 0;

        while (num > 0) {
            lastDigit = num % 10;
            if (counter % 2 != 0) {
                lastDigit *= 2;
                lastDigit -= lastDigit > 9 ? 9 : 0;
            }
            --counter;
            num /= 10;
            sum += lastDigit;
        }

        if (sum % 10 == 0) {
            return 0;
        }
        return 10 - (sum % 10);
    }
}
