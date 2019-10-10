package dao;

public class DaoFactory {

    ConnectionToDb connexion;

    public DaoFactory(ConnectionToDb connexion) {
        super();
    	this.connexion = connexion;
    }

    public Dao getDao(TypeDao type){
        if(type == null){
            return null;
        }
        if(type.equals(TypeDao.COMPUTER)){
            return ComputerDao.getInstance(connexion);
        }
        else if(type.equals(TypeDao.COMPANY)) {
            return CompanyDao.getInstance(connexion);
        }

        return null;
    }
}
