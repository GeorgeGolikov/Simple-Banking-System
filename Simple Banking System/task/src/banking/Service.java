package banking;

public class Service {
    public static boolean doTransfer(Account acc, String tableName, String cardNum,
                                        int money, DataAccess dao) {
        if (money > acc.getBalance()) {
            Menu.printFewMoney();
            return false;
        }

        if (dao.doTransfer(tableName, acc.getCardNumber(), cardNum, money)) {
            Menu.printSuccessOnTransfer();
            return true;
        }

        return false;
    }

    public static boolean checkLuhn(String cardNum) {
        if (!Utils.checkLuhnCheckSum(cardNum)) {
            Menu.printWrongCardForTransfer();
            return false;
        }
        return true;
    }

    public static boolean checkCardExisted(String tableName, String cardNum, DataAccess dao) {
        if (dao.getAccountByCardNum(tableName, cardNum) == null) {
            Menu.printCardDoesNotExist();
            return false;
        }
        return true;
    }

    public static boolean checkSameCard(Account acc, String cardNum) {
        if (cardNum.equals(acc.getCardNumber())) {
            Menu.printTransferSame();
            return false;
        }
        return true;
    }
}
