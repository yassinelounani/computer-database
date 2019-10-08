package dao;

public class Configuration {

    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String PORT = "3306";
    public static final String PROTOCOLE = "jdbc:mysql:";
    public static final String HOST = "127.0.0.1";
    public static final String DATABASE = "computer-database-db";
    public static final String USER= "admincdb";
    public static final String PASSWORD= "qwerty1234";
    public static final String TIMEZONE = "serverTimezone=UTC";

    public static String getUrl(){

        StringBuilder url = new StringBuilder();
        url.append(PROTOCOLE)
           .append("//")
           .append(HOST)
           .append(":")
           .append(PORT)
           .append("/")
           .append(DATABASE)
           .append("?")
           .append(TIMEZONE);

        return url.toString();
    }
}
