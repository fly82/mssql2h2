import models.ConnectDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[]args) throws ClassNotFoundException {
        Statement H2 = new ConnectDB().getStatementH2();
        Statement MSSQL = new ConnectDB().getStatementMSSQL();
        final String allYear2014 = "113214";
        final String SQL = "SELECT * FROM RC_4 WHERE Cdoc > "+allYear2014+" AND Fnum_51 = 'Контрольний' AND Fnum_3 IS NOT NULL";

        try {

            ResultSet rsMSSQL = MSSQL.executeQuery(SQL);
            while (rsMSSQL.next()) {
                String sql = "INSERT INTO appeals (id, name,date,address,number) " +
                        "VALUES ("+rsMSSQL.getString("Cdoc")+",'"+rsMSSQL.getString("Fnum_1").replaceAll("'","")+"','"+
                        rsMSSQL.getString("Fnum_3")+"','"+rsMSSQL.getString("Fnum_6")+"','"+rsMSSQL.getString("Fnum_118")+"')";
                System.out.println(sql);
                H2.executeUpdate(sql);
                }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            closeSilently(MSSQL);
            closeSilently(H2);
        }

    }

    private static void closeSilently(Statement stmt) {
        try {
            if (stmt != null) stmt.close();
        }
        catch (SQLException ignore) {
        }
    }
}
