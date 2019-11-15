package fr.excilys.cdb.persistence.mappers;

import static fr.excilys.cdb.persistence.mappers.Mapper.mapResultSetToCompany;
import static fr.excilys.cdb.persistence.mappers.Mapper.ID_COMPANY;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import fr.excilys.cdb.persistence.models.CompanyEntity;

public class CompanyRowMapper implements RowMapper<CompanyEntity> {

	@Override
	public CompanyEntity mapRow(ResultSet pRS, int pRowNum) throws SQLException {
    	return mapResultSetToCompany(pRS, ID_COMPANY);
    }
}
