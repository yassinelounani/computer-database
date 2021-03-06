package fr.excilys.cdb.persistence.dao;

public class Pageable {

	private int number;
	private int size;

	public Pageable(int number, int size) {
		super();
		this.number = number;
		this.size = size;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number){
		this.number = number;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "Page [number=" + number + ", size=" + size + "]";
	}
}
