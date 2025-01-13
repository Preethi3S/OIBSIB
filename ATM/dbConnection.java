package ATM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {

        private static final  String url = "*********" ;
        private static final  String user = "" ;
        private static final  String password = "";
    
    public static Connection getConnection() throws SQLException{
        return  DriverManager.getConnection(url, user, password);
    }
}