package haypi.model.search;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class PlayerSearch {
	public final static Search byNamePrefix(String server, String namePrefix) {
		Criterion criterion = Restrictions.ilike("name", namePrefix, MatchMode.START);
		if ( server != null ) {
			criterion = Restrictions.and(criterion, Restrictions.eq("server", server));
		}
		return new Search(criterion);
	}

	public final static Search byNameIgnoreCase(String server, String name) {
		Criterion criterion = Restrictions.ilike("name", name, MatchMode.EXACT);
		if ( server != null ) {
			criterion = Restrictions.and(criterion, Restrictions.eq("server", server));
		}
		return new Search(criterion);
	}

	public final static Search byAlliance(String server, String alliance) {
		Criterion criterion = Restrictions.ilike("alliance", alliance, MatchMode.EXACT);
		if ( server != null ) {
			criterion = Restrictions.and(criterion, Restrictions.eq("server", server));
		}
		return new Search(criterion);
	}
}
