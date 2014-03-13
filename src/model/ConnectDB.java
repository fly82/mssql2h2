package model;

import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.DriverManager.getConnection;

public class ConnectDB {
    final String USER = "sa";
    final String PASS = "PrDFk267";
    final String DB = "SL080";

    public ConnectDB() {
    }

    public Statement getStatementMSSQL() throws ClassNotFoundException {
        final String JDBC_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
        final String DB_URL = "jdbc:jtds:sqlserver://192.168.200.3:1433";

        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connected MSSQL database successfully...");
            return getConnection(DB_URL + ";DatabaseName=" + DB, USER, PASS).createStatement();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
