package models;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class ConnectDB {

    public ConnectDB() {
    }

    public java.sql.Statement getStatementH2() throws ClassNotFoundException {
        try {
            Class.forName("org.h2.Driver");
            System.out.println("Connected H2 database successfully...");
            return getConnection("jdbc:h2:../.appeals.h2.db", "sa", "sa").createStatement();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnectionMSSQL() throws ClassNotFoundException {
        final String JDBC_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
        final String DB_URL = "jdbc:jtds:sqlserver://192.168.200.3:1433";

        final String USER = "sa";
        final String PASS = "PrDFk267";

        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connected MSSQL database successfully...");
            return getConnection(DB_URL + ";DatabaseName=Zvertan", USER, PASS);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
