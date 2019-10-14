package wrappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import models.Company;
import models.CompanyBuilder;
import models.Computer;
import models.ComputerBuilder;

public class Mapper {
	
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String INTRODUCED = "introduced";
	public static final String DISCONTINUED = "discontinued";
	public static final String COMPANY_ID = "company_id";
	
	
	public static Computer mapResultSetToComputer(ResultSet result) throws SQLException {
		Company company = mapResultSetToCompany(result, COMPANY_ID);
		return ComputerBuilder.newInstance()
							  .setId(result.getLong(ID))
							  .setName(result.getString(NAME))
							  .setIntroduced(result.getObject(INTRODUCED, LocalDate.class))
							  .setDicontinued(result.getObject(DISCONTINUED, LocalDate.class))
							  .setCompany(company)
							  .build();
		
	}
	
	public static Company mapResultSetToCompany(ResultSet result, String id) throws SQLException {
		return CompanyBuilder.newInstance()
				.setId(result.getLong(id))
				.setName(result.getString(NAME))
				.build();
	}
	
	
}
