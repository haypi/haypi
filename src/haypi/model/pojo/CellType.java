package haypi.model.pojo;

public enum CellType {
	EMPTY, MAIN_CITY, BRANCH_CITY, FORTIFIED, WOOD, STONE, IRON, CROP;
	public static CellType getByIndex(int index) {
		return CellType.values()[index];
	}

	public static CellType getByIndex(String index) {
		return CellType.values()[Integer.parseInt(index)];
	}

	public int getOrdinal() {
		return ordinal();
	}
};