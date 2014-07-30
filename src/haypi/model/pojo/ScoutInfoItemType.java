package haypi.model.pojo;

public enum ScoutInfoItemType {
	PRESTIGE, TITLE, CHEST, HIDDEN_ATTACK, HEAL, CRANNY, CONVINCE,
	SPEED, ATTACK, DEFENCE, FORTUNE,
	INFANTRY, CAVALRY, ARCHERS, CATAPULTS,
	WOOD, STONE, IRON, CROP,
	UNKNOWN,
	X, Y, PLAYER,
	ARMOR, SWORD, HORSE, MANUAL, RING, 
	;
	
	public static ScoutInfoItemType getByIndex(int index) {
		return ScoutInfoItemType.values()[index];
	}

	public static ScoutInfoItemType getByIndex(String index) {
		return ScoutInfoItemType.values()[Integer.parseInt(index)];
	}

	public int getOrdinal() {
		return ordinal();
	}
};