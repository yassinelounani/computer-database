package fr.excilys.cdb.persistence.models;

public class Pageable {
	private int number;
	private int size;
	
	public Pageable() {
		super();
	}
	
	public int getNumber() {
		return number;
	}

	public Pageable setNumber(int number){
		this.number = number;
		return this;
	}

	public int getSize() {
		return size;
	}

	public Pageable setSize(int size) {
		this.size = size;
		return this;
	}

	@Override
	public String toString() {
		return "Page [number=" + number + ", size=" + size + "]";
	}

}
