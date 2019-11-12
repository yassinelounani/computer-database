package fr.excilys.cdb.persistence.dao;


public class DaoFactory {

    ConnectionToDb connectionToDb;

    public DaoFactory(ConnectionToDb connectionToDb) {
        super();
    	this.connectionToDb = connectionToDb;
    }
}
