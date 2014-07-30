package haypi.comm;

public class ListLetterRequest extends BaseRequest {
	public static final String COMMAND = "997";
	protected String letterId;

	public ListLetterRequest(String letterId) {
		super(COMMAND);
		this.letterId = letterId;
	}

	@Override
	public Object[] getDecoded() {
		return new Object[] { letterId };
	}

}
