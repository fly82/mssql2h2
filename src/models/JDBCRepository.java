package models;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.*;
import java.util.Map;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Iterables.transform;

public class JDBCRepository {
    @Autowired DataSource dataSource;
    public void convert() throws ClassNotFoundException {
        Connection conn = null;
        Statement stmtMSSQL = new ConnectDB().getStatementMSSQL();

        final String ALLYEAR2014 = "113214";
        final String MSSQL = "SELECT * FROM RC_4 WHERE Cdoc > "+ALLYEAR2014+" AND Fnum_51 = 'Контрольний' AND Fnum_3 IS NOT NULL";
        final String H2SQL = "INSERT INTO appeals (id, name, date, address, number) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = dataSource.getConnection();
            if (conn == null) System.out.println("null");
            PreparedStatement stmt = conn.prepareStatement(H2SQL);
            ResultSet rsMSSQL = stmtMSSQL.executeQuery(MSSQL);
            while (rsMSSQL.next()) {
                stmt.setString(1, rsMSSQL.getString("Cdoc"));
                stmt.setString(2, rsMSSQL.getString("Fnum_1").replaceAll("'", ""));
                stmt.setString(3, rsMSSQL.getString("Fnum_3"));
                stmt.setString(4, rsMSSQL.getString("Fnum_6"));
                stmt.setString(5, rsMSSQL.getString("Fnum_118"));

                stmt.setFloat(6, location(rsMSSQL.getString("Fnum_6")).latitude);
                stmt.setFloat(7, location(rsMSSQL.getString("Fnum_6")).longitude);
                stmt.execute();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            closeSilently(stmtMSSQL);
        }
    }
    private static void closeSilently(Statement stmt) {
        try {
            if (stmt != null) stmt.close();
        }
        catch (SQLException ignore) {
        }
    }

    private static String encodeParams(final Map<String, String> params) {
        final String paramsUrl = on('&').join(// получаем значение вида key1=value1&key2=value2...
                transform(params.entrySet(), new Function<Map.Entry<String, String>, String>() {

                    @Override
                    public String apply(final Map.Entry<String, String> input) {
                        try {
                            final StringBuffer buffer = new StringBuffer();
                            buffer.append(input.getKey());// получаем значение вида key=value
                            buffer.append('=');
                            buffer.append(URLEncoder.encode(input.getValue(), "utf-8"));// кодируем строку в соответствии со стандартом HTML 4.01
                            return buffer.toString();
                        } catch (final UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }));
        return paramsUrl;
    }

    public static Location location(String address) throws IOException {
        final String baseUrl = "http://maps.googleapis.com/maps/api/geocode/json";
        final Map<String, String> params = Maps.newHashMap();
        params.put("sensor", "false");
        params.put("address", "Україна, Полтава, " + address);
        final String url = baseUrl + '?' + encodeParams(params);
        System.out.println(url);
        final JSONObject response = JsonReader.read(url);
        JSONObject location = response.getJSONArray("results").getJSONObject(0);
        location = location.getJSONObject("geometry");
        location = location.getJSONObject("location");

        return new Location(location.getDouble("lng"), location.getDouble("lat"));
    }
}
