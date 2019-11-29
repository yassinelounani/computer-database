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
import org.springframework.data.domain.Page;

import fr.excilys.cdb.api.dto.Company;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.PageDto;
import fr.excilys.cdb.api.dto.PageDto.Builder;
import fr.excilys.cdb.api.dto.SortDto;
import fr.excilys.cdb.persistence.dao.SortDao;
import fr.excilys.cdb.persistence.mappers.HelperDate;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.CompanyEntity.CompanyBuilder;
import fr.excilys.cdb.persistence.models.ComputerEntity;
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
			    mapper.map(src -> src.getCompany().getName(), Computer::setNameCompany);
		});
		return modelMapper.map(computerEntity, Computer.class);
	}

	public static Company mapToCompany(CompanyEntity companyEntity) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(companyEntity, Company.class);
	}

	public static PageDto<Computer> mapAllComputersWithPage(Page<ComputerEntity> entry) {
		PageDto.Builder<Computer> builder = new Builder<>();
		return	builder.setNumber(entry.getNumber())
        		.setSize(entry.getSize())
        		.setTotalElement(entry.getTotalElements())
        		.setContent(mapContent(entry.getContent()))
        		.build();
    }

	public static PageDto<Company> mapAllCompaniesWithPage(Page<CompanyEntity> entry) {
		PageDto.Builder<Company> builder = new Builder<>();
		return	builder.setNumber(entry.getNumber())
        		.setSize(entry.getSize())
        		.setTotalElement(entry.getTotalElements())
        		.setContent(mapContentCompanies(entry.getContent()))
        		.build();
    }

	public static List<Computer> mapAll(List<ComputerEntity> entityList) {
        return entityList.stream()
                .map(entity -> mapToComputer(entity))
                .collect(Collectors.toList());
	}

	private static List<Computer> mapContent(List<ComputerEntity> list) {
		return list.stream()
               .map(entity -> mapToComputer(entity))
               .collect(Collectors.toList());
	}

	private static List<Company> mapContentCompanies(List<CompanyEntity> list) {
		return list.stream()
               .map(entity -> mapToCompany(entity))
               .collect(Collectors.toList());
	}

	public static <E> boolean  isValidBean(E bean) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
        Set<ConstraintViolation<E>> violations = validator.validate(bean);
        return violations.isEmpty();
	}

	public static SortDao mapToSortDao(SortDto sort) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(sort, SortDao.class);
	}
}
