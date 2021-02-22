package banking;

public class Menu {
    public static final int createAccNum = 1;
    public static final int loginNum = 2;
    public static final int exitNum = 0;
    public static final int balanceNum = 1;
    public static final int addIncomeNum = 2;
    public static final int transferNum = 3;
    public static final int closeAccNum = 4;
    public static final int logoutNum = 5;

    private static String createAccStr;
    private static String loginStr;
    private static String exitStr;

    private static String[] loggedMenu;

    static {
        createAccStr = "1. Create an account\n";
        loginStr = "2. Log into account\n";
        exitStr = "0. Exit\n";

        loggedMenu = new String[] {
                "1. Balance\n",
                "2. Add income\n",
                "3. Do transfer\n",
                "4. Close account\n",
                "5. Log out\n"
        };
    }

    public static void printMenu() {
        System.out.print(createAccStr + loginStr + exitStr);
    }

    public static void printAnswerOnCreateAcc(String cardNumber, String pinCode) {
        String res = "Your card has been created\n" + "Your card number:\n" +
                cardNumber + "\nYour card PIN:\n" + pinCode + "\n";
        System.out.println(res);
    }

    public static void printAnswerOnLogInto() {
        System.out.println("Enter your card number:");
    }

    public static void printEnterPin() {
        System.out.println("Enter your PIN:");
    }

    public static void printWrongPin() {
        System.out.println("\nWrong card number or PIN!\n");
    }

    public static void printSuccessLogin() {
        System.out.println("\nYou have successfully logged in!\n");
    }

    public static void printMenuLogged() {
        System.out.println(loggedMenu[0] + loggedMenu[1] + loggedMenu[2] +
                            loggedMenu[3] + loggedMenu[4] + exitStr);
    }

    public static void printBalance(int balance) {
        System.out.println("\nBalance: " + balance + "\n");
    }

    public static void printLogOut() {
        System.out.println("\nYou have successfully logged out!\n");
    }

    public static void printWrongOption() {
        System.out.println("Wrong option!\n");
    }

    public static void printSayBye() {
        System.out.println("\nBye!");
    }

    public static void printAddIncome() {
        System.out.println("Enter income:\n");
    }

    public static void printIncomeAdded() {
        System.out.println("Income was added!\n");
    }

    public static void printTransfer() {
        System.out.println("Transfer\n" +
                "Enter card number:\n");
    }

    public static void printWrongCardForTransfer() {
        System.out.println("Probably you made a mistake in the card number. Please try again!\n");
    }

    public static void printCardDoesNotExist() {
        System.out.println("Such a card does not exist.\n");
    }

    public static void printTransferSame() {
        System.out.println("You can't transfer money to the same account!\n");
    }

    public static void printMoneyToTransfer() {
        System.out.println("Enter how much money you want to transfer:\n");
    }

    public static void printFewMoney() {
        System.out.println("Not enough money!\n");
    }

    public static void printSuccessOnTransfer() {
        System.out.println("Success!\n");
    }

    public static void printCloseAccSuccess() {
        System.out.println("The account has been closed!\n");
    }
}
