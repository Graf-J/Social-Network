package de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * GlobalExceptionHandler handles exceptions that occur throughout the application.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	/**
     * Handle exceptions of type Exception.
     * Log the exception and return a custom error response.
     *
     * @param ex      The exception that occurred.
     * @param request The current web request.
     * @return A ResponseEntity containing an error message and HTTP status.
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleConflict(Exception ex, WebRequest request) {
        // Log the exception
        logger.error("An error occurred:", ex);

        // Return a custom error response
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                	.body("An unexpected error occurred. Please try again later.");
    }
}
