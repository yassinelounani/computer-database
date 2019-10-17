package fr.excilys.cdb.api.dto;

public class Page {

	private int number;
	private int size;

	public Page(int number, int size) {
		super();
		this.number = number;
		this.size = size;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
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
