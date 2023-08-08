package de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions;

@SuppressWarnings("serial")
public class IdNotFoundException extends RedirectException {
	public IdNotFoundException(String redirectPath, String message) {
        super(redirectPath, message);
    }
}

