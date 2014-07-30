package haypi.comm;

public class RankRequest extends BaseRequest {

	public static final String COMMAND = "962";

	protected int page;
	protected int rankType;

	public RankRequest(int page, RankType rankType) {
		super(COMMAND);
		this.page = page;
		this.rankType = rankType.ordinal();
	}

	@Override
	public Object[] getDecoded() {
		return new Object[] {page, rankType};
	}
	
	public RankType getRankType() { 
		return RankType.getByIndex(rankType);
	}

}
