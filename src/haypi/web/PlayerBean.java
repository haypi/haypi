package haypi.web;

import haypi.comm.RankType;
import haypi.comm.SessionController;
import haypi.model.pojo.Cell;
import haypi.model.pojo.Player;
import haypi.service.CellService;
import haypi.service.PlayerRankService;
import haypi.service.PlayerService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean
@ViewScoped
public class PlayerBean extends BasicBean {
	private static final long serialVersionUID = 1L;
	protected final Log log = LogFactory.getLog(getClass());

	@ManagedProperty(value = "#{cellService}")
	CellService cellService;

	@ManagedProperty(value = "#{playerService}")
	PlayerService playerService;

	@ManagedProperty(value = "#{playerRankService}")
	PlayerRankService playerRankService;

	Player selectedPlayer;
	String selectedAlliance;
	
	Integer fromPage = 0;
	Integer toPage = 0;
	
	List<Cell> selectedPlayerCells = null;
	List<Player> selectedPlayersInAlliance = null;

	public void setCellService(CellService cellService) {
		this.cellService = cellService;
	}

	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	public void setPlayerRankService(PlayerRankService playerRankService) {
		this.playerRankService = playerRankService;
	}

	public Player getSelectedPlayer() {
		return selectedPlayer;
	}

	public void setSelectedPlayer(Player selectedPlayer) {
		if ( this.selectedPlayer == null || !this.selectedPlayer.equals(selectedPlayer)) {
			selectedPlayerCells = null;
		}
		this.selectedPlayer = selectedPlayer;
	}

	public String getSelectedAlliance() {
		return selectedAlliance;
	}

	public void setSelectedAlliance(String selectedAlliance) {
		if ( this.selectedAlliance == null || !this.selectedAlliance.equals(selectedAlliance)) {
			selectedPlayersInAlliance = null;
		}
		this.selectedAlliance = selectedAlliance;
	}
	
	public Integer getFromPage() {
		return fromPage;
	}

	public Integer getToPage() {
		return toPage;
	}

	public void setFromPage(Integer fromPage) {
		this.fromPage = fromPage;
	}

	public void setToPage(Integer toPage) {
		this.toPage = toPage;
	}

	public List<Player> autocompletePlayer(String query) {
		String server = null;
		if ( getPrincipal() != null ) {
			SessionController controller = getPrincipal().getSessionController();
			if ( controller != null ) {
				server = controller.getServerName();
			}
		}
		return playerService.getPlayersByNamePrefix(server, query);
	}

	public List<String> autocompleteAlliance(String query) {
		String server = null;
		if ( getPrincipal() != null ) {
			SessionController controller = getPrincipal().getSessionController();
			if ( controller != null ) {
				server = controller.getServerName();
			}
		}
		return playerService.getAlliancesByNamePrefix(server, query);
	}

	public List<Cell> getCells() {
		if (selectedPlayer == null) {
			return null;
		} else if ( selectedPlayerCells == null ){
			selectedPlayerCells = playerService.getCells(selectedPlayer);
		}
		return selectedPlayerCells;
	}

	public List<Cell> getPlayerCells(Player player) {
		if (player == null) {
			return null;
		} else {
			return playerService.getCells(player);
		}
	}

	public List<Player> getPlayersInAlliance() {
		if (selectedAlliance == null || selectedAlliance.length() == 0) {
			return null;
		} else if ( selectedPlayersInAlliance == null ) {
			String server = null;
			if ( getPrincipal() != null ) {
				SessionController controller = getPrincipal().getSessionController();
				if ( controller != null ) {
					server = controller.getServerName();
				}
			}

			selectedPlayersInAlliance = playerService.getPlayersByAlliance(server, selectedAlliance);
			Collections.sort(selectedPlayersInAlliance, new Comparator<Player>() {

				@Override
				public int compare(Player o1, Player o2) {
					if ( o1.getPrestigeRank() == null || o1.getPrestigeRank() == 0 ) {
						return 1;
					}
					if ( o2.getPrestigeRank() == null || o2.getPrestigeRank() == 0 ) {
						return -1;
					}
					return o1.getPrestigeRank().compareTo(o2.getPrestigeRank());
				}
			});
		}
		return selectedPlayersInAlliance;
	}
	
	public String storeRankPrestige() throws Exception {
		SessionController controller = getPrincipal().getSessionController();
		playerRankService.listRank(controller, RankType.PRESTIGE, fromPage, toPage, true);
		return "";
	}
	
	public String storeRankLevel() throws Exception {
		SessionController controller = getPrincipal().getSessionController();
		playerRankService.listRank(controller, RankType.LEVEL, fromPage, toPage, true);
		return "";
	}
	
	public void handleProgressComplete() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completed", "Completed"));
	}

	public Integer getProgress() {
		return playerRankService.getProgress();
	}

	public void setProgress(Integer progress) {
		log.info("Set progress:" + progress);
	}
	
}
