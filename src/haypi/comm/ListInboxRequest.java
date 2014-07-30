package haypi.comm;

public class ListInboxRequest extends BaseRequest {

	public static final String COMMAND = "998";

	public ListInboxRequest() {
		super(COMMAND);
	}

	@Override
	public Object[] getDecoded() {
		return new Object[] {};
	}

}
