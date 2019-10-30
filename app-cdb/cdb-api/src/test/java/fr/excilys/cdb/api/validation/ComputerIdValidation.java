package fr.excilys.cdb.api.validation;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.excilys.cdb.api.dto.Identifier;

public class ComputerIdValidation {
	private static final int ID_ZERO = 0;
	private static final int ID_1 = 12;

	private Validator validator;

	@BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
	public void test_minComputerIdAnnotation_expect_success() {
		//prepare
    	Identifier computerId = new Identifier(ID_1);
    	//execute
        Set<ConstraintViolation<Identifier>> violations = validator.validate(computerId);
		//verify
        assertThat(violations.isEmpty()).isTrue();
	}

    @Test
	public void test_minComputerIdAnnotation_expect_echou() {
		//prepare
    	Identifier computerId = new Identifier(ID_ZERO);
    	//execute
        Set<ConstraintViolation<Identifier>> violations = validator.validate(computerId);
		//verify
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.iterator().next().getMessage()).contains("doit être au minimum égal à 1");
	}
}
