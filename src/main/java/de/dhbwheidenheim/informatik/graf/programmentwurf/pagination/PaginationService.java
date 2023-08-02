package de.dhbwheidenheim.informatik.graf.programmentwurf.pagination;

import org.springframework.stereotype.Service;

@Service
public class PaginationService {
	public Pagination getPagination(
			String page, 
			String pageSize, 
			Integer defaultPageSize,
			Long entityCount
		) {
		// Parse Parameters to Integer or receive default value
		Integer pageParam = parseInt(page, 0);
		Integer pageSizeParam = parseInt(pageSize, defaultPageSize);
		
		// Calculate amount of pages
		Integer numPages = (int)Math.ceil((double)entityCount / (double)pageSizeParam);
		
		return new Pagination(pageParam, pageSizeParam, numPages);
	}
	
	private Integer parseInt(String stringValue, Integer fallbackValue) {
		try {
			return Integer.parseInt(stringValue);
		} catch(NumberFormatException ex) {
			return fallbackValue;
		}
	}
}