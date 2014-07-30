package haypi.comm;

public class ListMapRequest extends BaseRequest {

	public static final String COMMAND = "201";

	protected int x;
	protected int y;

	public ListMapRequest(int x, int y) {
		super(COMMAND);
		this.x = x;
		this.y = y;
	}

	@Override
	public Object[] getDecoded() {
		return new Object[] {x, y};
	}

}
