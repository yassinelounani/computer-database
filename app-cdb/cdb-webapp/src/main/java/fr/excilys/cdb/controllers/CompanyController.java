package fr.excilys.cdb.controllers;

import static org.springframework.http.ResponseEntity.ok;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fr.excilys.cdb.api.CompanyService;
import fr.excilys.cdb.api.dto.Company;
import fr.excilys.cdb.api.dto.Navigation;
import fr.excilys.cdb.api.dto.PageDto;
import fr.excilys.cdb.api.exception.NotFoundCompanyException;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/companies")
@CrossOrigin(origins = "http://localhost:4200")
public class CompanyController {

	private final CompanyService companyService;

	@Autowired
	public CompanyController(CompanyService companyService) {
		super();
		this.companyService = companyService;
	}
	
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping
	@ApiOperation(value = "${swagger.companies}", notes = "${swagger.companies.desc}")
	public ResponseEntity<List<Company>> getAll() {
		List<Company> companies = companyService.getCompanies();
		return ok().body(companies); 
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/page")
	@ApiOperation(value = "${swagger.comp.page}", notes = "${swagger.comp.page.desc}")
	public ResponseEntity<PageDto<Company>> getAllWithPage(Navigation navigation) {
		PageDto<Company> page = getRequestPage(navigation);
		PageDto<Company> pageDto = companyService.getCompaniesWithPage(page);
		return ok().body(pageDto);
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@CrossOrigin
	@GetMapping("/find")
	@ApiOperation(value = "${swagger.comp.find}", notes = "${swagger.comp.find.desc}")
	public ResponseEntity<PageDto<Company>> find(@Valid Navigation navigation) {
		PageDto<Company> page = getRequestPage(navigation);
		PageDto<Company> pageDto = companyService.getSerchCompaniesWithPage(page, navigation.getValue());
		return ok().body(pageDto);
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping(value = "/sort")
	@ApiOperation(value = "${swagger.comp.sort}", notes = "${swagger.comp.sort.desc}")
	public ResponseEntity<PageDto<Company>> sort(@Valid Navigation navigation) {
		PageDto<Company> page = getRequestPage(navigation);
		PageDto<Company> pageDto = companyService.getCompaniesWithPageAndSort(page, navigation);
		return ok().body(pageDto);
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PostMapping("/add")
	@ApiOperation(value = "${swagger.comp.add}", notes = "${swagger.comp.add.desc}")
	public ResponseEntity<HttpStatus> add(@RequestBody @Valid Company Company) {
			int addCompany = companyService.addCompany(Company);
			if (addCompany == 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			} else {
				return ResponseEntity.status(HttpStatus.CREATED).build();
			}
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PutMapping("/update")
	@ApiOperation(value = "${swagger.comp.update}", notes = "${swagger.comp.update.desc}")
	public ResponseEntity<HttpStatus> update(@RequestBody @Valid Company Company) {
		try {
			int updateValue = companyService.updateCompany(Company);
			if ( updateValue == 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			} else {
				return ResponseEntity.status(HttpStatus.OK).build();
			}
		} catch (NotFoundCompanyException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	private PageDto<Company> getRequestPage(Navigation navigation) {
		PageDto.Builder<Company> builder = new PageDto.Builder<>();
		return builder.setNumber(navigation.getNumber())
			.setSize(navigation.getSize())
			.build();
	}
}
