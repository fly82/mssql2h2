package model;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.*;
import java.util.Map;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Iterables.transform;
import static java.util.Map.Entry;

public class JDBCRepository {
    final String BEGIN2014YEAR = "113214";
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
        final String baseUrl = "http://maps.googleapis.com/maps/api/geocode/json";
        final Map<String, String> params = Maps.newHashMap();
        params.put("sensor", "false");
        params.put("address", "Україна, Полтава, " + address);
        final String url = baseUrl + '?' + encodeParams(params);
        System.out.println(url);
        final JSONObject response = JsonReader.read(url);
        JSONObject location = response.getJSONArray("results").getJSONObject(0);
        location = location.getJSONObject("geometry");
        location = location.getJSONObject("geoLocation");

        return new Location(location.getDouble("lng"), location.getDouble("lat"));
    }

    private String encodeParams(final Map<String, String> params) {
        return on('&').join(transform(params.entrySet(), input -> {
                        try {
                            final StringBuffer buffer = new StringBuffer();
                            buffer.append(input.getKey());
                            buffer.append('=');
                            buffer.append(URLEncoder.encode(input.getValue(), "utf-8"));// стандарт HTML 4.01
                            return buffer.toString();
                        } catch (final UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                }));
    }

    private String lastRecord(Connection conn) throws SQLException {

        ResultSet rs = conn.createStatement().executeQuery("SELECT MAX(id) FROM appeals");
        rs.next();
        return rs.getString("MAX(id)") != null ? rs.getString("MAX(id)") : BEGIN2014YEAR;
    }
}
