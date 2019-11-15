package fr.excilys.cdb.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Computer.Builder;

public class HelperFront {

	public static Computer buildComputer(HttpServletRequest request) {
		String computerName = (String) request.getParameter("name");
		String introduced = (String) request.getParameter("introduced");
		String discontinued = (String) request.getParameter("discontinued");
		String companyId = (String) request.getParameter("companyId");
		Computer computer = Builder.newInstance()
								.setName(computerName)
								.setIntroduced(introduced)
								.setDicontinued(discontinued)
								.setIdCompany(Long.parseLong(companyId))
								.build();
		return computer;
	}

	public static <E> Map<String, String> checkError(E bean) {
		Map<String, String> messages = new HashMap<String, String>();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	    Validator validator = factory.getValidator(); 
		Set<ConstraintViolation<E>> violations = validator.validate(bean);
		if (violations.size() > 0 ) {
		      for (ConstraintViolation<E> contraintes : violations) {
		        messages.put(
			        		contraintes.getPropertyPath().toString(),
			        		contraintes.getPropertyPath().toString() + " " + contraintes.getMessage()
		        		);	 
		      }
		}
		return messages;
	}

	
}
