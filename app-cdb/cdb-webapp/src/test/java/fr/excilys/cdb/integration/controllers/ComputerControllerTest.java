package fr.excilys.cdb.integration.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.configuration.SpringConfig;
import fr.excilys.cdb.configuration.WebSecurityConfig;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringConfig.class, WebSecurityConfig.class})
public class ComputerControllerTest {
	
	private final static Computer COMPUTER = Computer.Builder.newInstance()
			.setId(55)
			.setName("MAC_EXILYS")
			.setIntroduced("2019-01-10")
			.setDicontinued("2019-10-10")
			.setIdCompany(5l)
			.build();
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setup(){
		mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext)
	                         	.apply(springSecurity()).build();   
	}

	@Test
	@WithMockUser(username = "yassine", password = "123456")
	public void test_getComputer_with_id_expect_Ok() throws Exception {
		mockMvc
		.perform(get("/computers/6"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value("6"))
        .andExpect(jsonPath("$.name").value("MacBook Pro"))
        .andDo(print());
	}

	@Test
	@WithMockUser(username = "yassine", password = "123456")
	public void test_getComputer_with_id_expect_ko() throws Exception {
		mockMvc
		.perform(get("/computers/5"))
		.andExpect(status().isNotFound())
        .andDo(print());
	}

	@Test
	@WithMockUser(username = "yassine", password = "123456")
	public void test_getComputers_with_page_expect_pageofComputer() throws Exception {
		mockMvc
		.perform(get("/computers/page")
		.param("number", "1")
		.param("size", "5"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.content").value(hasSize(5)));
	}

	@Test
	@WithMockUser(username = "yassine", password = "123456")
	public void test_getcomuters_with_find_expect_pageofComputer() throws Exception {
		mockMvc
		.perform(get("/computers/find/Amiga")
		.param("number", "1")
		.param("size", "5"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.content").value(hasSize(5)))
		.andExpect(jsonPath("$.content[*].name")
				.value(everyItem(containsString("Amiga"))));
	}

	@Test
	@WithMockUser(username = "yassine", password = "123456")
	public void test_geComputer_with_sort_expect_pageOfComputers() throws Exception {
		mockMvc
		.perform(get("/computers/sort")
		.param("number", "1")
		.param("size", "5")
		.param("property", "name")
		.param("order", "DESC"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.content").value(hasSize(5)));
	}

	@Test
	@WithMockUser(username = "yassine", password = "123456", roles = {"ADMIN"})
	public void test_addComputer_expect_Ok() throws Exception {
		mockMvc
		.perform(post("/computers/add")
		.content(asJsonString(COMPUTER))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "yassine", password = "123456", roles = {"ADMIN"})
	public void test_updateComputer_expect_Ok() throws Exception {
		mockMvc
		.perform(put("/computers/update")
		.content(asJsonString(COMPUTER))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "yassine", password = "123456", roles = {"ADMIN"})
	public void test_deleteComputer_expect_Ok() throws Exception {
		mockMvc
		.perform(delete("/computers/delete/100"))
		.andExpect(status().isOk());
	}

	public static String asJsonString(final Object obj) {
	    try {
	         String json = new ObjectMapper().writeValueAsString(obj);
	         System.err.println(json);
	        return json;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
}
