package model;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCRepository {
    final String BEGIN2014YEAR = "112616";//"113214";
    final String PREFIXQUERTY ="SELECT * FROM RC_15 WHERE ";
    final String GEOPOINTOFREF = "Україна, Полтава, ";
    final String BASEURL = "http://maps.googleapis.com/maps/api/geocode/json?address=";

    final public void convert() throws ClassNotFoundException, SQLException {
        Connection connH2 = null;
        Statement stmtMSSQL = new ConnectDB().getStatementMSSQL();
        String sqlMSSQL;
        ResultSet rsMSSQL;
        StringBuffer str = new StringBuffer();
        List<String> list = new ArrayList<>();

        final String H2SQL =
                "INSERT INTO appeals (id, name, date, address, number,lat,lon) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Class.forName("org.h2.Driver");
            connH2 = DriverManager.getConnection("jdbc:h2:.ticket","sa","sa");
            String firstRec = ext(connH2, Extremum.MIN);
            String lastRec = ext(connH2, Extremum.MAX);

            sqlMSSQL = PREFIXQUERTY + "Fnum_14 = 'Виконано' AND Cdoc BETWEEN " + firstRec + " AND " + lastRec;
            rsMSSQL = stmtMSSQL.executeQuery(sqlMSSQL);
            while (rsMSSQL.next()) { str.append(rsMSSQL.getString("Cdoc")).append(","); }
            str.deleteCharAt(str.length()-1);
            connH2.createStatement().execute("DELETE FROM appeals WHERE ID IN (" + str.toString() + ")");

            sqlMSSQL = PREFIXQUERTY + "Cdoc > " + lastRec + " AND (Fnum_14 != 'Виконано' OR Fnum_14 IS NULL) AND Fnum_8 IS NOT NULL";
            PreparedStatement stmtH2 = connH2.prepareStatement(H2SQL);
            rsMSSQL = stmtMSSQL.executeQuery(sqlMSSQL);
            Location tempLocation;
            String address, event, date, time;

            while (rsMSSQL.next()) {
                System.out.println(rsMSSQL.getString("Fnum_14") != null ? rsMSSQL.getString("Fnum_14") : "null");
                stmtH2.setString(1, rsMSSQL.getString("Cdoc"));
                event = rsMSSQL.getString("Fnum_15") != null ? rsMSSQL.getString("Fnum_15").replaceAll("'", "") : "";
                stmtH2.setString(2, event);
                date = rsMSSQL.getString("Fnum_3") != null ? rsMSSQL.getString("Fnum_3") : "";
                time = rsMSSQL.getString("Fnum_2") != null ? rsMSSQL.getString("Fnum_2") : "";
                stmtH2.setString(3, date + " " + time);
                address  = rsMSSQL.getString("Fnum_8");
                stmtH2.setString(4, address);
                stmtH2.setString(5, rsMSSQL.getString("Fnum_89"));
                tempLocation = geoLocation(address);
                stmtH2.setFloat(6, tempLocation.latitude);
                stmtH2.setFloat(7, tempLocation.longitude);
                stmtH2.execute();
                Thread.sleep(999);
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
     

    final private Location geoLocation(final String address) throws IOException {

        final String url = BASEURL + URLEncoder.encode(GEOPOINTOFREF + address, "utf-8") + "&sensor=false";
        final JSONObject response = JsonReader.read(url);
        JSONObject location = response.getJSONArray("results").getJSONObject(0);
        location = location.getJSONObject("geometry");
        location = location.getJSONObject("location");

        return new Location(location.getDouble("lng"), location.getDouble("lat"));
    }

    final private String ext(final Connection conn, final Extremum ext) throws SQLException {

        System.out.println(ext.toString());
        ResultSet rs = conn.createStatement().executeQuery("SELECT " + ext.toString() + "(id) AS EXT FROM appeals");
        rs.next();
        return rs.getString("EXT") != null ? rs.getString("EXT") : BEGIN2014YEAR;
    }
}
