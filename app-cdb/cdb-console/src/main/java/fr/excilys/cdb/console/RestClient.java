package fr.excilys.cdb.console;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import fr.excilys.cdb.api.dto.Company;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Navigation;
import fr.excilys.cdb.api.dto.PageDto;

public class RestClient {

   private static final String REST_URI_COMPUTERS = "http://localhost:8080/cdb-webapp/computers";
   private static final String REST_URI_COMPANIES = "http://localhost:8080/cdb-webapp/companies";

   private HttpAuthenticationFeature feature; 
   private Client client;

   public RestClient() {
	  super();
	  feature = HttpAuthenticationFeature.digest("alex", "user");
	  client = ClientBuilder.newClient().register(feature);
   }

   public Optional<Computer> getComputerById(long id) {
       return client
         .target(REST_URI_COMPUTERS)
         .path(String.valueOf(id))
         .request(MediaType.APPLICATION_JSON)
         .get().readEntity(new GenericType<Optional<Computer>>(){});
   }

   public List<Computer> getAllComputer() {
	   return client
         .target(REST_URI_COMPUTERS)
         .request(MediaType.APPLICATION_JSON)
         .get().readEntity(new GenericType<List<Computer>>(){});
   }

   public List<Company> getAllCompanies() {
	   return client
         .target(REST_URI_COMPANIES)
         .request(MediaType.APPLICATION_JSON)
         .get().readEntity(new GenericType<List<Company>>(){});
   }

   public PageDto<Computer> getComputerWhithPage(Navigation navigation) {
	   return client
         .target(REST_URI_COMPUTERS+"/page")
         .queryParam("number", navigation.getNumber())
         .queryParam("size", navigation.getSize())
         .request(MediaType.APPLICATION_JSON)
         .get()
         .readEntity(new GenericType<PageDto<Computer>>() {});
   }

   public PageDto<Company> getCompaniesWhithPage(Navigation navigation) {
	   return client
         .target(REST_URI_COMPANIES+"/page")
         .queryParam("number", navigation.getNumber())
         .queryParam("size", navigation.getSize())
         .request(MediaType.APPLICATION_JSON)
         .get()
         .readEntity(new GenericType<PageDto<Company>>() {});
   }

   public Response createComputer(Computer computer) {
	    return client
	      .target(REST_URI_COMPUTERS + "/add")
	      .request(MediaType.APPLICATION_JSON)
	      .post(Entity.entity(computer, MediaType.APPLICATION_JSON));
	}

   public Response updateComputer(long id, Computer computer) {
	    return client
	      .target(REST_URI_COMPUTERS + "/update")
	      .path(String.valueOf(id))
	      .request(MediaType.APPLICATION_JSON)
	      .put(Entity.entity(computer, MediaType.APPLICATION_JSON));
	}

   public Response deleteComputer(long id) {
	    return client
	      .target(REST_URI_COMPUTERS + "/delete")
	      .path(String.valueOf(id))
	      .request(MediaType.APPLICATION_JSON)
	      .delete();
	}
}