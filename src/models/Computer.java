package models;

import java.util.Date;

public class Computer {

	private long id;
	private String name;
	private Date dateIntroduce;
	private Date dateDisconnected;
	private Company company;
	
	public Computer(String name) {
		super();
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateIntroduce() {
		return dateIntroduce;
	}

	public void setDateIntroduce(Date dateIntroduce) {
		this.dateIntroduce = dateIntroduce;
	}

	public Date getDateDisconnected() {
		return dateDisconnected;
	}

	public void setDateDisconnected(Date dateDisconnected) {
		if(dateDisconnected.after(this.dateIntroduce)) {
			this.dateDisconnected = dateDisconnected;
		}
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "Computer [name=" + name + ", dateIntroduce=" + dateIntroduce + ", dateDisconnected=" + dateDisconnected
				+ ", Manifacturer=" + company + "]";
	}
}
