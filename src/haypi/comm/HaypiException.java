package haypi.comm;

@SuppressWarnings("serial")
public class HaypiException extends Exception {
	private int status;

	public HaypiException(int status) {
		super("STATUS=" + status);
		this.status = status;
	}

	public HaypiException(String message) {
		super(message);
	}
	
	public int getStatus() {
		return status;
	}
	
}
