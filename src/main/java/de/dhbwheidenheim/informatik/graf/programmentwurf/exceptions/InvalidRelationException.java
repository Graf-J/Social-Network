package de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions;

/**
 * Custom exception class to indicate invalid relation, extending the RedirectException class.
 * This exception is used to handle cases where a relation between entities is considered invalid.
 */
@SuppressWarnings("serial")
public class InvalidRelationException extends RedirectException {
	/**
     * Constructs a new InvalidRelationException with the specified redirect path and error message.
     *
     * @param redirectPath The path to redirect to.
     * @param message The error message associated with the exception.
     */
	public InvalidRelationException(String redirectPath, String message) {
        super(redirectPath, message);
    }
}
