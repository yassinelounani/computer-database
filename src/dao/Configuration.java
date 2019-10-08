package dao;

public class Configuration {

    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String PORT = "3306";
    public static final String PROTOCOLE = "jdbc:mysql:";
    public static final String HOST = "localhost";
    public static final String DATABASE = "test_db";
    public static final String USER= "user";
    public static final String PASSWORD= "passwd";

    public static String getUrl(){

        StringBuilder url = new StringBuilder();
        url.append(PROTOCOLE)
           .append("//")
           .append(HOST)
           .append(":")
           .append(PORT)
           .append("/")
           .append(DATABASE);

        return url.toString();
    }
}
