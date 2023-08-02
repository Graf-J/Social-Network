package de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions;

@SuppressWarnings("serial")
public class EmailNotFoundException extends RedirectException {
	public EmailNotFoundException(Long redirectId, String message) {
        super(redirectId, message);
    }
}
