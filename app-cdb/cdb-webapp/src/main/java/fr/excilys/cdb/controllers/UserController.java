    package fr.excilys.cdb.controllers;

import static org.springframework.http.ResponseEntity.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.excilys.cdb.api.dto.Token;
import fr.excilys.cdb.api.dto.UserDto;
import fr.excilys.cdb.business.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

	@Autowired
	private UserService userservice;
	
	
	@Autowired
	  AuthenticationManager authenticationManager;
	
	@PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto) {
		UserDto user = userservice.login(userDto);
        System.err.println(user);
        
        return ok().body(user);
    }
	

}
