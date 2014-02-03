import models.ConnectDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[]args) throws ClassNotFoundException {
        Connection H2 = new ConnectDB().getConnectionH2();
        Connection MSSQL = new ConnectDB().getConnectionMSSQL();
        final String allYear2014 = "113214";
        final String SQL = "SELECT * FROM RC_4 WHERE Cdoc > "+allYear2014+" AND Fnum_51 = 'Контрольний' AND Fnum_3 IS NOT NULL";

        try {
            ResultSet rs = MSSQL.createStatement().executeQuery(SQL);
            while (rs.next()) {
                System.out.println(rs.getString("Cdoc") + " " + rs.getString("Fnum_3"));
                    }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            closeSilently(H2);
            closeSilently(MSSQL);
        }

    }

    private static void closeSilently(Connection conn) {
        try {
            if (conn != null) conn.close();
        }
        catch (SQLException ignore) {
        }
    }
}
