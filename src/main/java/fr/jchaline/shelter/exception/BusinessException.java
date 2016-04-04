package fr.jchaline.shelter.exception;

/**
 * Specific exception for business logic
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * This exception must not be checked, only sent by server side and managed by client
	 * @param string the message managed by the client
	 */
	public BusinessException(String string) {
		super(string);
	}
}
