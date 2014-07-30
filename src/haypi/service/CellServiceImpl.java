package haypi.service;

import haypi.model.dao.CellDao;
import haypi.model.dao.PlayerDao;
import haypi.model.pojo.Cell;
import haypi.model.pojo.Player;
import haypi.model.search.CellSearch;
import haypi.model.search.Sort;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("cellService")
public class CellServiceImpl implements CellService {
	private final Log log = LogFactory.getLog(getClass());

	protected CellDao cellDao;
	protected PlayerDao playerDao;
	
	@Autowired
	public void setCellDao(CellDao cellDao) {
		this.cellDao = cellDao;
	}

	@Autowired
	public void setPlayerDao(PlayerDao playerDao) {
		this.playerDao = playerDao;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveCells(List<Cell> cells) {
		if (log.isDebugEnabled()) {
			log.debug("CellService.saveCells: " + cells.toString());
		}
		Calendar now = Calendar.getInstance();
		now.clear(Calendar.MILLISECOND);

		// clear previous objects in cache
		cellDao.flush();
		
		for (Cell cell : cells) {
			cell.setLastUpdate(now.getTime());
			cellDao.saveOrUpdate(cell);
		}
	}
	
	@Override
	public Cell getCell(String server, int x, int y) {
		List<Cell> cells = cellDao.getObjects(Cell.class, null, null, CellSearch.byXY(server, x,y));
		if ( cells.isEmpty() ) return null;
		return cells.get(0);
	}

	@Override
	public List<Cell> getCellByRowAndXRange(String server, int row, int minX, int maxX) {
		Sort sort = new Sort(Order.asc("cellXY.x"));
		List<Cell> cells = cellDao.getObjects(Cell.class, null, sort, CellSearch.byRowAndXRange(server, row, minX, maxX));
		return cells;
	}
	
	@Override
	public List<Cell> getCellByOwner(String server, String owner) {
		List<Cell> cells = cellDao.getObjects(Cell.class, null, null, CellSearch.byOwner(server, owner));
		return cells;
	}
	
	@Override
	public List<Cell> getCellByXYAndRange(String server, int x, int y, int range) {
		List<Cell> cells = cellDao.getObjects(Cell.class, null, null, CellSearch.byXYAndRange(server, x, y, range));
		return cells;
	}
	
	@Override
	public List<Cell> getCellByXYAndRangeAndLevel(String server, int x, int y, int range, int minPlayerLevel) {
		List<Cell> cells = cellDao.getObjects(Cell.class, null, null, CellSearch.byXYAndRangeAndLevel(server, x, y, range, minPlayerLevel));
		return cells;
	}

}
