package haypi.comm;

public class DeleteLetterRequest extends BaseRequest {
	public static final String COMMAND = "984";
	protected String letterId;

	public DeleteLetterRequest(String letterId) {
		super(COMMAND);
		this.letterId = letterId;
	}

	@Override
	public Object[] getDecoded() {
		return new Object[] { letterId };
	}

}
