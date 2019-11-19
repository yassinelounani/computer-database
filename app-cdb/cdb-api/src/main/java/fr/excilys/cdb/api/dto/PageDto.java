package fr.excilys.cdb.api.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;

public class PageDto<T> {
	@Min(0)
	private int number;
	@Min(1)
	private int size;
	private long totalElement;
	private List<T> content;

	public PageDto(Builder<T> builder) {
		this.number = builder.number;
		this.size = builder.size;
		this.totalElement = builder.totalElement;
		this.content = builder.content;
	}
	
	public int getNumber() {
		return number;
	}

	public int getSize() {
		return size;
	}

	public long getTotalElement() {
		return totalElement;
	}

	public List<T> getContent() {
		return content;
	}

	public static class Builder<T> {
		private int number;
		private int size;
		private long totalElement;
		private List<T> content;

		public Builder<T> setNumber(int number) {
			this.number = number;
			return this;
		}
		
		public Builder() {
			super();
			this.content = new ArrayList<>();
		}

		public Builder<T> setSize(int size) {
			this.size = size;
			return this;
		}

		public Builder<T> setTotalElement(long totalElement) {
			this.totalElement = totalElement;
			return this;
		}

		public Builder<T> setContent(List<T> content) {
			this.content = content;
			return this;
		}

		public PageDto<T> build() {
			return new PageDto<>(this);
		}
	}

	@Override
	public String toString() {
		return "PageDto [number=" + number + ", size=" + size + ", totalElement=" + totalElement + ", content="
				+ content + "]";
	}
}
