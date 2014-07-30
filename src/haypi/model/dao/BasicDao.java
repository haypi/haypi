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

public interface BasicDao<T extends BasicObject> {

	public int count(Class<T> className, Search search);

	public List<T> getObjects(Class<T> clazz, Page page, Sort sort, Search search);

	public Serializable save(T object);

	public Serializable saveAndFlush(final T object);

	public T merge(T object);

	public T mergeAndFlush(T object);

	public T saveOrUpdate(T object);

	public T saveOrUpdateAndFlush(T object);

	public void flush();

	public void update(T object);

	public void updateAndFlush(final T object);

	public int bulkUpdate(String queryString);

	public void delete(T object);
	
	public void evict(T object);
}
