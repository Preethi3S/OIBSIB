package ATM;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TransactionHistory {
    public void showTransactionHistory(int userID) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // Establish database connection
            con = dbConnection.getConnection();
            String query = "SELECT transactionID, transactionAmount, transactionType, timestamp FROM transactions WHERE userID = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1, userID);

            rs = pst.executeQuery();

            System.out.println("-------------------------------------------------------------------------------------------------------------");
            System.out.println("Transaction History for User ID: " + userID);
            System.out.println("-------------------------------------------------------------------------------------------------------------");
            while (rs.next()) {
                int transactionID = rs.getInt("transactionID");
                double transactionAmount = rs.getDouble("transactionAmount");
                String transactionType = rs.getString("transactionType");
                Timestamp timestamp = rs.getTimestamp("timestamp");

                System.out.println("Transaction ID: " + transactionID);
                System.out.println("Transaction Amount: " + transactionAmount);
                System.out.println("Transaction Type: " + transactionType);
                System.out.println("Timestamp: " + timestamp);
                System.out.println("-------------------------------------------------------------------------------------------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving transaction history: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
