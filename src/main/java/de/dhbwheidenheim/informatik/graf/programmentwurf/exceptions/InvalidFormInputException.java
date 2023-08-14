package de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions;

/**
 * Custom exception class to indicate invalid form input, extending the RedirectException class.
 * This exception is used to handle cases where the input provided through a form is considered invalid.
 */
@SuppressWarnings("serial")
public class InvalidFormInputException extends RedirectException {
	/**
     * Constructs a new InvalidFormInputException with the specified redirect path and error message.
     *
     * @param redirectPath The path to redirect to.
     * @param message The error message associated with the exception.
     */
	public InvalidFormInputException(String redirectPath, String message) {
        super(redirectPath, message);
    }
}
