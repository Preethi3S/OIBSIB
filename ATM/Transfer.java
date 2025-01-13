package ATM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Transfer {

    public void transferAmount(int senderID) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter the recipient User ID: ");
            int recipientID = sc.nextInt();
            System.out.print("Enter the amount to transfer: ");
            double transferAmount = sc.nextDouble();

            if (transferAmount <= 0) {
                System.out.println("Invalid amount. Please enter a positive value.");
                return;
            }

            // Check sender's balance
            Connection con = dbConnection.getConnection();
            String checkBalanceQuery = "SELECT accountBalance FROM accounts WHERE userID = ?";
            PreparedStatement checkBalanceStmt = con.prepareStatement(checkBalanceQuery);
            checkBalanceStmt.setInt(1, senderID);
            ResultSet rs = checkBalanceStmt.executeQuery();

            if (rs.next()) {
                double senderBalance = rs.getDouble("accountBalance");

                if (transferAmount > senderBalance) {
                    System.out.println("Insufficient balance. Transaction canceled.");
                    return;
                }

                // Deduct from sender's account
                String deductBalanceQuery = "UPDATE accounts SET accountBalance = accountBalance - ? WHERE userID = ?";
                PreparedStatement deductBalanceStmt = con.prepareStatement(deductBalanceQuery);
                deductBalanceStmt.setDouble(1, transferAmount);
                deductBalanceStmt.setInt(2, senderID);
                deductBalanceStmt.executeUpdate();

                // Add to recipient's account
                String addBalanceQuery = "UPDATE accounts SET accountBalance = accountBalance + ? WHERE userID = ?";
                PreparedStatement addBalanceStmt = con.prepareStatement(addBalanceQuery);
                addBalanceStmt.setDouble(1, transferAmount);
                addBalanceStmt.setInt(2, recipientID);
                addBalanceStmt.executeUpdate();

                // Log transactions for both sender and recipient
                logTransaction(senderID, "Transfer (Sent)", transferAmount);
                logTransaction(recipientID, "Transfer (Received)", transferAmount);

                System.out.println("Amount transferred successfully!");
            } else {
                System.out.println("Sender account not found.");
            }

            con.close();
        } catch (SQLException e) {
            System.err.println("Error during transfer: " + e.getMessage());
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

