import models.ConnectDB;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[]args) throws ClassNotFoundException {
        java.sql.Statement H2 = new ConnectDB().getStatementH2();
        Connection MSSQL = new ConnectDB().getConnectionMSSQL();
        final String allYear2014 = "113214";
        final String SQL = "SELECT * FROM RC_4 WHERE Cdoc > "+allYear2014+" AND Fnum_51 = 'Контрольний' AND Fnum_3 IS NOT NULL";

        try {

            ResultSet rsMSSQL = MSSQL.createStatement().executeQuery(SQL);
            while (rsMSSQL.next()) {
                //System.out.println(rsMSSQL.getString("Cdoc") + " " + rsMSSQL.getString("Fnum_3"));
                String sql = "INSERT INTO appeals VALUES ('fgjkd')";
                //+rsMSSQL.getString("Fnum_3")+","+rsMSSQL.getString("Fnum_6")+","+
                 //       rsMSSQL.getString("Fnum_118")+")";
                System.out.print(sql);
                H2.executeUpdate(sql);


                //Long.parseLong(rsMSSQL.getString("Cdoc")), rsMSSQL.getString("Fnum_1"),
                //rsMSSQL.getString("Fnum_3"), rsMSSQL.getString("Fnum_6"), rsMSSQL.getString("Fnum_118"),

                    }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
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
