package model;

import java.sql.SQLException;

public interface Migrate {
    public void convert() throws ClassNotFoundException, SQLException;
}
