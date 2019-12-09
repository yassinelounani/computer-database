package fr.excilys.cdb.api.dto;

public class FilterByProperty {
	private String filter;
	private String value;
	
	public FilterByProperty() {
		super();
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "FilterByProperty [property=" + filter + ", value=" + value + "]";
	}
}
