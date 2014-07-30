package haypi.comm;

import haypi.model.pojo.Cell;
import haypi.model.pojo.CellXY;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

public class ListMapResponse extends BaseResponse {

	List<Cell> cells = new ArrayList<Cell>();

	public ListMapResponse(ListMapRequest request, String response) throws IOException, HaypiException {
		super(request, response);

		String line;
		Cell cell = null;
		try {
			while (StringUtils.hasLength((line = readLine()))) {
				cell = new Cell();
				CellXY cellxy = new CellXY(request.getServerName(), Integer.parseInt(line), Integer.parseInt(readLine()));
				cell.setCellXY(cellxy);
				String ownerName = readLine();
				cell.setOwner(ownerName);
				cell.setType(Integer.parseInt(readLine()));
				cell.setName(readLine());
				String nextLine = readLine();
				// bug fix, sometimes empty lines
				boolean levelSet = false;
				while (!levelSet) {
					try {
						while (!StringUtils.hasLength(nextLine)) {
							nextLine = readLine();
						}
						cell.setLevel(Integer.parseInt(nextLine));
						levelSet = true;
					} catch (NumberFormatException e) {
						cell.setName(cell.getName() + '#' + nextLine);
						nextLine = null;
					}
				}

				String allianceName = readLine();
				if (StringUtils.hasLength(allianceName)) {
						cell.setAlliance(allianceName);
				}
				cells.add(cell);
			}
		} catch (Exception e) {
			log.error("Error " + e + ", cell=" + cell + " response=" + response);
		}
	}

	@Override
	public String toString() {
		return cells.toString();
	}

	public List<Cell> getCells() {
		return cells;
	}
}
