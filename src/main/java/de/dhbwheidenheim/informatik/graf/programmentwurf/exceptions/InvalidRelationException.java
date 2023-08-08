package de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions;

@SuppressWarnings("serial")
public class InvalidRelationException extends RedirectException {
	public InvalidRelationException(String redirectPath, String message) {
        super(redirectPath, message);
    }
}
