package fr.excilys.cdb.api.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Computer.Builder;

public class ComputerValidationTest {

	private static final int ID_ZERO = 0;
	private static final String EMPTY = "";
	private static final String DATE_2018_04_12 = "2018-04-12";
	private static final String DATE_2017_04_12 = "2017-04-12";
	private static final String TEST = "Test";
	private static final int ID_1 = 1;
	private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
	public void test_testMinIdAnnotation_expect_success() {
		//prepare
    	Computer computer = Builder.newInstance()
							        .setId(ID_1)
							        .setName(TEST)
							        .setIntroduced(DATE_2017_04_12)
							        .setDicontinued(DATE_2018_04_12)
							        .build();
		//execute
        Set<ConstraintViolation<Computer>> violations = validator.validate(computer);
		//verify
        assertThat(violations.isEmpty()).isTrue();
	}

    @Test
	public void test_minIdAnnotation_expect_echou() {
		//prepare
    	Computer computer = Builder.newInstance()
							        .setId(ID_ZERO)
							        .setName(TEST)
							        .setIntroduced(DATE_2017_04_12)
							        .setDicontinued(DATE_2018_04_12)
							        .build();
		//execute
        Set<ConstraintViolation<Computer>> violations = validator.validate(computer);
		//verify
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.iterator().next().getMessage()).contains("doit être au minimum égal à 1");
	}

    @Test
	public void test_nameComputer_with_NotBlanck_expect_success() {
		//prepare
    	Computer computer = Builder.newInstance()
							        .setId(ID_1)
							        .setName(TEST)
							        .setIntroduced(DATE_2017_04_12)
							        .setDicontinued(DATE_2018_04_12)
							        .build();
		//execute
        Set<ConstraintViolation<Computer>> violations = validator.validate(computer);
		//verify
        assertThat(violations.isEmpty()).isTrue();
	}

    @Test
	public void test_nameComputer_with_NotBlanck_expect_echou() {
		//prepare
    	Computer computer = Builder.newInstance()
							        .setId(ID_1)
							        .setName(EMPTY)
							        .setIntroduced(DATE_2017_04_12)
							        .setDicontinued(DATE_2018_04_12)
							        .build();
		//execute
        Set<ConstraintViolation<Computer>> violations = validator.validate(computer);
		//verify
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.iterator().next().getMessage()).contains("ne peut pas être vide");
	}

    @Test
	public void test_introducedBeforeDiscontinued_expect_success() {
		//prepare
    	Computer computer = Builder.newInstance()
							        .setId(ID_1)
							        .setName(TEST)
							        .setIntroduced(DATE_2017_04_12)
							        .setDicontinued(DATE_2018_04_12)
							        .build();
		//execute
        Set<ConstraintViolation<Computer>> violations = validator.validate(computer);
		//verify
        assertThat(violations.isEmpty()).isTrue();
	}

    @Test
	public void test_introducedBeforeDiscontinued_expect_echou() {
		//prepare
    	Computer computer = Builder.newInstance()
							        .setId(ID_1)
							        .setName(TEST)
							        .setIntroduced(DATE_2018_04_12)
							        .setDicontinued(DATE_2017_04_12)
							        .build();
		//execute
        Set<ConstraintViolation<Computer>> violations = validator.validate(computer);
		//verify
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.iterator().next().getMessage()).contains("introduced date shold be before discontinued date");
	}

    @Test
	public void test_introducedDateFormat_expect_success() {
		//prepare
    	Computer computer = Builder.newInstance()
							        .setId(ID_1)
							        .setName(TEST)
							        .setIntroduced(DATE_2017_04_12)
							        .setDicontinued(DATE_2018_04_12)
							        .build();
		//execute
        Set<ConstraintViolation<Computer>> violations = validator.validate(computer);
		//verify
        assertThat(violations.isEmpty()).isTrue();
	}

    @Test
	public void test_introducedDateFormat_expect_echou() {
		//prepare
    	Computer computer = Builder.newInstance()
							        .setId(ID_1)
							        .setName(TEST)
							        .setIntroduced("340-56")
							        .setDicontinued(DATE_2018_04_12)
							        .build();
		//execute
        Set<ConstraintViolation<Computer>> violations = validator.validate(computer);
		//verify
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.iterator().next().getMessage()).contains("format date is not valid");
	}

}