import models.JDBCRepository;

public class Main {

    public static void main(String[]args) throws ClassNotFoundException {
        JDBCRepository repo = new JDBCRepository();
        repo.convert();
    }
}
