package haypi.service;

import haypi.comm.ListMapRequest;
import haypi.comm.ListMapResponse;
import haypi.comm.SessionController;
import haypi.model.pojo.Cell;
import haypi.model.pojo.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("listMapService")
public class ListMapServiceImpl implements ListMapService {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	protected CellService cellService;

	protected Integer progress;

	@Override
	public List<Cell> listMap(SessionController controller, int x, int y) throws Exception {
		log.debug("ListMapService request: x=" + x + " y=" + y);

		ListMapRequest listMapRequest = new ListMapRequest(x, y);
		ListMapResponse listMapResponse = (ListMapResponse) controller.sendCommand(listMapRequest);
		List<Cell> result = listMapResponse.getCells();
		log.debug("ListMapService result:" + result);
		return result;

	}

	@Override
	public List<Cell> listMap(SessionController controller, int x, int y, int radius, boolean store, boolean insertOnly) throws Exception {
		List<Cell> result = new ArrayList<Cell>();
		log.debug("ListMapService request: x=" + x + " y=" + y + " radius=" + radius);

		Point point = new Point(x, y);

		Point minTopLeft = getTopLeftCorner(x, y, radius);
		Point maxBottomRight = getBottomRightCorner(x, y, radius);
		result = listMapRecursive(controller, point, minTopLeft, maxBottomRight, store, insertOnly);

		log.debug("ListMapService result:" + result);
		return result;
	}

	public Point getTopLeftCorner(int x, int y, int radius) {
		Point minTopLeft = new Point(x + 4 - 7 * radius, y + 4 - 7 * radius);
		if (minTopLeft.x < 0) {
			minTopLeft.x = 0;
		}
		if (minTopLeft.y < 0) {
			minTopLeft.y = 0;
		}
		return minTopLeft;
	}

	public Point getBottomRightCorner(int x, int y, int radius) {
		Point maxBottomRight = new Point(x - 4 + 7 * radius, y - 4 + 7 * radius);
		if (maxBottomRight.x > X_MAX) {
			maxBottomRight.x = X_MAX;
		}
		if (maxBottomRight.y > Y_MAX) {
			maxBottomRight.y = Y_MAX;
		}
		return maxBottomRight;
	}

	private List<Cell> listMapRecursive(SessionController controller, Point point, Point minTopLeft, Point maxBottomRight, boolean store,
			boolean insertOnly) throws Exception {
		List<Cell> result = new ArrayList<Cell>();
		LRUMap cache = new LRUMap(360000);
		TreeSet<Point> pointsToScan = new TreeSet<Point>();
		pointsToScan.add(point);

		int radius = ((maxBottomRight.x - minTopLeft.x + 1) / 7 + 1) / 2;
		int totalPoints = radius * (radius + 1) / 2 * 8 + 1;

		setProgress(new Integer(0));

		while (!pointsToScan.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("Scanning " + point + ", still " + pointsToScan.size() + " to go");
			}
			point = pointsToScan.first();
			pointsToScan.remove(point);
			if (point.x < 0 || point.y < 0 || point.x < minTopLeft.x || point.y < minTopLeft.y || point.x > maxBottomRight.x
					|| point.y > maxBottomRight.y) {
				// out of bounds
				continue;
			}

			cache.put(point, point);

			boolean incomplete = true;
			// add current point
			if (store && insertOnly) {
				// check if 7x7 area already in db
				List<Cell> savedCells = cellService.getCellByXYAndRange(controller.getServerName(), point.x, point.y, 4);
				Point tlc = getTopLeftCorner(point.x, point.y, 1);
				Point brc = getBottomRightCorner(point.x, point.y, 1);
				incomplete = savedCells.size() < (brc.x-tlc.x+1)*(brc.y-tlc.y+1);
			}
			

			if (!store || !insertOnly || incomplete) {
				result.addAll(listMap(controller, point.x, point.y));
			}

			if (store) {
				// do not return objects, but write them to database instead
				try {
					cellService.saveCells(result);
				} catch (Exception e) {
					log.error("Error storing cells", e);
				}
				result.clear();
			}

			// don't explore further if this was on the border (because it might
			// return to the center with an offset)
			if (point.x > minTopLeft.x && point.y < maxBottomRight.y && point.x < maxBottomRight.x && point.y > minTopLeft.y) {
				// go recursive for all
				Point topLeft = getNextPoint(point, -7, -7, minTopLeft, maxBottomRight);
				if (!cache.containsKey(topLeft)) {
					pointsToScan.add(topLeft);
				}
				Point top = getNextPoint(point, 0, -7, minTopLeft, maxBottomRight);
				if (!cache.containsKey(top)) {
					pointsToScan.add(top);
				}
				Point topRight = getNextPoint(point, 7, -7, minTopLeft, maxBottomRight);
				if (!cache.containsKey(topRight)) {
					pointsToScan.add(topRight);
				}
				Point left = getNextPoint(point, -7, 0, minTopLeft, maxBottomRight);
				if (!cache.containsKey(left)) {
					pointsToScan.add(left);
				}
				Point right = getNextPoint(point, 7, 0, minTopLeft, maxBottomRight);
				if (!cache.containsKey(right)) {
					pointsToScan.add(right);
				}
				Point bottomLeft = getNextPoint(point, -7, 7, minTopLeft, maxBottomRight);
				if (!cache.containsKey(bottomLeft)) {
					pointsToScan.add(bottomLeft);
				}
				Point bottom = getNextPoint(point, 0, 7, minTopLeft, maxBottomRight);
				if (!cache.containsKey(bottom)) {
					pointsToScan.add(bottom);
				}
				Point bottomRight = getNextPoint(point, 7, 7, minTopLeft, maxBottomRight);
				if (!cache.containsKey(bottomRight)) {
					pointsToScan.add(bottomRight);
				}
			}

			if (totalPoints != 0) {
				setProgress(100 * cache.size() / totalPoints);
			}

		}

		//setProgress(null);
		return result;
	}

	private Point getNextPoint(Point prevPoint, int xOffset, int yOffset, Point minTopLeft, Point maxBottomRight) {
		Point point = new Point(prevPoint.x + xOffset, prevPoint.y + yOffset);
		if (point.x < minTopLeft.x) {
			point.x = minTopLeft.x;
		}
		if (point.x > maxBottomRight.x) {
			point.x = maxBottomRight.x;
		}
		if (point.y < minTopLeft.y) {
			point.y = minTopLeft.y;
		}
		if (point.y > maxBottomRight.y) {
			point.y = maxBottomRight.y;
		}
		return point;
	}

	public void setCellService(CellService cellService) {
		this.cellService = cellService;
	}

	public Integer getProgress() {
		return this.progress;
	}

	public synchronized void setProgress(Integer progress) {
		this.progress = progress;
	}

}
