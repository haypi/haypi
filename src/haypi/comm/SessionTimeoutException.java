package haypi.comm;

@SuppressWarnings("serial")
public class SessionTimeoutException extends HaypiException {

	public SessionTimeoutException() {
		super("Session expired, please login again");
	}
}
