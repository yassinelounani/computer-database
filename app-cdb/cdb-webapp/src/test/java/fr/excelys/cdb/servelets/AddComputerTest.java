package fr.excelys.cdb.servelets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import static org.junit.jupiter.api.Assertions.fail;

public class AddComputerTest {
	  private static final String DISCONTINUED_MUST_BE_BEFORE_THE_INTRODUCED = "The discontinued date must be before the introduced date";
	  private static final String DATE_2019_10_02 = "2019-10-02";
	  private static final String TEST_NAME = "test";
	  private static final String NAME_NOT_NULL = "name ne peut pas être vide";
	  private static final String COMPUTER_ADDED_WITH_SUCCESS = "Success! Computer added with success";
	  private static final String AMIGA_CORPORATION = "14 -- Amiga Corporation";
	  private static final String DATE_2019_10_20 = "2019-10-20";
	  private static final String DATE_2019_10_06 = "2019-10-06";
	  private static final String TOCHIBA_NEW = "TOCHIBA NEW";

	  private WebDriver driver;
	  private String baseUrl;
	  private StringBuffer verificationErrors = new StringBuffer();

	  @BeforeEach
	  public void setUp() throws Exception {
	    driver = new FirefoxDriver();
	    baseUrl = "http://localhost:8080/cdb-webapp/addComputer";
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	  }

	  @Test
	  public void test_addComputer_expect_success() throws Exception {
	    driver.get(baseUrl);
	    driver.findElement(By.id("computerName")).sendKeys(TOCHIBA_NEW);
	    driver.findElement(By.id("introduced")).sendKeys(DATE_2019_10_06);
	    driver.findElement(By.id("discontinued")).sendKeys(DATE_2019_10_20);
	    new Select(driver.findElement(By.id("companyId"))).selectByVisibleText(AMIGA_CORPORATION);
	    driver.findElement(By.id("submit")).click();
	    try {
            assertEquals(driver.findElement(By.id("success")).getText(), COMPUTER_ADDED_WITH_SUCCESS);
        } catch (Error e) {
        	verificationErrors.append(e.toString());
        }

	  }

	  @Test
	  public void test_addComputer_with_empty_name_expect_fail() throws Exception {
	    driver.get(baseUrl);
	    driver.findElement(By.id("computerName")).sendKeys("");
	    driver.findElement(By.id("introduced")).sendKeys(DATE_2019_10_06);
	    driver.findElement(By.id("discontinued")).sendKeys(DATE_2019_10_20);
	    new Select(driver.findElement(By.id("companyId"))).selectByVisibleText(AMIGA_CORPORATION);
	    driver.findElement(By.id("submit")).click();
	    try {
            assertEquals(driver.findElement(By.id("errorName")).getText(), NAME_NOT_NULL);
        } catch (Error e) {
        	verificationErrors.append(e.toString());
        }

	  }

	  @Test
	  public void test_addComputer_with_introduced_is_after_discontinued_expect_fail() throws Exception {
	    driver.get(baseUrl);
	    driver.findElement(By.id("computerName")).sendKeys(TEST_NAME);
	    driver.findElement(By.id("introduced")).sendKeys(DATE_2019_10_06);
	    driver.findElement(By.id("discontinued")).sendKeys(DATE_2019_10_02);
	    new Select(driver.findElement(By.id("companyId"))).selectByVisibleText(AMIGA_CORPORATION);
	    try {
            assertEquals(driver.findElement(By.id("spanIntro")).getText(), DISCONTINUED_MUST_BE_BEFORE_THE_INTRODUCED);
        } catch (Error e) {
        	verificationErrors.append(e.toString());
        }

	  }

	  @AfterEach
	  public void tearDown() throws Exception {
	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	    	fail(verificationErrorString);
	    }
	  }
}
