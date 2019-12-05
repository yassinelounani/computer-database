package fr.excelys.cdb.servelets;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import fr.excilys.cdb.api.ComputerService;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.controllers.ComputerController;

public class ComputerControllerTest {
	
	private Computer MAC = Computer.Builder.newInstance()
			.setId(55)
			.setName("MAC_EX")
			.setIntroduced("2019-01-10")
			.setDicontinued("2019-10-10")
			.setIdCompany(5l)
			.build();
	private Computer TOSH = Computer.Builder.newInstance()
			.setId(55)
			.setName("TOSH_EX")
			.setIntroduced("2019-01-10")
			.setDicontinued("2019-10-10")
			.setIdCompany(5l)
			.build();
	
	
	@Mock
	private ComputerService mockComputerService;
	
	@InjectMocks
	private ComputerController computerController;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup(){
		MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(computerController)
                .build();
	}
	
	@Test
	public void test_getComputers_expect_ok() throws Exception {
	    List<Computer> Computers = Arrays.asList(MAC, TOSH);
	    doReturn(Computers).when(mockComputerService).getComputers();
	    mockMvc.perform(get("/computers"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$", hasSize(2)));
	    verify(mockComputerService, times(1)).getComputers();
	    verifyNoMoreInteractions(mockComputerService);
	}
	
}
