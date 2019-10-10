package api;

import exception.BadNumberPageException;
import exception.BadSizePageException;

public class Page {
	
	private int number;
	private int size;
	
	public Page(int number, int size) throws BadNumberPageException, BadSizePageException {
		super();
		this.setNumber(number);
		this.setSize(size);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) throws BadNumberPageException {
		if(number < 1) {
			throw new BadNumberPageException("Number Page Not be null or negative number first Page start at 1");
		}
		this.number = number;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) throws BadSizePageException {
		if(size <= 0) {
			throw new BadSizePageException("Size of Page Not be null or negative");
		}
		this.size = size;
	}

	@Override
	public String toString() {
		return "Page [number=" + number + ", size=" + size + "]";
	}

}
