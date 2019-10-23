package fr.excilys.cdb.api.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IntroducedBeforeDiscontinuedValidator.class})
@Documented
public @interface IntroducedBeforeDiscontinued {

	String message() default "format date is not valid";

    Class[] groups() default {};
 
    Class[] payload() default {};

}
       