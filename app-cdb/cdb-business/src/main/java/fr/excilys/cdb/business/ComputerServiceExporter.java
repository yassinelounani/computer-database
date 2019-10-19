package fr.excilys.cdb.business;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.excilys.cdb.api.ComputerService;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Page;
import fr.excilys.cdb.api.exception.NotFoundCompanyException;
import fr.excilys.cdb.api.exception.NotFoundComputerException;
import fr.excilys.cdb.persistence.dao.CompanyDao;
import fr.excilys.cdb.persistence.dao.ComputerDao;
import fr.excilys.cdb.persistence.mappers.HelperDate;
import fr.excilys.cdb.persistence.mappers.Mapper;
import fr.excilys.cdb.persistence.models.CompanyBuilder;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.ComputerBuilder;
import fr.excilys.cdb.persistence.models.ComputerEntity;
import fr.excilys.cdb.persistence.models.Pageable;


public class ComputerServiceExporter implements ComputerService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerServiceExporter.class);
	
	private ComputerDao computerDao;
	private CompanyDao companyDao;
	
	
	static ComputerServiceExporter instance = null;

    public static synchronized ComputerServiceExporter getInstance() {
        if (instance == null) {
            instance = new ComputerServiceExporter();
        }
        return instance;
    }
    
	
	
	private ComputerServiceExporter() {
		super();
		this.computerDao = ComputerDao.getInstance();
		this.companyDao = CompanyDao.getInstance();
	}
	
	public List<Computer> getComputers() {
		List<ComputerEntity> computers = computerDao.getComputers();
		LOGGER.info("get all Computers from Dao Computer");
		return mapAll(computers);
	}
	
	public List<Computer> getComputersWithPage(Page page) {
		Pageable pageable = Mapper.map(page, Pageable.class);
		List<ComputerEntity> computers = computerDao.getComputersWithPage(pageable);
		LOGGER.info("get all Computers with page {} from Dao Computer", page.getNumber());
		return mapAll(computers);
	}
	
	public Optional<Computer> getComputerById(long id) {
		Optional<ComputerEntity> computerEntity = computerDao.getComputerById(id);
		LOGGER.info("get Computer with id : {} from Dao Computer", id);
		Computer computer =  Mapper.map(computerEntity, Computer.class);
		return Optional.ofNullable(computer);
	}
	
	public int addComputer(Computer computer) throws NotFoundCompanyException {
		String id = String.valueOf(computer.getId());
		ComputerEntity computerEntity = mapToComputerEntity(computer);
		if(Integer.parseInt(computer.getIdCompany()) > 0) {
			Optional<CompanyEntity> company = companyDao.getCompanyById(computer.getId());
			if(!company.isPresent()) {
					throw new NotFoundCompanyException("Company with id :"+ id+" not Exist referenced by company_id");
			} else {
				return computerDao.addComputer(computerEntity);
			}
		}
		else {
			return computerDao.addComputer(computerEntity);
		}		
	}
	
	public int deleteComputerById(long id) throws NotFoundComputerException {
		Optional<Computer> getComputer = getComputerById(id);
		if(getComputer.isPresent()) {
			throw new NotFoundComputerException("Company with id :"+ id +" not Exist");
		}
		return computerDao.deleteComputerById(id);
	}
	
	public int updateComputer(Computer computer) throws NotFoundComputerException {
		Optional<Computer> getComputer = getComputerById(computer.getId());
		if(!getComputer.isPresent()) {
			throw new NotFoundComputerException("Company with id :"+ computer.getId() +" not Exist");
		}
		computer = prepareComputerToUpdate(computer, getComputer.get());
		ComputerEntity entity = mapToComputerEntity(computer);
		return computerDao.updateComputer(entity);	
	}
	
	private Computer prepareComputerToUpdate(Computer computer, Computer getComputer) {
		if(computer.getName().isEmpty()) {
			computer.setName(getComputer.getName());
		}
		if(computer.getDateIntroduced() == null) {
			computer.setDateIntroduced(getComputer.getDateIntroduced());
		}
		if(computer.getDateDiscontinued() == null) {
			computer.setDateDiscontinued(getComputer.getDateDiscontinued());	
		}
		if(Long.parseLong(computer.getIdCompany()) <= 0) {
			computer.setIdCompany(computer.getIdCompany());
		}
		return computer;
	}

	private ComputerEntity mapToComputerEntity(Computer computer) {
		return ComputerBuilder.newInstance()
					.setId(computer.getId())
					.setName(computer.getName())
					.setCompany(mapToCompanyEntity(computer))
					.setIntroduced(HelperDate.stringDateToLocalDate(computer.getDateIntroduced()))
					.setDicontinued( HelperDate.stringDateToLocalDate(computer.getDateDiscontinued()))
					.build();
					
	}

	private CompanyEntity mapToCompanyEntity(Computer computer) {
		return CompanyBuilder.newInstance()
				.setId(Long.parseLong(computer.getIdCompany()))
				.setName(computer.getNameCompany())
				.build();
	}
	
	
				
	private Computer mapToComputer(ComputerEntity computerEntity) {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<ComputerEntity, Computer> typeMap = modelMapper.createTypeMap(ComputerEntity.class, Computer.class);
		typeMap.addMappings(mapper -> {
			    mapper.map(src -> src.getCompany().getId(), Computer::setId);
		});
		return modelMapper.map(computerEntity, Computer.class);
	}
	
	public List<Computer> mapAll(List<ComputerEntity> entityList) {
        return entityList.stream()
                .map(entity -> mapToComputer(entity))
                .collect(Collectors.toList());
    }
		 

}
