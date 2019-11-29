package fr.excilys.cdb.business;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.excilys.cdb.persistence.models.UserEntity;
import fr.excilys.cdb.persistence.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
    UserRepository userRepository;

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
}
