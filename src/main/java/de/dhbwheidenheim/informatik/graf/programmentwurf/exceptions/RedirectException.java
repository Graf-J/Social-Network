package de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions;

@SuppressWarnings("serial")
public class RedirectException extends RuntimeException {
	private final Long redirectId;
	
	public RedirectException(Long redirectId, String message) {
        super(message);
        this.redirectId = redirectId;
    }
	
	public Long getRedirectId() {
		return this.redirectId;
	}
}
