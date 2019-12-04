package fr.excilys.cdb.controllers;

import static org.springframework.http.ResponseEntity.ok;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.excilys.cdb.api.ComputerService;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Identifier;
import fr.excilys.cdb.api.dto.Navigation;
import fr.excilys.cdb.api.dto.PageDto;
import fr.excilys.cdb.api.dto.SortDto;
import fr.excilys.cdb.api.exception.NotFoundCompanyException;
import fr.excilys.cdb.api.exception.NotFoundComputerException;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/computers")
@CrossOrigin(origins = "http://localhost:4200")
public class ComputerController {

	private final ComputerService computerService;

	@Autowired
	public ComputerController(ComputerService computerService) {
		super();
		this.computerService = computerService;
	}

	@CrossOrigin
	@GetMapping
	@ApiOperation(value = "${swagger.computers}", notes = "${swagger.computers.desc}")
	public ResponseEntity<List<Computer>> getAll() {
		List<Computer> computers = computerService.getComputers();
		return ok().body(computers);
	}

	@CrossOrigin
	@ApiOperation(value = "${swagger.id}", notes = "${swagger.id.desc}")
	@GetMapping("/{id}")
	public ResponseEntity<Computer> getById(@PathVariable("id") final Long id) {
		Identifier computerId = new Identifier(id);
		Optional<Computer> computer = computerService.getComputerById(computerId);
		return ResponseEntity.of(computer);
	}

	@CrossOrigin
	@ApiOperation(value = "${swagger.page}", notes = "${swagger.page.desc}")
	@GetMapping("/page")
	public ResponseEntity<PageDto<Computer>> getAllWithPage(@Valid Navigation navigation) {
		PageDto<Computer> page = getPage(navigation);
		PageDto<Computer> pageDto = computerService.getComputersWithPage(page);
		return ok().body(pageDto);
	}

	@CrossOrigin
	@GetMapping("/find/{name}")
	@ApiOperation(value = "${swagger.find}", notes = "${swagger.find.desc}")
	public ResponseEntity<PageDto<Computer>> find(@PathVariable("name") final String name, @Valid Navigation navigation) {
		PageDto<Computer> page = getPage(navigation);
		PageDto<Computer> pageDto = computerService.getSerchComputersWithPage(page, name);
		return ok().body(pageDto);
	}

	@CrossOrigin
	@GetMapping(value = "/sort")
	@ApiOperation(value = "${swagger.sort}", notes = "${swagger.sort.desc}")
	public ResponseEntity<PageDto<Computer>> sort(@Valid Navigation navigation) {
		System.err.println(navigation);
		PageDto<Computer> page = getPage(navigation);
		SortDto sort = new SortDto(navigation.getProperty(), navigation.getOrder());
		PageDto<Computer> pageDto = computerService.getComputersWithPageAndSort(page, sort);
		return ok().body(pageDto);
	}

	@CrossOrigin
	@DeleteMapping("/delete/{id}")
	@ApiOperation(value = "${swagger.delete}", notes = "${swagger.delete.desc}")
	public ResponseEntity<HttpStatus> delete(@PathVariable(value="id") Long id) {
		Identifier computerId = new Identifier(id);
		try {
			computerService.deleteComputerById(computerId);
		} catch (NotFoundComputerException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@CrossOrigin
	@DeleteMapping("/delete/company/{id}")
	@ApiOperation(value = "${swagger.delete.comp}", notes = "${swagger.delete.comp.desc}")
	public ResponseEntity<HttpStatus> deleteCompanies(@PathVariable(value="id") Long id) {
		Identifier computerId = new Identifier(id);
		try {
			computerService.deleteCompany(computerId);
		} catch (NotFoundCompanyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@CrossOrigin
	@PostMapping("/add")
	@ApiOperation(value = "${swagger.add}", notes = "${swagger.add.desc}")
	public ResponseEntity<HttpStatus> add(@RequestBody @Valid Computer computer) {
		try {
			int addComputer = computerService.addComputer(computer);
			if (addComputer == 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			} else {
				return ResponseEntity.status(HttpStatus.CREATED).build();
			}
		} catch (NotFoundCompanyException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@CrossOrigin
	@PutMapping("/update")
	@ApiOperation(value = "${swagger.update}", notes = "${swagger.update.desc}")
	public ResponseEntity<HttpStatus> update(@RequestBody @Valid Computer computer) {
		try {
			int updateValue = computerService.updateComputer(computer);
			if ( updateValue == 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			} else {
				return ResponseEntity.status(HttpStatus.OK).build();
			}
		} catch (NotFoundComputerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	private PageDto<Computer> getPage(@Valid Navigation navigation) {
		PageDto.Builder<Computer> builder = new PageDto.Builder<>();
		return builder.setNumber(navigation.getNumber())
			.setSize(navigation.getSize())
			.build();
	}
} 
