package model;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.*;

public class JDBCRepository {
    final String BEGIN2014YEAR = "113214";
    final String GEOPOSITION = "Україна, Полтава, ";
    final String BASEURL = "http://maps.googleapis.com/maps/api/geocode/json?address=";

    public void convert() throws ClassNotFoundException, SQLException {
        Connection connH2 = null;
        Statement stmtMSSQL = new ConnectDB().getStatementMSSQL();
        final String H2SQL = "INSERT INTO appeals (id, name, date, address, number,lat,lon) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Class.forName("org.h2.Driver");
            connH2 = DriverManager.getConnection("jdbc:h2:.appeals","sa","sa");

            String sqlMSSQL = "SELECT * FROM RC_4 WHERE Cdoc > "+lastRecord(connH2)+" AND Fnum_51 = 'Контрольний' AND Fnum_3 IS NOT NULL";

            PreparedStatement stmtH2 = connH2.prepareStatement(H2SQL);
            ResultSet rsMSSQL = stmtMSSQL.executeQuery(sqlMSSQL);
            Location tempLocation = new Location(0,0);
            while (rsMSSQL.next()) {
                tempLocation = geoLocation(rsMSSQL.getString("Fnum_6"));
                stmtH2.setString(1, rsMSSQL.getString("Cdoc"));
                stmtH2.setString(2, rsMSSQL.getString("Fnum_1").replaceAll("'", ""));
                stmtH2.setString(3, rsMSSQL.getString("Fnum_3"));
                stmtH2.setString(4, rsMSSQL.getString("Fnum_6"));
                stmtH2.setString(5, rsMSSQL.getString("Fnum_118"));
                stmtH2.setFloat(6, tempLocation.latitude);
                stmtH2.setFloat(7, tempLocation.longitude);
                stmtH2.execute();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            if (stmtMSSQL != null) stmtMSSQL.close();
            if (connH2 != null) connH2.close();
        }
    }

    private Location geoLocation(String address) throws IOException {

        final String url = BASEURL + URLEncoder.encode(GEOPOSITION + address, "utf-8") + "&sensor=false";
        System.out.println(url);
        final JSONObject response = JsonReader.read(url);
        JSONObject location = response.getJSONArray("results").getJSONObject(0);
        location = location.getJSONObject("geometry");
        location = location.getJSONObject("location");

        return new Location(location.getDouble("lng"), location.getDouble("lat"));
    }

    private String lastRecord(Connection conn) throws SQLException {

        ResultSet rs = conn.createStatement().executeQuery("SELECT MAX(id) FROM appeals");
        rs.next();
        return rs.getString("MAX(id)") != null ? rs.getString("MAX(id)") : BEGIN2014YEAR;
    }
}
