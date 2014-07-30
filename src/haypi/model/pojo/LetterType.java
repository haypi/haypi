package haypi.model.pojo;

public enum LetterType {
	PLAYER, _ONE, WAR, SCOUT, HUNT, SYSTEM, _SIX;
	public static LetterType getByIndex(int index) {
		return LetterType.values()[index];
	}

	public static LetterType getByIndex(String index) {
		return LetterType.values()[Integer.parseInt(index)];
	}

	public int getOrdinal() {
		return ordinal();
	}
};