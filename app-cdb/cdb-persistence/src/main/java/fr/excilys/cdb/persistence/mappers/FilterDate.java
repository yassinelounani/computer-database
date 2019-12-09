package fr.excilys.cdb.persistence.mappers;

import java.time.LocalDate;

public class FilterDate {
	private LocalDate begin;
	private LocalDate end;
	
	public FilterDate(Builder builder) {
		super();
		this.begin = builder.begin;
		this.end = builder.end;
	}

	public LocalDate getBegin() {
		return begin;
	}

	public LocalDate getEnd() {
		return end;
	}
	
	@Override
	public String toString() {
		return "FilterDate [begin=" + begin + ", end=" + end + "]";
	}

	public static class Builder {
		private LocalDate begin;
		private LocalDate end;

		private Builder() {
			super();
		}
		public static Builder newInstance() {
			return new Builder();
		}

		public Builder setBegin(LocalDate begin) {
			this.begin = begin;
			return this;
		}

		public Builder setEnd(LocalDate end) {
			this.end = end;
			return this;
		}
		
		public FilterDate build() {
			return new FilterDate(this);
		}
		
	}
}
