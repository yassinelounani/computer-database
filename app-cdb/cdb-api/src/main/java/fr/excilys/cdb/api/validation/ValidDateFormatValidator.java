package fr.excilys.cdb.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidDateFormatValidator implements ConstraintValidator<ValidDateFormat, String> {
	
	private String date;
	@Override
    public void initialize(ValidDateFormat constraintAnnotation) {
		this.date = constraintAnnotation.date();
	}

    @Override
    public boolean isValid(String request, ConstraintValidatorContext constraintValidatorContext) {
    	if (isBlank(request)) {
            return true;
        }
    	boolean isValid = false;
    	if ((request.matches("^\\d{4}-\\d{2}-\\d{2}$")
    			|| request.matches("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$"))) {
    		isValid = true; 
        } else {
        	constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                        .buildConstraintViolationWithTemplate( "format is invalid " )
                        .addPropertyNode(date).addConstraintViolation();
            isValid = false;
        }
    	return isValid; 
    }
    
    private boolean isBlank(String date) {
    	return date == null || date.trim().isEmpty() ? true : false;
    }
}
