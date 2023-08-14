package de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions;

/**
 * Custom exception class to indicate that an email address was not found, extending the RedirectException class.
 * This exception is used to handle cases where an email-related operation fails due to the email address not being found.
 */
@SuppressWarnings("serial")
public class EmailNotFoundException extends RedirectException {
	/**
     * Constructs a new EmailNotFoundException with the specified redirect path and error message.
     *
     * @param redirectPath The path to redirect to.
     * @param message The error message associated with the exception.
     */
	public EmailNotFoundException(String redirectPath, String message) {
        super(redirectPath, message);
    }
}
