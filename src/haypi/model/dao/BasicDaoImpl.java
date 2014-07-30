/**
 * 
 */
package haypi.model.dao;

import haypi.model.pojo.BasicObject;
import haypi.model.search.Page;
import haypi.model.search.Search;
import haypi.model.search.Sort;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BasicDaoImpl<T extends BasicObject> extends HibernateDaoSupport implements BasicDao<T> {

	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * Create a detached criteria and apply restrictions on it (search and
	 * sort). Also populates page with total results this criteria will match.
	 * 
	 * @param className
	 * @param page
	 * @param sort
	 * @param search
	 * @return A detached criteria with restrictions applied and ready to be
	 *         used
	 */
	public DetachedCriteria getDetachedCriteria(Class<T> clazz, Page page, Sort sort, Search search) {

		DetachedCriteria criteria = search.getDetachedCriteria();
		if ( criteria == null ) {
			criteria = DetachedCriteria.forClass(clazz);
		}

		if (sort != null && sort.getOrders() != null) {
			for (Order order : sort.getOrders()) {
				criteria.addOrder(order);
			}

		}

		if (search != null && search.getRestrictions() != null) {
			criteria.add(search.getRestrictions());
		}

		if (page != null) {
			page.setTotalResults(countResults(criteria));
		}

		return criteria;
	}

	public int count(Class<T> className, Search search) {
		DetachedCriteria criteria = DetachedCriteria.forClass(className);
		criteria.add(search.getRestrictions());
		return countResults(criteria);
	}

	/**
	 * Counts results for a given criteria
	 * 
	 * @param criteria
	 * @return how many results matched the given criteria
	 */
	@SuppressWarnings("unchecked")
	public int countResults(DetachedCriteria criteria) {
		criteria.setProjection(Projections.rowCount());
		List<Integer> results = getHibernateTemplate().findByCriteria(criteria);
		criteria.setProjection(null);
		if (results.size() > 0) {
			return ((Integer) results.get(0)).intValue();
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public List<T> getObjects(Class<T> clazz, Page page, Sort sort, Search search) {
		if (page != null) {
			return getHibernateTemplate().findByCriteria(getDetachedCriteria(clazz, page, sort, search), page.getFirstResult(),
					page.getResultsPerPage());
		} else {
			return getHibernateTemplate().findByCriteria(getDetachedCriteria(clazz, page, sort, search));
		}
	}

	public Serializable save(T object) {
		return getHibernateTemplate().save(object);
	}

	public T saveOrUpdate(T object) {
		getHibernateTemplate().saveOrUpdate(object);
		return object;
	}

	public T merge(final T object) {
		 getHibernateTemplate().merge(object);
		 return object;
	}

	public T mergeAndFlush(final T object) {
		HibernateCallback<T> hc = new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException {
				session.merge(object);
				session.flush();
				session.clear();
				return object;
			}
		};
		return getHibernateTemplate().execute(hc);
	}

	public T saveOrUpdateAndFlush(final T object) {
		HibernateCallback<T> hc = new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException {
				session.saveOrUpdate(object);
				session.flush();
				session.clear();
				return object;
			}
		};
		return getHibernateTemplate().execute(hc);
	}

	public Serializable saveAndFlush(final T object) {
		HibernateCallback<Serializable> hc = new HibernateCallback<Serializable>() {
			public Serializable doInHibernate(Session session) throws HibernateException {
				Serializable id = session.save(object);
				session.flush();
				session.clear();
				return id;
			}
		};
		return getHibernateTemplate().execute(hc);
	}

	public void flush() {
		getHibernateTemplate().flush();
		getHibernateTemplate().clear();
	}

	public void update(T object) {
		getHibernateTemplate().update(object);
	}

	public void updateAndFlush(final T object) {
		HibernateCallback<T> hc = new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException {
				session.update(object);
				session.flush();
				session.clear();
				return null;
			}
		};
		getHibernateTemplate().execute(hc);
	}

	public int bulkUpdate(String queryString) {
		return getHibernateTemplate().bulkUpdate(queryString);
	}

	public void delete(T object) {
		getHibernateTemplate().delete(object);
	}
	
	public void evict(T object) {
		getHibernateTemplate().evict(object);
	}
}
