package database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;

/**
 *
 * @author murabbiprogrammer
 */
public class Koneksi {

    public static ConnectionSource cs() {
        String dbName = "penyewaanlapanganfutsal-orml-java";
        String link = "jdbc:mysql://localhost:3306/" + dbName;
        String user = "root";
        String pass = "";

        ConnectionSource initCs = null;
        try {
            initCs = new JdbcConnectionSource(link, user, pass);

        } catch (SQLException ex) {
            System.out.println("Eror : " + ex);
        }
        return initCs;
    }
}
