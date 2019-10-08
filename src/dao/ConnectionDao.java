package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static dao.Configuration.JDBC_DRIVER;
import static dao.Configuration.PASSWORD;
import static dao.Configuration.USER;

public class ConnectionDao {

    private String configuration;

    public ConnectionDao(String configuration) {
        this.configuration = configuration;
    }

    public Connection getConnectionDb() {

        Connection connexion = null;
        try{
             Class.forName(JDBC_DRIVER);
             String url = Configuration.getUrl();
             connexion = DriverManager.getConnection(url, USER, PASSWORD);

             return connexion;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connexion;

    }


    // chargement de la classe par son nom


}
