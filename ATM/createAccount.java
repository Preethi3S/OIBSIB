package ATM;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class createAccount {
    public void Details() {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter user ID (positive number): ");
            int userID = sc.nextInt();
            sc.nextLine(); 

            System.out.print("Enter a 4-digit PIN: ");
            int userPin = sc.nextInt();
            sc.nextLine(); 

            System.out.print("Enter initial deposit amount (or 0): ");
            int accountBalance = sc.nextInt();
            sc.nextLine(); 

            System.out.print("Enter your name: ");
            String username = sc.nextLine();

            System.out.print("Enter branch name: ");
            String branch = sc.nextLine();

            if (!validateInputs(userID, userPin, accountBalance, username, branch)) {
                return;
            }

            System.out.println("\nCreating Your ACCOUNT\n");

            String hashedPin = hashPin(userPin); 
            if (isUserIdUnique(userID)) {
                createAccountInDatabase(userID, hashedPin, accountBalance, username, branch);
                System.out.println("-------------------------------------------------------------------------------------------------------------");
                System.out.println("Your Account has been created successfully!");
                System.out.println("-------------------------------------------------------------------------------------------------------------");

                Login login = new Login(userID, userPin);
                login.loginUser();
            } else {
                System.out.println("User ID already exists. Please choose a different User ID.");
            }
        } catch (SQLException e) {
            System.err.println("Error while creating the account: " + e.getMessage());
        } finally {
            sc.close();
        }
    }

    private boolean validateInputs(int userID, int userPin, int accountBalance, String username, String branch) {
        if (userID <= 0) {
            System.out.println("User ID must be a positive number.");
            return false;
        }
        if (String.valueOf(userPin).length() != 4) {
            System.out.println("User PIN must be exactly 4 digits.");
            return false;
        }
        if (accountBalance < 0) {
            System.out.println("Initial deposit cannot be negative.");
            return false;
        }
        if (username.isEmpty() || branch.isEmpty()) {
            System.out.println("Username and branch name cannot be empty.");
            return false;
        }
        return true;
    }

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

    private boolean isUserIdUnique(int userID) throws SQLException {
        Connection con = dbConnection.getConnection();
        String query = "SELECT * FROM accounts WHERE userID = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, userID);
        ResultSet rs = pst.executeQuery();
        boolean isUnique = !rs.next(); 
        con.close();
        return isUnique;
    }

    private void createAccountInDatabase(int userID, String hashedPin, int accountBalance, String username, String branch) throws SQLException {
        Connection con = dbConnection.getConnection();
        String query = "INSERT INTO accounts (userID, username, userPin, accountBalance, branch) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, userID);
        pst.setString(2, username);
        pst.setString(3, hashedPin);
        pst.setInt(4, accountBalance);
        pst.setString(5, branch);
        pst.executeUpdate();
        con.close();
    }
}
