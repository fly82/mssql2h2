import models.JDBCRepository;

import java.sql.SQLException;

public class Main {

    public static void main(String[]args) throws ClassNotFoundException, SQLException {
        JDBCRepository repo = new JDBCRepository();
        repo.convert();
    }
}
