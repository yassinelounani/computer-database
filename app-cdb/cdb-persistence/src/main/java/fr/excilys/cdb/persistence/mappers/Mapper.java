package fr.excilys.cdb.persistence.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import fr.excilys.cdb.persistence.models.CompanyBuilder;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.ComputerBuilder;
import fr.excilys.cdb.persistence.models.ComputerEntity;
import fr.excilys.cdb.persistence.models.Pageable;


public class Mapper {
	
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String INTRODUCED = "introduced";
	public static final String DISCONTINUED = "discontinued";
	public static final String COMPANY_ID = "company_id";
	
	private static ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

	
	
	public static ComputerEntity mapResultSetToComputer(ResultSet result) throws SQLException {
		CompanyEntity company = mapResultSetToCompany(result, COMPANY_ID);
		return ComputerBuilder.newInstance()
							  .setId(result.getLong(ID))
							  .setName(result.getString(NAME))
							  .setIntroduced(result.getObject(INTRODUCED, LocalDate.class))
							  .setDicontinued(result.getObject(DISCONTINUED, LocalDate.class))
							  .setCompany(company)
							  .build();
		
	}
	
	public static CompanyEntity mapResultSetToCompany(ResultSet result, String id) throws SQLException {
		return CompanyBuilder.newInstance()
				.setId(result.getLong(id))
				.setName(result.getString(NAME))
				.build();
	}
	
	public static <D, T> D map(final T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }
	

    public static <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outCLass) {
        return entityList.stream()
                .map(entity -> map(entity, outCLass))
                .collect(Collectors.toList());
    }

    public static <S, D> D map(final S source, D destination) {
        modelMapper.map(source, destination);
        return destination;
    }
    
    public static Pageable mapToPageable(int numPage, int size) {
    	return new Pageable(numPage, size);
    }
	
	
}
