package fr.excilys.cdb.persistence.mappers;

import static fr.excilys.cdb.persistence.mappers.Mapper.mapResultSetToComputer;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import fr.excilys.cdb.persistence.models.ComputerEntity;

public class ComputerRowMapper implements RowMapper<ComputerEntity> {
	
	@Override
	public ComputerEntity mapRow(ResultSet pRS, int pRowNum) throws SQLException {
    	return mapResultSetToComputer(pRS);
    }

}
