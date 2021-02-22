package banking;

import java.sql.*;

public final class DataAccess {

    private static DataAccess driver;
    private final Connection connection;

    public static DataAccess getDataAccessObject(String url) throws SQLException {
        if (driver == null) driver = new DataAccess(url);
        return driver;
    }

    private Connection getNewConnection(String url) throws SQLException {
        return DriverManager.getConnection(url);
    }

    private DataAccess(String url) throws SQLException {
        connection = getNewConnection(url);
    }

    public void createTable(String tableName) {
//        String dropSql = "DROP TABLE IF EXISTS " + tableName + ";\n";
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n"
                + "	number TEXT UNIQUE,\n"
                + "	pin TEXT,\n"
                + " balance INTEGER DEFAULT 0\n"
                + ");";

        try(Statement stmt = driver.connection.createStatement();) {
            stmt.execute(sql);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCardToDB(String tableName, Account account) {
        String sql = "INSERT INTO " + tableName
                + "(number, pin, balance)\n"
                + "VALUES(?, ?, ?);";

        try(PreparedStatement pstmt = driver.connection.prepareStatement(sql);) {
            pstmt.setString(1, account.getCardNumber());
            pstmt.setString(2, account.getPinCode());
            pstmt.setInt(3, account.getBalance());
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Account getAccountByCardNum(String tableName, String cardNum) {
        String sql = "SELECT * FROM " + tableName + " WHERE number = " + cardNum;

        try(
                PreparedStatement pstmt = driver.connection.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
        ) {
            if (rs.next()) {
                return new Account(rs.getString(2), rs.getString(3), rs.getInt(4));
            }
            else {
                return null;
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public int addIncome(String tableName, String cardNum, int income) {
        String sql = "UPDATE " + tableName + " SET balance=balance + ? WHERE number=?;";

        try(PreparedStatement pstmt = driver.connection.prepareStatement(sql);) {
            pstmt.setInt(1, income);
            pstmt.setString(2, cardNum);

            return pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public boolean doTransfer(String tableName, String cardNumFrom, String cardNumTo, int money) {
        try {
            driver.connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

        String updateSenderSql = "UPDATE " + tableName + " SET balance=balance - ? WHERE number=?;";
        String updateReceiverSql = "UPDATE " + tableName + " SET balance=balance + ? WHERE number=?;";
        try(
                PreparedStatement updateSender = driver.connection.prepareStatement(updateSenderSql);
                PreparedStatement updateReceiver = driver.connection.prepareStatement(updateReceiverSql);
        ) {
            updateSender.setInt(1, money);
            updateSender.setString(2, cardNumFrom);
            updateSender.executeUpdate();

            updateReceiver.setInt(1, money);
            updateReceiver.setString(2, cardNumTo);
            updateReceiver.executeUpdate();

            driver.connection.commit();
        }
        catch(SQLException e) {
            if (driver.connection != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    driver.connection.rollback();
                }
                catch (SQLException excep) {
                    excep.printStackTrace();
                }
            }
            return false;
        }
        finally {
            try {
                driver.connection.setAutoCommit(true);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public int deleteAccount(String tableName, String cardNum) {
        String sql = "DELETE FROM " + tableName + " WHERE number=?;";

        try(PreparedStatement pstmt = driver.connection.prepareStatement(sql);) {
            pstmt.setString(1, cardNum);

            return pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
}