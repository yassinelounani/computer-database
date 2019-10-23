package fr.excilys.cdb.api.validation;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import fr.excilys.cdb.api.dto.Computer;

public class IntroducedBeforeDiscontinuedValidator implements ConstraintValidator<IntroducedBeforeDiscontinued, Computer> {

	@Override
    public void initialize(IntroducedBeforeDiscontinued constraintAnnotation) {
	}

    @Override
    public boolean isValid(Computer request, ConstraintValidatorContext constraintValidatorContext) {
    	boolean isValid = false;  
    	if (request.getIntroduced().trim().isEmpty() || request.getDiscontinued().trim().isEmpty()) {
            return true;
        }
    	if ((request.getIntroduced().matches("^\\d{4}-\\d{2}-\\d{2}$")
    			|| request.getIntroduced().matches("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$"))
    			&& (request.getDiscontinued().matches("^\\d{4}-\\d{2}-\\d{2}$")
    			|| request.getDiscontinued().matches("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$"))) {
    		
    		LocalDate introducedDate = LocalDate.parse(request.getIntroduced());
            LocalDate discontinuedDate = LocalDate.parse(request.getDiscontinued());
            isValid = introducedDate.isBefore(discontinuedDate);
            if ( !isValid ) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate( "date shold be before discontinued date" )
                        .addPropertyNode( "introduced" ).addConstraintViolation();
            }
    	}
    	return isValid;        
    }
}