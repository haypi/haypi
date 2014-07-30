package haypi.model.search;

import haypi.model.pojo.Cell;
import haypi.model.pojo.CellXY;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class CellSearch {
	public final static Search byXY(String server, int x, int y) {
		CellXY xy = new CellXY(server, x, y);
		Criterion criterion = Restrictions.eq("cellXY", xy);

		return new Search(criterion);
	}

	public final static Search byRowAndXRange(String server, int row, int minX, int maxX) {
		Criterion criterion = Restrictions.eq("cellXY.server", server);
		criterion = Restrictions.and(criterion, Restrictions.eq("cellXY.y", row));
		Criterion criterionXmin = Restrictions.ge("cellXY.x", minX);
		Criterion criterionXmax = Restrictions.le("cellXY.x", maxX);
		criterion = Restrictions.and(criterion, Restrictions.and(criterionXmin, criterionXmax));

		return new Search(criterion);
	}

	public final static Search byXYAndRangeAndLevel(String server, int x, int y, int range, int minPlayerLevel) {
		DetachedCriteria c1 = DetachedCriteria.forClass(Cell.class);

		Criterion criterionXmin = Restrictions.gt("cellXY.x", x - range);
		Criterion criterionXmax = Restrictions.lt("cellXY.x", x + range);
		Criterion criterionYmin = Restrictions.gt("cellXY.y", y - range);
		Criterion criterionYmax = Restrictions.lt("cellXY.y", y + range);
		Criterion criterionServer = Restrictions.eq("cellXY.server", server);

		c1.add(Restrictions.and(
				criterionServer,
				Restrictions.and(Restrictions.isNotNull("owner"),
						Restrictions.and(criterionXmin, Restrictions.and(criterionXmax, Restrictions.and(criterionYmin, criterionYmax))))));

		// Criterion criterionOwnerLevel = Restrictions.ge("level",
		// minPlayerLevel);
		// DetachedCriteria c2 = c1.createCriteria("owner");
		// c2.add(criterionOwnerLevel);

		return new Search(c1);
	}

	public final static Search byXYAndRange(String server, int x, int y, int range) {
		Criterion criterionXmin = Restrictions.gt("cellXY.x", x - range);
		Criterion criterionXmax = Restrictions.lt("cellXY.x", x + range);
		Criterion criterionYmin = Restrictions.gt("cellXY.y", y - range);
		Criterion criterionYmax = Restrictions.lt("cellXY.y", y + range);
		Criterion criterionServer = Restrictions.eq("cellXY.server", server);

		return new Search(Restrictions.and(criterionServer,
				Restrictions.and(criterionXmin, Restrictions.and(criterionXmax, Restrictions.and(criterionYmin, criterionYmax)))));
	}

	public static Search byOwner(String server, String player) {
		return new Search(Restrictions.and(Restrictions.eq("owner", player), Restrictions.eq("cellXY.server", server)));
	}

}
