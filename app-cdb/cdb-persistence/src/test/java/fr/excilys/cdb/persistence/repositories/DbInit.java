package fr.excilys.cdb.persistence.repositories;

import java.time.LocalDate;

import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.CompanyEntity.CompanyBuilder;
import fr.excilys.cdb.persistence.models.ComputerEntity;
import fr.excilys.cdb.persistence.models.ComputerEntity.ComputerBuilder;

public class DbInit {
	
	private static final String APPLE_II = "Apple II";
	private static final String COMMODORE_INTERNATIONAL = "Commodore International";
	private static final String APPLE_INC = "Apple Inc.";
	public static final String QUNTUM_MAC = "QUNTUM MAC";
	public static final int UPDATE_OK = 1;
	public static final int VALUE_20 = 20;
	public static final int VALUE_10 = 10;
	public static final int UPDATE_KO = 0;
	public static final long ID_31 = 31;
	public static final int PAGE_2 = 2;
	public static final int PAGE_1 = 1;
	public static final int SIZE_5 = 5;
	public static final LocalDate DISCONTINUED = LocalDate.of(1993, 10, 1);
	public static final LocalDate INTRODUCED = LocalDate.of(1977, 4, 1);
	public static final long ID_2 = 2;
	public static final long ID_1 = 1;
	public static final long ID_6 = 6;
	public static final String CM_2A = "CM-2a";
	public static final String THINKING_MACHINES = "Thinking Machines";
	
	public static final CompanyEntity COMPANY = CompanyBuilder.newInstance()
																.setId(ID_2)
																.setName(THINKING_MACHINES)
																.build();

	public static final CompanyEntity APPLE = CompanyBuilder.newInstance()
																.setId(ID_1)
																.setName(APPLE_INC)
																.build();
	public static final CompanyEntity COMOMMODORE = CompanyBuilder.newInstance()
																.setId(ID_6)
																.setName(COMMODORE_INTERNATIONAL)
																.build();
	
	public static final ComputerEntity COMPUTER = ComputerBuilder.newInstance()
															 .setId(ID_2)
															 .setName(CM_2A)
															 .setIntroduced(INTRODUCED)
															 .setDicontinued(DISCONTINUED)
															 .setCompany(COMPANY)
															 .build();
	
	public static final ComputerEntity APPLE_COMP = ComputerBuilder.newInstance()
			 .setId(ID_6)
			 .setName(APPLE_II)
			 .setIntroduced(INTRODUCED)
			 .setDicontinued(DISCONTINUED)
			 .setCompany(APPLE)
			 .build();
	
}
