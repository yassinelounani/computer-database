package fr.excilys.cdb.api.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public final class Identifier {
	
	@NotNull
	@Min(1)
	private long id;

	public Identifier(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ComputerId [id=" + id + "]";
	}

}
