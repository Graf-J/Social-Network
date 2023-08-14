package de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions;

/**
 * Custom exception class used to redirect the user to a specific path while displaying an error message.
 */
@SuppressWarnings("serial")
public class RedirectException extends RuntimeException {
	private final String redirectPath;
	
	/**
     * Constructs a new RedirectException with the specified redirect path and error message.
     *
     * @param redirectPath The path to redirect to.
     * @param message The error message associated with the exception.
     */
	public RedirectException(String redirectPath, String message) {
        super(message);
        this.redirectPath = redirectPath;
    }
	
	/**
     * Gets the path to which the user should be redirected.
     *
     * @return The redirect path.
     */
	public String getRedirectPath() {
		return this.redirectPath;
	}
}
