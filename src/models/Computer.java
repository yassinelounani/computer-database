package models;

import java.util.Date;

public class Computer {
	private String name;
	private Date dateIntroduce;
	private Date dateDisconected;
	private String Manifacturer;
	
	public Computer(String name) {
		super();
		this.name = name;
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

	public Date getDateDisconected() {
		return dateDisconected;
	}

	public void setDateDisconected(Date dateDisconected) {
		if(dateDisconected.after(this.dateIntroduce)) {
			this.dateDisconected = dateDisconected;
		}
	}

	public String getManifacturer() {
		return Manifacturer;
	}

	public void setManifacturer(String manifacturer) {
		Manifacturer = manifacturer;
	}

	@Override
	public String toString() {
		return "Computer [name=" + name + ", dateIntroduce=" + dateIntroduce + ", dateDisconected=" + dateDisconected
				+ ", Manifacturer=" + Manifacturer + "]";
	}
}
