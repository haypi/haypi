package haypi.comm;


public enum RankType {
	PRESTIGE, ALLIANCE, LEVEL, SERVER_ACHIEVEMENT;
	public static RankType getByIndex(int index) {
		return RankType.values()[index];
	}

	public static RankType getByIndex(String index) {
		return RankType.values()[Integer.parseInt(index)];
	}
}
