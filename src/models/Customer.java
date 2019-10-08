package models;

import java.util.ArrayList;
import java.util.List;

public class Customer {

	private List<Computer> computers;
	
	public Customer() {
		super();
		this.computers = new ArrayList<>();
	}
	
	public Customer(List<Computer> computers) {
		super();
		this.computers = computers;
	}

	public List<Computer> getComputers() {
		return computers;
	}

	public void addComputer(Computer computer) {
		computers.add(computer);
	}

	@Override
	public String toString() {
		return "Customer [computers=" + computers + "]";
	}	
}
