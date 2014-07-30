package haypi.comm;

public class ScoutRequest extends BaseRequest {

	public static final String COMMAND = "240";

	protected int x;
	protected int y;

	public ScoutRequest(int x, int y) {
		super(COMMAND);
		this.x = x;
		this.y = y;
	}

	@Override
	public Object[] getDecoded() {
		return new Object[] {x, y};
	}

}
