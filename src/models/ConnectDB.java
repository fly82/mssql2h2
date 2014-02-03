package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class ConnectDB {

    public Connection getConnectionH2() throws ClassNotFoundException {
        try {
            Class.forName("org.h2.Driver");

            return getConnection("jdbc:h2:../.appeals.h2.db", "sa", "sa");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnectionMSSQL() throws ClassNotFoundException {
        final String SERVER = "sqlserver://192.168.200.3:1433";
        final String PASS = "PrDFk267";

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            return getConnection("jdbc:jtds:" + SERVER + ";DatabaseName=Zvertan", "sa", PASS);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
