package ATM;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Login {
    private int userID;
    private String userPin;

    // Constructor
    public Login(int userID, int userPin) {
        this.userID = userID;
        this.userPin = hashPin(userPin); // Store the hashed PIN
    }

    public boolean loginUser() {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.println("-------------------------------------------------------------------------------------------------------------");
            System.out.println("Welcome to the Login Page\n");

            System.out.print("Enter your User ID: ");
            int enteredUserID = sc.nextInt();

            System.out.print("Enter your 4-digit PIN: ");
            int enteredPin = sc.nextInt();
            sc.nextLine(); // Consume the newline

            // Hash the entered PIN for comparison
            String hashedEnteredPin = hashPin(enteredPin);

            if (authenticateUser(enteredUserID, hashedEnteredPin)) {
                System.out.println("Login successful! Access granted to your account.\n");
                displayMenu(enteredUserID); // Call the method to display ATM functionalities
                return true;
            } else {
                System.out.println("Invalid User ID or PIN. Please try again.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
            return false;
        }
    }

    // Authenticate the user
    private boolean authenticateUser(int enteredUserID, String hashedPin) throws SQLException {
        Connection con = dbConnection.getConnection();
        String query = "SELECT * FROM accounts WHERE userID = ? AND userPin = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, enteredUserID);
        pst.setString(2, hashedPin);
        ResultSet rs = pst.executeQuery();

        boolean isAuthenticated = rs.next(); // If a record is found, the user is authenticated
        con.close();
        return isAuthenticated;
    }

    // Hash the PIN using SHA-256
    private String hashPin(int pin) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(String.valueOf(pin).getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing PIN: " + e.getMessage());
        }
    }

    // Display ATM functionalities menu
    private void displayMenu(int userID) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("-------------------------------------------------------------------------------------------------------------");
            System.out.println("Please choose one of the following options:");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.println("-------------------------------------------------------------------------------------------------------------");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    TransactionHistory history = new TransactionHistory();
                    history.showTransactionHistory(userID);
                    break;
                case 2:
                    Withdraw withdraw = new Withdraw();
                    withdraw.withdrawAmount(userID);
                    break;
                case 3:
                    Deposit deposit = new Deposit();
                    deposit.depositAmount(userID);
                    break;
                case 4:
                    Transfer transfer = new Transfer();
                    transfer.transferAmount(userID);
                    break;
                case 5:
                    System.out.println("Thank you for using our ATM system. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

