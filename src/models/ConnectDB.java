package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.DriverManager.getConnection;

public class ConnectDB {

    public ConnectDB() {
    }

    public Statement getStatementH2() throws ClassNotFoundException {
        try {
            Class.forName("org.h2.Driver");
            System.out.println("Connected H2 database successfully...");
            return getConnection("jdbc:h2:.appeals", "sa", "sa").createStatement();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Statement getStatementMSSQL() throws ClassNotFoundException {
        final String JDBC_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
        final String DB_URL = "jdbc:jtds:sqlserver://192.168.200.3:1433";

        final String USER = "sa";
        final String PASS = "PrDFk267";

        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connected MSSQL database successfully...");
            return getConnection(DB_URL + ";DatabaseName=Zvertan", USER, PASS).createStatement();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
