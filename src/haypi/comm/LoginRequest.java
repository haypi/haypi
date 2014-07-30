package haypi.comm;


public class LoginRequest extends BaseRequest {
	public static final String COMMAND = "101";
	
	protected String user;
	protected String pass;
	protected String deviceId;
	protected String pvp = "0";
	protected String gameVersion = "305";
	
	public LoginRequest(String user, String pass, boolean pvp, String deviceId) {
		super(COMMAND);
		this.user = user;
		this.pass = pass;
		this.pvp = pvp?"1":"0";
		this.deviceId = deviceId;
	}

	@Override
	public Object[] getDecoded() {
		return new Object[] {user, pass, null, gameVersion, pvp,  deviceId};
	}

	@Override
	public Class<? extends BaseResponse> getResponseClass() {
		return LoginResponse.class;
	}
}
