package fr.excilys.cdb.api.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public final class PageAndSort {
	@Valid
	private Page page;
	@NotNull
	Sort sort;

	public PageAndSort(@Valid Page page, @NotNull Sort sort) {
		super();
		this.page = page;
		this.sort = sort;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "PageAndSort [page=" + page + ", sort=" + sort + "]";
	}
}
