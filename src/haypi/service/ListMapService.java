package haypi.service;

import haypi.comm.SessionController;
import haypi.model.pojo.Cell;
import haypi.model.pojo.Point;

import java.util.List;

public interface ListMapService {

	public static final int X_MAX = 600;
	public static final int Y_MAX = 600;

	public List<Cell> listMap(SessionController controller, int x, int y) throws Exception;

	public List<Cell> listMap(SessionController controller, int x, int y, int radius, boolean store, boolean insertOnly) throws Exception;

	public Point getTopLeftCorner(int x, int y, int radius);

	public Point getBottomRightCorner(int x, int y, int radius);
	
	public Integer getProgress();

}