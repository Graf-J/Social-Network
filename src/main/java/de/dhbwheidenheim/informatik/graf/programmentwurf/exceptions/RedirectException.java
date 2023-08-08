package de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions;

@SuppressWarnings("serial")
public class RedirectException extends RuntimeException {
	private final String redirectPath;
	
	public RedirectException(String redirectPath, String message) {
        super(message);
        this.redirectPath = redirectPath;
    }
	
	public String getRedirectPath() {
		return this.redirectPath;
	}
}
