package fr.excilys.cdb.business;

import static fr.excilys.cdb.business.Helper.mapToUser;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.excilys.cdb.api.dto.Token;
import fr.excilys.cdb.api.dto.UserDto;
import fr.excilys.cdb.persistence.models.UserEntity;
import fr.excilys.cdb.persistence.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
    UserRepository userRepository;
	
	@Autowired
	PasswordEncoder bCrypt;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("UserName " + username + " not found");
        } else {
        	String[] roles = getRoles(user);
             return User.withUsername(user.getUsername()).password(user.getPassword()).roles(roles).build();
        }
    }

    public UserDto findByUsernameAndPassword(UserDto userDto) throws UsernameNotFoundException{
    	System.err.println(userDto);
    	UserEntity user = userRepository.findByUsername(userDto.getUsername());
    	if (user == null) {
            throw new UsernameNotFoundException("Username " + userDto.getUsername() + " not found");
        } else {
        	 if(bCrypt.matches(userDto.getPassword(), user.getPassword())) {
        		 return mapToUser(user);
        	 } else {
        		 throw new UsernameNotFoundException("password " + userDto.getUsername() + " is incorect");
        	 }
        }
    }

	private String[] getRoles(UserEntity user) {
		return 	user.getRoles().stream()
					.map(mapper -> mapper.getRole())
					.collect(Collectors.toList())
					.toArray(new String[0]);
	}

	
	  public Token login(UserDto requestUser) {
		  String token = null;
		  	UsernamePasswordAuthenticationToken authenticationTokenRequest = new
	                UsernamePasswordAuthenticationToken(requestUser.getUsername(), requestUser.getPassword());
	        System.err.println(authenticationTokenRequest);
		  	try {
	            Authentication authentication = this.authenticationManager.authenticate(authenticationTokenRequest);
	            SecurityContext securityContext = SecurityContextHolder.getContext();
	            securityContext.setAuthentication(authentication);
	            System.err.println(authentication);
	            final UserDetails userDetails = loadUserByUsername(requestUser.getUsername());
	            System.err.println(userDetails);
	            token = jwtTokenUtil.generateToken(userDetails);
	        } catch (BadCredentialsException ex) { 
	        	System.out.println(ex.getMessage());
	        }
	        return new Token(token);
	    }
}	
