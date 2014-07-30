package haypi.web;

import haypi.comm.SessionController;
import haypi.comm.SessionTimeoutException;
import haypi.model.pojo.Cell;
import haypi.model.pojo.Player;
import haypi.model.pojo.Point;
import haypi.model.pojo.ScoutInfo;
import haypi.security.HaypiUserDetails;
import haypi.service.CellService;
import haypi.service.ListMapService;
import haypi.service.ScoutService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ManagedBean
@ViewScoped
public class HomeBean extends BasicBean {
	private static final long serialVersionUID = 1L;
	protected final Log log = LogFactory.getLog(getClass());

	@ManagedProperty(value = "#{listMapService}")
	ListMapService listMapService;

	@ManagedProperty(value = "#{cellService}")
	CellService cellService;

	@ManagedProperty(value = "#{scoutService}")
	ScoutService scoutService;

	Map<Integer, Map<Integer, Cell>> map = new TreeMap<Integer, Map<Integer, Cell>>();
	List<Cell> cells = null;
	List<ScoutInfo> scoutResult = null;

	Integer x = 0;
	Integer y = 0;
	Integer radius = 1;

	Integer scoutx = 0;
	Integer scouty = 0;
	Integer scoutr = 1;

	Integer minPlayerLevel = 10;
	Boolean scoutOasis = Boolean.FALSE;
	Boolean troopsOnly = Boolean.TRUE;

	public String listSavedMap() throws Exception {
		Point topLeft = listMapService.getTopLeftCorner(x, y, radius);
		Point bottomRight = listMapService.getBottomRightCorner(x, y, radius);
		map.clear();
		SessionController controller = getPrincipal().getSessionController();
		for (int row = topLeft.y; row <= bottomRight.y; row++) {
			List<Cell> cellRow = cellService.getCellByRowAndXRange(controller.getServerName(), row, topLeft.x, bottomRight.x);
			Map<Integer, Cell> rowMap = new HashMap<Integer, Cell>();
			for (Cell cell : cellRow) {
				rowMap.put(new Integer(cell.getX()), cell);
			}
			map.put(new Integer(row), rowMap);
		}
		return "";
	}

	public Integer getProgress() {
		return listMapService.getProgress();
	}

	public void setProgress(Integer progress) {

	}

	public String listMap() throws Exception {
		SessionController controller = getPrincipal().getSessionController();
		if (x == null || y == null) {
			return "";
		}
		map.clear();
		cells = listMapService.listMap(controller, x, y, radius, false, false);
		// convert to map
		for (Cell cell : cells) {
			int row = cell.getY();
			Map<Integer, Cell> rowMap = map.get(row);
			if (rowMap == null) {
				rowMap = new HashMap<Integer, Cell>();
				map.put(row, rowMap);
			}
			// insert current cell
			rowMap.put(new Integer(cell.getX()), cell);
		}
		return "";
	}

	public String storeMap() throws Exception {
		SessionController controller = getPrincipal().getSessionController();
		if (x == null || y == null) {
			return "";
		}
		map.clear();
		cells = listMapService.listMap(controller, x, y, radius, true, false);
		return "";
	}

	public void handleProgressComplete() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completed", "Completed"));
	}

	public void saveCells() {
		if (cells != null) {
			cellService.saveCells(cells);
		}
	}

	public Integer[] getMapRows() {
		return map.keySet().toArray(new Integer[0]);
	}

	public Integer[] getMapColumns(int y) {
		Map<Integer, Cell> rowMap = map.get(y);
		if (rowMap == null) {
			return new Integer[] {};
		}
		Integer[] columns = rowMap.keySet().toArray(new Integer[0]);
		if (columns.length == 0)
			return columns;
		Arrays.sort(columns);
		return columns;
	}

	public Cell[] getMapRowCells(int row) {
		Integer[] columns = getMapColumns(row);
		if (columns.length == 0) {
			return new Cell[] {};
		}
		Cell[] cells = new Cell[columns[columns.length - 1] - columns[0] + 1];
		for (int column = 0; column < cells.length; column++) {
			cells[column] = getMapCell(column + columns[0], row);
		}
		return cells;
	}

	public Cell getMapCell(int x, int y) {
		return map.get(y).get(x);
	}

	public Map<Integer, Map<Integer, Cell>> getMap() {
		return map;
	}

	public void setListMapService(ListMapService listMapService) {
		this.listMapService = listMapService;
	}

	public void setCellService(CellService cellService) {
		this.cellService = cellService;
	}

	public void setScoutService(ScoutService scoutService) {
		this.scoutService = scoutService;
	}

	public Integer getX() {
		return x;
	}

	public Integer getY() {
		return y;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public Integer getMinPlayerLevel() {
		return minPlayerLevel;
	}

	public void setMinPlayerLevel(Integer minPlayerLevel) {
		this.minPlayerLevel = minPlayerLevel;
	}

	public Boolean getScoutOasis() {
		return scoutOasis;
	}

	public void setScoutOasis(Boolean scoutOasis) {
		this.scoutOasis = scoutOasis;
	}
	
	public Boolean getTroopsOnly() {
		return troopsOnly;
	}

	public void setTroopsOnly(Boolean troopsOnly) {
		this.troopsOnly = troopsOnly;
	}

	public String scout() throws Exception {
		HaypiUserDetails principal = getPrincipal();
		if ( principal == null ) {
			getResponse().sendRedirect("/login.html");
			return "";
		}
		SessionController controller = getPrincipal().getSessionController();
		scoutResult = new ArrayList<ScoutInfo>();
		try{
			scoutService.scout(controller, scoutx, scouty, scoutr, !scoutOasis, troopsOnly, minPlayerLevel, scoutResult);
		} catch (SessionTimeoutException e) {
			SecurityContext ctx = SecurityContextHolder.getContext();
			ctx.setAuthentication(null);
			getResponse().sendRedirect("/j_spring_security_logout");
		} catch (Exception e ) {
			if ( e.getCause() != null && e.getCause() instanceof SessionTimeoutException ) {
				SecurityContext ctx = SecurityContextHolder.getContext();
				ctx.setAuthentication(null);
				getResponse().sendRedirect("/j_spring_security_logout");
			} else {
				throw e;
			}
		}
		return "";
	}

	public String scoutPlayer() throws Exception {
		SessionController controller = getPrincipal().getSessionController();
		scoutResult = new ArrayList<ScoutInfo>();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		PlayerBean playerBean = facesContext.getApplication().evaluateExpressionGet(facesContext, "#{playerBean}", PlayerBean.class);
		Player player = playerBean.getSelectedPlayer();
		if ( player != null ) {
			scoutService.scout(controller, player, !scoutOasis, troopsOnly, scoutResult);
		}
		return "";
	}

	public String scoutAlliance() throws Exception {
		return "";
	}

	public List<ScoutInfo> getScoutResult() {
		return scoutResult;
	}

	public Integer getScoutProgress() {
		return scoutService.getProgress();
	}

	public Integer getScoutx() {
		return scoutx;
	}

	public Integer getScouty() {
		return scouty;
	}

	public Integer getScoutr() {
		return scoutr;
	}

	public void setScoutx(Integer scoutx) {
		this.scoutx = scoutx;
	}

	public void setScouty(Integer scouty) {
		this.scouty = scouty;
	}

	public void setScoutr(Integer scoutr) {
		this.scoutr = scoutr;
	}

}
