package de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions;

@SuppressWarnings("serial")
public class InvalidFormInputException extends RedirectException {
	public InvalidFormInputException(String redirectPath, String message) {
        super(redirectPath, message);
    }
}
