package fr.excilys.cdb.business;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

	private String[] getRoles(UserEntity user) {
		return 	user.getRoles().stream()
					.map(mapper -> mapper.getRole())
					.collect(Collectors.toList())
					.toArray(new String[0]);
	}

	
	public UserDto login(UserDto requestUser) {
		UserDto sendUser = null;
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
	        Collection<SimpleGrantedAuthority> roles = (Collection<SimpleGrantedAuthority>) securityContext.getAuthentication().getAuthorities();
	        sendUser = UserDto.Builder.newInstance()
	            	.setUsername(requestUser.getUsername())
	            	.setToken(token)
	            	.setRoles( getRolesFromUserDetails(roles))
	            	.build();
	        } catch (BadCredentialsException ex) { 
	        	System.out.println(ex.getMessage());
	        }
	        return sendUser;
	    }
	  
	private Set<String> getRolesFromUserDetails(Collection<SimpleGrantedAuthority> collection) {
			return 	collection.stream()
						.map(mapper -> mapper.getAuthority())
						.collect(Collectors.toSet());
	}
	  	
}	
