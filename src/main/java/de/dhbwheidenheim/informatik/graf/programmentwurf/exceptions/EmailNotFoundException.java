package de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions;

@SuppressWarnings("serial")
public class EmailNotFoundException extends RedirectException {
	public EmailNotFoundException(String redirectPath, String message) {
        super(redirectPath, message);
    }
}
