package fr.excilys.cdb.controllers;

import static org.springframework.http.ResponseEntity.ok;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
import fr.excilys.cdb.api.dto.FilterByProperty;
import fr.excilys.cdb.api.dto.Identifier;
import fr.excilys.cdb.api.dto.Navigation;
import fr.excilys.cdb.api.dto.PageDto;
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
	
	@Secured("ROLE_USER")
	@CrossOrigin
	@GetMapping
	@ApiOperation(value = "${swagger.computers}", notes = "${swagger.computers.desc}")
	public ResponseEntity<List<Computer>> getAll() {
		List<Computer> computers = computerService.getComputers();
		return ok().body(computers);
	}
	
	@Secured("ROLE_USER")
	@CrossOrigin
	@ApiOperation(value = "${swagger.id}", notes = "${swagger.id.desc}")
	@GetMapping("/{id}")
	public ResponseEntity<Computer> getById(@PathVariable("id") final Long id) {
		Identifier computerId = new Identifier(id);
		Optional<Computer> computer = computerService.getComputerById(computerId);
		return ResponseEntity.of(computer);
	}

	@Secured("ROLE_USER")
	@CrossOrigin
	@ApiOperation(value = "${swagger.page}", notes = "${swagger.page.desc}")
	@GetMapping("/page")
	public ResponseEntity<PageDto<Computer>> getAllWithPage(@Valid Navigation navigation) {
		PageDto<Computer> page = getPage(navigation);
		PageDto<Computer> pageDto = computerService.getComputersWithPage(page);
		return ok().body(pageDto);
	}

	@Secured("ROLE_USER")
	@CrossOrigin
	@GetMapping("/find")
	@ApiOperation(value = "${swagger.find}", notes = "${swagger.find.desc}")
	public ResponseEntity<PageDto<Computer>> find(@Valid Navigation navigation) {
		PageDto<Computer> page = getPage(navigation);
		PageDto<Computer> pageDto = computerService.getSerchComputersWithPage(page, navigation.getValue(), navigation.getProperty());
		return ok().body(pageDto);
	}

	@Secured("ROLE_USER")
	@CrossOrigin
	@GetMapping("/find/date")
	@ApiOperation(value = "${swagger.find}", notes = "${swagger.find.desc}")
	public ResponseEntity<PageDto<Computer>> findByDate(FilterByProperty filter, @Valid Navigation navigation) {
		PageDto<Computer> page = getPage(navigation);
		PageDto<Computer> pageDto = computerService.getcomputerByDate(page, navigation);
		return ok().body(pageDto);
	}

	@Secured("ROLE_USER")
	@CrossOrigin
	@GetMapping(value = "/sort")
	@ApiOperation(value = "${swagger.sort}", notes = "${swagger.sort.desc}")
	public ResponseEntity<PageDto<Computer>> sort(@Valid Navigation navigation) {
		PageDto<Computer> page = getPage(navigation);
		PageDto<Computer> pageDto = computerService.getComputersWithPageAndSort(page, navigation);
		return ok().body(pageDto);
	}

	@Secured("ROLE_USER")
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

	@Secured("ROLE_USER")
	@CrossOrigin
	@PostMapping("/add")
	@ApiOperation(value = "${swagger.add}", notes = "${swagger.add.desc}")
	public ResponseEntity<HttpStatus> add(@Valid @RequestBody Computer computer) {
		System.err.println(computer);
		System.err.println(computer);
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

	@Secured("ROLE_USER")
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

	private PageDto<Computer> getPage(Navigation navigation) {
		PageDto.Builder<Computer> builder = new PageDto.Builder<>();
		return builder.setNumber(navigation.getNumber())
			.setSize(navigation.getSize())
			.build();
	}
} 
