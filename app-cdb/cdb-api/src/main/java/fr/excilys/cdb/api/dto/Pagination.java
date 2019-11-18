package fr.excilys.cdb.api.dto;

import java.util.List;

public class Pagination {

	private int numbersOfPages;
	private int startPage;
	private int endPage;
	private int currentPage;
	private int size;
	private List<Computer> computers;

	public Pagination(Builder builder) {
		this.numbersOfPages = builder.numbersOfPages;
		this.startPage = builder.startPage;
		this.endPage = builder.endPage;
		this.currentPage = builder.currentPage;
		this.size = builder.size;
		this.computers = builder.computers;
	}

	public int getNumbersOfPages() {
		return numbersOfPages;
	}
	public int getStartPage() {
		return startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public int getSize() {
		return size;
	}

	public List<Computer> getComputers() {
		return computers;
	}
	
	public static class Builder {
		private int numbersOfPages;
		private int startPage;
		private int endPage;
		private int currentPage;
		private int size;
		private List<Computer> computers;

		public static Builder newInstance() {
			return new Builder();
		}

		public Builder setNumbersOfPages(int numbersOfPages) {
			this.numbersOfPages = numbersOfPages;
			return this;
		}

		public Builder setStartPage(int startPage) {
			this.startPage = startPage;
			return this;
		}

		public Builder setEndPage(int endPage) {
			this.endPage = endPage;
			return this;
		}

		public Builder setCurrentPage(int currentPage) {
			this.currentPage = currentPage;
			return this;
		}

		public Builder setSize(int size) {
			this.size = size;
			return this;
		}

		public Builder setComputers(List<Computer> computers) {
			this.computers = computers;
			return this;
		}

		public Pagination build() {
			return new Pagination(this);
		}
		
	}

	@Override
	public String toString() {
		return "Pagination [numbersOfPages=" + numbersOfPages + ", startPage=" + startPage + ", endPage=" + endPage
				+ ", currentPage=" + currentPage + ", size=" + size + ", computers=" + computers + "]";
	}
}
