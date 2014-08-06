import model.JDBCRepository;
import model.Migrate;

import java.sql.SQLException;

public class Main {

    public static void main(String[]args) throws ClassNotFoundException, SQLException {
        Migrate repo = new JDBCRepository();
        repo.convert();
    }
}
