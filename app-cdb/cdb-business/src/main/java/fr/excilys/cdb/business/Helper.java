package fr.excilys.cdb.business;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.persistence.mappers.HelperDate;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.ComputerEntity;
import fr.excilys.cdb.persistence.models.CompanyEntity.CompanyBuilder;
import fr.excilys.cdb.persistence.models.ComputerEntity.ComputerBuilder;

public class Helper {

	public static ComputerEntity mapToComputerEntity(Computer computer) {
		return ComputerBuilder.newInstance()
					.setId(computer.getId())
					.setName(computer.getName())
					.setCompany(mapToCompanyEntity(computer))
					.setIntroduced(HelperDate.stringDateToLocalDate(computer.getIntroduced()))
					.setDicontinued( HelperDate.stringDateToLocalDate(computer.getDiscontinued()))
					.build();
					
	}

	public static CompanyEntity mapToCompanyEntity(Computer computer) {
		return CompanyBuilder.newInstance()
				.setId(computer.getIdCompany())
				.setName(computer.getNameCompany())
				.build();
	}

	public static Computer mapToComputer(ComputerEntity computerEntity) {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<ComputerEntity, Computer> typeMap = modelMapper.createTypeMap(ComputerEntity.class, Computer.class);
		typeMap.addMappings(mapper -> {
			    mapper.map(src -> src.getCompany().getId(), Computer::setIdCompany);
		});
		return modelMapper.map(computerEntity, Computer.class);
	}

	public static List<Computer> mapAll(List<ComputerEntity> entityList) {
        return entityList.stream()
                .map(entity -> mapToComputer(entity))
                .collect(Collectors.toList());
    }

	public static <E> boolean  isValidBean(E bean) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
        Set<ConstraintViolation<E>> violations = validator.validate(bean);
        return violations.isEmpty();
	}
}
