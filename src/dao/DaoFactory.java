package dao;

public class DaoFactory {

    ConnectionDao connexion;

    public DaoFactory(ConnectionDao connexion) {
        this.connexion = connexion;
    }

    public Dao getDao(TypeDao type){
        if(type == null){
            return null;
        }
        if(type.equals(TypeDao.Computer)){
            return ComputerDao.getInstance(connexion);
        }
        else if(type.equals(TypeDao.COMPANY)) {
            return CompanyDao.getInstance(connexion);
        }

        return null;
    }
}
