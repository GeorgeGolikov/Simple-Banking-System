package banking;

import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exitFlag = false;

        String tableName = "card";
        DataAccess dao;
        try {
            // /home/george/Work/sqlite/
            // args: -filename card.s3db
            String connection = "jdbc:sqlite:" + args[1];
            dao = DataAccess.getDataAccessObject(connection);
        }
        catch (SQLException | RuntimeException e) {
            System.out.println(e.getMessage());
            return;
        }

        dao.createTable(tableName);

        while (!exitFlag) {
            Menu.printMenu();

            if (scanner.hasNextInt()) {
                int option = scanner.nextInt();

                switch (option) {
                    case Menu.createAccNum:
                        Account acc = new Account();
                        dao.addCardToDB(tableName, acc);
                        Menu.printAnswerOnCreateAcc(acc.getCardNumber(), acc.getPinCode());
                        break;
                    case Menu.loginNum:
                        String cardNumber;
                        String pinCode;

                        Menu.printAnswerOnLogInto();
                        if (scanner.hasNext()) {
                            cardNumber = scanner.next();
                        }
                        else {
                            Menu.printWrongPin();
                            break;
                        }

                        Menu.printEnterPin();
                        if (scanner.hasNext()) {
                            pinCode = scanner.next();
                        }
                        else {
                            Menu.printWrongPin();
                            break;
                        }

                        Account account = dao.getAccountByCardNum(tableName, cardNumber);
                        boolean refreshFlag = false;
                        if (account != null && pinCode.equals(account.getPinCode())) {
                            Menu.printSuccessLogin();

                            boolean loopStopFlag = false;
                            while (!loopStopFlag) {
                                Menu.printMenuLogged();
                                if (refreshFlag) {
                                    account = dao.getAccountByCardNum(tableName, cardNumber);
                                    refreshFlag = false;
                                }

                                int optionLogged;
                                if (scanner.hasNextInt()) {
                                    optionLogged = scanner.nextInt();
                                }
                                else {
                                    Menu.printWrongOption();
                                    scanner.next();
                                    continue;
                                }

                                switch (optionLogged) {
                                    case Menu.balanceNum:
                                        Menu.printBalance(account.getBalance());
                                        break;
                                    case Menu.addIncomeNum:
                                        Menu.printAddIncome();
                                        if (scanner.hasNextInt()) {
                                            int money = scanner.nextInt();
                                            if (money < 0) {
                                                Menu.printWrongOption();
                                                continue;
                                            }
                                            if (dao.addIncome(tableName, cardNumber, money) != 0) {
                                                Menu.printIncomeAdded();
                                                refreshFlag = true;
                                            }
                                        }
                                        else {
                                            Menu.printWrongOption();
                                            scanner.next();
                                            continue;
                                        }
                                        break;
                                    case Menu.transferNum:
                                        Menu.printTransfer();
                                        if (scanner.hasNext()) {
                                            String cardNum = scanner.next();
                                            if (!Service.checkLuhn(cardNum)) {
                                                continue;
                                            }
                                            if (!Service.checkCardExisted(tableName, cardNum, dao)) {
                                                continue;
                                            }
                                            if (!Service.checkSameCard(account, cardNum)) {
                                                continue;
                                            }

                                            Menu.printMoneyToTransfer();

                                            if (scanner.hasNextInt()) {
                                                int money = scanner.nextInt();
                                                if (money < 0) {
                                                    Menu.printWrongOption();
                                                    continue;
                                                }
                                                if (Service.doTransfer(account, tableName, cardNum, money, dao)) {
                                                    refreshFlag = true;
                                                }
                                            }
                                            else {
                                                Menu.printWrongOption();
                                                scanner.next();
                                                continue;
                                            }
                                        }
                                        break;
                                    case Menu.closeAccNum:
                                        if (dao.deleteAccount(tableName, cardNumber) != 0) {
                                            Menu.printCloseAccSuccess();
                                            loopStopFlag = true;
                                        }
                                        break;
                                    case Menu.logoutNum:
                                        Menu.printLogOut();
                                        loopStopFlag = true;
                                        break;
                                    case Menu.exitNum:
                                        Menu.printSayBye();
                                        loopStopFlag = true;
                                        exitFlag = true;
                                        break;
                                    default:
                                        Menu.printWrongOption();
                                }
                            }
                        }
                        else {
                            Menu.printWrongPin();
                        }

                        break;
                    case Menu.exitNum:
                        Menu.printSayBye();
                        exitFlag = true;
                        break;
                    default:
                        Menu.printWrongOption();
                }
            }
            else {
                Menu.printWrongOption();
                scanner.next();
            }
        }
    }
}
