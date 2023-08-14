package de.dhbwheidenheim.informatik.graf.programmentwurf.pagination;

import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling operations related to pagination.
 */
@Service
public class PaginationService {
	/**
	 * Generates a Pagination object based on the provided page, pageSize, and entityCount.
	 *
	 * @param page The current page number as a String.
	 * @param pageSize The number of items per page as a String.
	 * @param defaultPageSize The default number of items per page.
	 * @param entityCount The total count of entities to be paginated.
	 * @return A Pagination object containing pagination information.
	 */
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
	
	/**
	 * Parses a String to an Integer, ensuring a fallback value is returned if parsing fails.
	 * The result is clamped to a minimum value of 0.
	 *
	 * @param stringValue The String value to be parsed.
	 * @param fallbackValue The fallback value to be used if parsing fails.
	 * @return The parsed Integer value or the fallback value if parsing fails.
	 */
	private Integer parseInt(String stringValue, Integer fallbackValue) {
		try {
			Integer result = Integer.parseInt(stringValue);
			
			return result < 0 ? 0 : result;
		} catch(NumberFormatException ex) {
			return fallbackValue;
		}
	}
}