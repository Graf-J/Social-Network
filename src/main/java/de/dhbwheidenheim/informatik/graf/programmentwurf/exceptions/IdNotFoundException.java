package de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions;

/**
 * Custom exception class to indicate that an entity with a specific ID was not found, extending the RedirectException class.
 * This exception is used to handle cases where an operation involving an entity's ID fails due to the ID not being found.
 */
@SuppressWarnings("serial")
public class IdNotFoundException extends RedirectException {
	/**
     * Constructs a new IdNotFoundException with the specified redirect path and error message.
     *
     * @param redirectPath The path to redirect to.
     * @param message The error message associated with the exception.
     */
	public IdNotFoundException(String redirectPath, String message) {
        super(redirectPath, message);
    }
}

