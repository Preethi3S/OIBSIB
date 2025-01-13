package ATM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Withdraw {

    public void withdrawAmount(int userID) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter the amount to withdraw: ");
            double withdrawAmount = sc.nextDouble();

            if (withdrawAmount <= 0) {
                System.out.println("Invalid amount. Please enter a positive value.");
                return;
            }

            // Check if sufficient balance is available
            Connection con = dbConnection.getConnection();
            String checkBalanceQuery = "SELECT accountBalance FROM accounts WHERE userID = ?";
            PreparedStatement checkBalanceStmt = con.prepareStatement(checkBalanceQuery);
            checkBalanceStmt.setInt(1, userID);
            ResultSet rs = checkBalanceStmt.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("accountBalance");

                if (withdrawAmount > currentBalance) {
                    System.out.println("Insufficient balance. Transaction canceled.");
                    return;
                }

                // Update account balance
                String updateBalanceQuery = "UPDATE accounts SET accountBalance = accountBalance - ? WHERE userID = ?";
                PreparedStatement updateBalanceStmt = con.prepareStatement(updateBalanceQuery);
                updateBalanceStmt.setDouble(1, withdrawAmount);
                updateBalanceStmt.setInt(2, userID);
                updateBalanceStmt.executeUpdate();

                // Log transaction
                logTransaction(userID, "Withdraw", withdrawAmount);

                System.out.println("Amount withdrawn successfully!");
            } else {
                System.out.println("Account not found.");
            }

            con.close();
        } catch (SQLException e) {
            System.err.println("Error during withdrawal: " + e.getMessage());
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

