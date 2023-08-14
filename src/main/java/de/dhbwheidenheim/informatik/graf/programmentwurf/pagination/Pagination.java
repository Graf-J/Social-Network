package de.dhbwheidenheim.informatik.graf.programmentwurf.pagination;


/**
 * The Pagination class represents a paginated result set, providing information
 * about the current page, the number of items per page, and the total number of pages.
 */
public class Pagination {
	private Integer page;
	private Integer pageSize;
	private Integer numPages;
	
	public Pagination(Integer page, Integer pageSize, Integer numPages) {
		this.page = page;
		this.pageSize = pageSize;
		this.numPages = numPages;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getNumPages() {
		return numPages;
	}

	public void setNumPages(Integer numPages) {
		this.numPages = numPages;
	} 
}
