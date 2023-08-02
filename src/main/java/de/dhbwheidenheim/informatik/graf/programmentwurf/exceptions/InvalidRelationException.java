package de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions;

@SuppressWarnings("serial")
public class InvalidRelationException extends RedirectException {
	public InvalidRelationException(Long redirectId, String message) {
        super(redirectId, message);
    }
}
