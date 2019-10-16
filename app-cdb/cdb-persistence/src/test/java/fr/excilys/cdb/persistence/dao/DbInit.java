package fr.excilys.cdb.persistence.dao;

import java.time.LocalDate;

import fr.excilys.cdb.persistence.models.CompanyBuilder;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.ComputerBuilder;
import fr.excilys.cdb.persistence.models.ComputerEntity;

public class DbInit {
	
	public static final String QUNTUM_MAC = "QUNTUM MAC";
	public static final int UPDATE_OK = 1;
	public static final int VALUE_20 = 20;
	public static final int VALUE_10 = 10;
	public static final int UPDATE_KO = 0;
	public static final int ID_100 = 100;
	public static final int PAGE_2 = 2;
	public static final int PAGE_1 = 1;
	public static final int SIZE_5 = 5;
	public static final LocalDate DISCONTINUED = LocalDate.of(1993, 10, 1);
	public static final LocalDate INTRODUCED = LocalDate.of(1977, 4, 1);
	public static final long ID_6 = 6;
	public static final long ID_1 = 1;
	public static final String APPLE_II = "Apple II";
	public static final String APPLE_INC = "Apple Inc.";
	
	public static final CompanyEntity COMPANY = CompanyBuilder.newInstance()
																.setId(ID_1)
																.setName(APPLE_INC)
																.build();
	public static final ComputerEntity COMPUTER = ComputerBuilder.newInstance()
															 .setId(ID_6)
															 .setName(APPLE_II)
															 .setIntroduced(INTRODUCED)
															 .setDicontinued(DISCONTINUED)
															 .setCompany(COMPANY)
															 .build();
	
		
}
