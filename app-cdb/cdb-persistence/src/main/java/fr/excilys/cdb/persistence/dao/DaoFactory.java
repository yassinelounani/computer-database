package fr.excilys.cdb.persistence.dao;

import java.sql.Connection;
import java.util.Optional;

public class DaoFactory {

    ConnectionToDb connectionToDb;

    public DaoFactory(ConnectionToDb connectionToDb) {
        super();
    	this.connectionToDb = connectionToDb;
    }

    public Dao getDao(TypeDao type){
        if(type == null){
            return null;
        }
        if(type.equals(TypeDao.COMPUTER)){
            return ComputerDao.getInstance(connectionToDb);
        }
        else if(type.equals(TypeDao.COMPANY)) {
            return CompanyDao.getInstance(connectionToDb);
        }

        return null;
    }
}
