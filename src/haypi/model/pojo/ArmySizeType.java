package haypi.model.pojo;

public enum ArmySizeType {
	SEVERAL, ONE_OR_TWO_DOZEN, DOZENS, SCORES, ONE_OR_TWO_HUNDRED, SEVERAL_HUNDRED, HUNDREDS, ONE_OR_TWO_THOUSAND, SEVERAL_THOUSAND, THOUSANDS, OVER_100_HUNDRED;

	public static String ARMY_SIZE_SEVERAL = ": Several of."; // < 9
	public static String ARMY_SIZE_SCORES = ": Scores of."; // 50-99
	public static String ARMY_SIZE_10 = ": Over 100 hundred."; // >= 10000

	
//	"Several" means that enemies number is between 0~9
//	"One or two dozen" means that enemies number is between 10~24
//	"Dozens" means that enemies number is between 25~49
//	"Scores" means that enemies number is between 50~99
//	"One or two hundred" means that enemies number is between 100~249
//	"Several hundred" means that enemies number is between 250~499
//	"Hundred" means that enemies number is between 500~999
//	"One or two thousand" means that enemies number is between 1000~2499
//	"Several thousand" means that enemies number is between 2500~4999
//	"Thousands" means that enemies number is between 5000~9999
//	"Over 100 hundred"  means that enemies number is between >=10000
	
	// public static ArmySizeType getByValue(String value) {
	//
	// }

	public static ArmySizeType getByIndex(int index) {
		return ArmySizeType.values()[index];
	}

	public static ArmySizeType getByIndex(String index) {
		return ArmySizeType.values()[Integer.parseInt(index)];
	}

	public int getOrdinal() {
		return ordinal();
	}
};