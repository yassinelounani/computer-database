package dao;

import models.Company;

import java.util.List;

public class CompanyDao implements Dao {

    private ConnectionDao connexion;

    private CompanyDao(ConnectionDao connexion) {
        super();
        this.connexion = connexion;
    }

    private static CompanyDao INSTANCE = null;

    public static synchronized CompanyDao getInstance(ConnectionDao connexion)
    {
        if (INSTANCE == null) {
            INSTANCE = new CompanyDao(connexion);
        }
        return INSTANCE;
    }

    public List<Company> getCompanies() {

    }


}
