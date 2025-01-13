package ATM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Deposit {

    public void depositAmount(int userID) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter the amount to deposit: ");
            double depositAmount = sc.nextDouble();

            if (depositAmount <= 0) {
                System.out.println("Invalid amount. Please enter a positive value.");
                return;
            }

            // Update account balance
            Connection con = dbConnection.getConnection();
            String updateBalanceQuery = "UPDATE accounts SET accountBalance = accountBalance + ? WHERE userID = ?";
            PreparedStatement updateBalanceStmt = con.prepareStatement(updateBalanceQuery);
            updateBalanceStmt.setDouble(1, depositAmount);
            updateBalanceStmt.setInt(2, userID);
            updateBalanceStmt.executeUpdate();

            // Log transaction
            logTransaction(userID, "Deposit", depositAmount);

            System.out.println("Amount deposited successfully!");

            con.close();
        } catch (SQLException e) {
            System.err.println("Error during deposit: " + e.getMessage());
        }
    }

    private void logTransaction(int userID, String transactionType, double amount) throws SQLException {
        Connection con = dbConnection.getConnection();
        String insertTransactionQuery = "INSERT INTO transactions (userID, transactionType, transactionAmount) VALUES (?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(insertTransactionQuery);
        pst.setInt(1, userID);
        pst.setString(2, transactionType);
        pst.setDouble(3, amount);
        pst.executeUpdate();
        con.close();
    }
}
