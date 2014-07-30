package haypi.service;

import haypi.model.pojo.Cell;

import java.util.List;

public interface CellService {

	public Cell getCell(String server, int x, int y);

	public List<Cell> getCellByRowAndXRange(String server, int row, int minX, int maxX);

	public List<Cell> getCellByXYAndRange(String server, int x, int y, int range);

	public List<Cell> getCellByXYAndRangeAndLevel(String server, int x, int y, int range, int minPlayerLevel);

	public List<Cell> getCellByOwner(String server, String owner);

	public void saveCells(List<Cell> cells);

}