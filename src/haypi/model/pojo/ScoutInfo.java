package haypi.model.pojo;

import static haypi.model.pojo.ScoutInfoItemType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoutInfo implements Comparable<ScoutInfo> {

	public int x;
	public int y;
	public String player;
	public int minPrestigeValue = 0;
	public int maxPrestigeValue = 0;


	public Map<ScoutInfoItemType, String> info = new HashMap<ScoutInfoItemType, String>();
	
	public Cell cell;
	public String alliance;

	public void parse(String line) {
		info.clear();
		String tokens[] = line.split("\\$");
		for (String token : tokens) {
			String items[] = token.split("#");
			ScoutInfoItemType itemType = ScoutInfoItemType.getByIndex(items[0]);
			switch (itemType) {
			case X:
				this.x = Integer.parseInt(items[1]);
				break;
			case Y:
				this.y = Integer.parseInt(items[1]);
				break;
			case PLAYER:
				if ( items.length > 1 ) {
					this.player = items[1];
				}
				break;
			case INFANTRY:
				info.put(itemType, items[1]);
				this.minPrestigeValue += getMinPrestigeValue(items[1]);
				this.maxPrestigeValue += getMaxPrestigeValue(items[1]);
				break;
			case ARCHERS:
				info.put(itemType, items[1]);
				this.minPrestigeValue += 2*getMinPrestigeValue(items[1]);
				this.maxPrestigeValue += 2*getMaxPrestigeValue(items[1]);
				break;
			case CAVALRY:
				info.put(itemType, items[1]);
				this.minPrestigeValue += 4*getMinPrestigeValue(items[1]);
				this.maxPrestigeValue += 4*getMaxPrestigeValue(items[1]);
				break;
			case CATAPULTS:
				info.put(itemType, items[1]);
				this.minPrestigeValue += 10*getMinPrestigeValue(items[1]);
				this.maxPrestigeValue += 10*getMaxPrestigeValue(items[1]);
				break;
			default:
				if ( items.length > 1 ) {
					info.put(itemType, items[1]);
				}
			}
		}
	}
	
	private static int getMinPrestigeValue(String text) {
		if ( text.equals(": Several of.")) {
			return 0;
		} else if ( text.equals(": One or two dozen.")) {
			return 10;
		} else if ( text.equals(": Dozens of.")) {
			return 25;
		} else if ( text.equals(": Scores of.")) {
			return 50;
		} else if ( text.equals(": One or two hundred.")) {
			return 100;
		} else if ( text.equals(": Several hundred.")) {
			return 250;
		} else if ( text.equals(": Hundreds of.")) {
			return 500;
		} else if ( text.equals(": One or two thousand.")) {
			return 1000;
		} else if ( text.equals(": Several thousand.")) {
			return 2500;
		} else if ( text.equals(": Thousands of.")) {
			return 5000;
		} else if ( text.equals(": Over 100 hundred.")) {
			return 10000;
		} else {
			return Integer.parseInt(text);
		}
	}
	
	private static int getMaxPrestigeValue(String text) {
		if ( text.equals(": Several of.")) {
			return 0;
		} else if ( text.equals(": One or two dozen.")) {
			return 25;
		} else if ( text.equals(": Dozens of.")) {
			return 50;
		} else if ( text.equals(": Scores of.")) {
			return 100;
		} else if ( text.equals(": One or two hundred.")) {
			return 250;
		} else if ( text.equals(": Several hundred.")) {
			return 500;
		} else if ( text.equals(": Hundreds of.")) {
			return 1000;
		} else if ( text.equals(": One or two thousand.")) {
			return 2500;
		} else if ( text.equals(": Several thousand.")) {
			return 5000;
		} else if ( text.equals(": Thousands of.")) {
			return 10000;
		} else if ( text.equals(": Over 100 hundred.")) {
			return 20000;
		} else {
			return Integer.parseInt(text);
		}
	}
	
	public String getProperty(String property) {
		return info.get(ScoutInfoItemType.valueOf(property));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (ScoutInfoItemType itemType : ScoutInfoItemType.values()) {
			String value = info.get(itemType);
			if ( value != null ) {
				sb.append("\n");
				sb.append(itemType.name());
				sb.append(" ");
				sb.append(value);
			}
		}
		return  sb.toString();
	}
	
	public List<String> getHtmlLines() {
		StringBuilder sb = new StringBuilder();
		List<String> lines = new ArrayList<String>();
		for (ScoutInfoItemType itemType : ScoutInfoItemType.values()) {
			String value = info.get(itemType);
			if ( value != null ) {
				sb.append(" ");
				sb.append(itemType.name());
				sb.append(" ");
				sb.append(value);
				if ( itemType == CONVINCE || itemType == FORTUNE || itemType == CATAPULTS || itemType == CROP || itemType == RING) {
					lines.add(sb.toString());
					sb = new StringBuilder();
				}
			}
		}
		return lines;
	}
	
	public String getPlayer() {
		return player;
	}
	
	
	public void setAlliance(String alliance) {
		this.alliance = alliance;
	}
	
	public String getAlliance() {
		return alliance;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() { 
		return y;
	}
	
	public String getPrestigeValueStr() {
		if ( minPrestigeValue == maxPrestigeValue ) {
			return String.valueOf(minPrestigeValue);
		} else {
			return minPrestigeValue + "-" + maxPrestigeValue;
		}
	}
	
	public int getMinPrestigeValue() {
		return minPrestigeValue;
	}

	public int getMaxPrestigeValue() {
		return maxPrestigeValue;
	}

	public void setMinPrestigeValue(int minPrestigeValue) {
		this.minPrestigeValue = minPrestigeValue;
	}

	public void setMaxPrestigeValue(int maxPrestigeValue) {
		this.maxPrestigeValue = maxPrestigeValue;
	}

	public String getArmy() {
		return info.get(INFANTRY) + " | " + info.get(CAVALRY) + " | " + info.get(ARCHERS) + " | " + info.get(CATAPULTS); 
	}
	
	public boolean getImportant() {
		return getMinPrestigeValue() > 500;
	}
	
	public boolean getFarm() {
		return "449300".equals(info.get(WOOD)) || "449300".equals(info.get(IRON)) || "449300".equals(info.get(CROP));
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}
	
	@Override
	public int compareTo(ScoutInfo o) {
		return (o.maxPrestigeValue + o.minPrestigeValue) /2 - (this.maxPrestigeValue + this.minPrestigeValue) / 2;
	}
	
	
}
