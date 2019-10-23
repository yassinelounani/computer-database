package fr.excilys.cdb.api.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.constraints.Pattern;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = {ValidDateFormatValidator.class})
public @interface ValidDateFormat {

	String message() default "";

    Class[] groups() default {};
 
    Class[] payload() default {};
 
    String date();
}
