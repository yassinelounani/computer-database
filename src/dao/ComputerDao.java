package dao;

import models.Computer;

import java.util.List;

public class ComputerDao implements Dao {

    private ConnectionDao connexion;

    private ComputerDao(ConnectionDao connexion) {
        super();
        this.connexion = connexion;
    }

    private static ComputerDao INSTANCE = null;

    public static synchronized ComputerDao getInstance(ConnectionDao connexion)
    {
        if (INSTANCE == null) {
            INSTANCE = new ComputerDao(connexion);
        }
        return INSTANCE;
    }

    public List<Computer> getComputers() {

    }

    public Computer getComputerById(long id) {

    }

    public void addComputer(Computer computer) {

    }

    public int deleteComputer(long id) {

    }

    public void updateComputer(Computer computer) {

    }


}
