/**
 * 
 */
package haypi.model.search;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.transform.ResultTransformer;

/**
 * @author Cosmin.Mutu
 * 
 */
public class Search {

	private String searchColumn1;
	private String searchColumn2;
	private String searchColumn3;
	private String searchColumn4;
	private String searchColumn5;
	private String searchColumn6;
	private String searchColumn7;
	private String searchColumn8;

	private List<String> errorMessages;

	private Criterion restrictions;

	private Projection projections;
	private String projectionsCountDistinctProperty;
	private ResultTransformer resultTransformer;
	private DetachedCriteria detachedCriteria;

	public Search() {
	}

	public Search(DetachedCriteria detachedCriteria) {
		this.detachedCriteria = detachedCriteria;
	}

	public Search(Criterion restrictions) {
		this.restrictions = restrictions;
	}

	/**
	 * Create a new search object
	 * 
	 * @param restrictions
	 *            filter results based on columns (equals, greater than, lesser
	 *            than, not null, etc)
	 * @param projections
	 *            perform projections (sum, avg, distinct, etc)
	 * @param projectionsCountDistinctProperty
	 *            (if projections and pagination are used, a column name must be
	 *            specified to perform count on it)
	 * @param resultTransformer
	 *            a result transformer must be used with projections, e.q:
	 *            Transformers.aliasToBean(ClassName.class)
	 * 
	 */
	public Search(Criterion restrictions, Projection projections, String projectionsCountDistinctProperty, ResultTransformer resultTransformer) {
		this.restrictions = restrictions;
		this.projections = projections;
		this.projectionsCountDistinctProperty = projectionsCountDistinctProperty;
		this.resultTransformer = resultTransformer;
	}

	public Criterion getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(Criterion restrictions) {
		this.restrictions = restrictions;
	}

	public Projection getProjections() {
		return projections;
	}

	public void setProjections(Projection projections) {
		this.projections = projections;
	}

	public String getProjectionsCountDistinctProperty() {
		return projectionsCountDistinctProperty;
	}

	public ResultTransformer getResultTransformer() {
		return resultTransformer;
	}

	public void setResultTransformer(ResultTransformer resultTransformer) {
		this.resultTransformer = resultTransformer;
	}

	public void setProjectionsCountDistinctProperty(String projectionsCountDistinctProperty) {
		this.projectionsCountDistinctProperty = projectionsCountDistinctProperty;
	}

	public String getSearchColumn1() {
		return searchColumn1;
	}

	public void setSearchColumn1(String searchColumn1) {
		this.searchColumn1 = (searchColumn1 == null) ? searchColumn1 : searchColumn1.trim();
	}

	public String getSearchColumn2() {
		return searchColumn2;
	}

	public void setSearchColumn2(String searchColumn2) {
		this.searchColumn2 = (searchColumn2 == null) ? searchColumn2 : searchColumn2.trim();
	}

	public String getSearchColumn3() {
		return searchColumn3;
	}

	public void setSearchColumn3(String searchColumn3) {
		this.searchColumn3 = (searchColumn3 == null) ? searchColumn3 : searchColumn3.trim();
	}

	public String getSearchColumn4() {
		return searchColumn4;
	}

	public void setSearchColumn4(String searchColumn4) {
		this.searchColumn4 = (searchColumn4 == null) ? searchColumn4 : searchColumn4.trim();
	}

	public String getSearchColumn5() {
		return searchColumn5;
	}

	public void setSearchColumn5(String searchColumn5) {
		this.searchColumn5 = (searchColumn5 == null) ? searchColumn5 : searchColumn5.trim();
	}

	public String getSearchColumn6() {
		return searchColumn6;
	}

	public void setSearchColumn6(String searchColumn6) {
		this.searchColumn6 = (searchColumn6 == null) ? searchColumn6 : searchColumn6.trim();
	}

	public String getSearchColumn7() {
		return searchColumn7;
	}

	public void setSearchColumn7(String searchColumn7) {
		this.searchColumn7 = searchColumn7;
	}

	public String getSearchColumn8() {
		return searchColumn8;
	}

	public void setSearchColumn8(String searchColumn8) {
		this.searchColumn8 = searchColumn8;
	}
	
	public DetachedCriteria getDetachedCriteria() {
		return detachedCriteria;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public void addErrorMessage(String errorMessage) {
		if (errorMessages == null)
			errorMessages = new ArrayList<String>();
		errorMessages.add(errorMessage);
	}

	@Override
	public String toString() {
		return "Search [errorMessages=" + errorMessages + ", restrictions=" + restrictions + ", searchColumn1=" + searchColumn1 + ", searchColumn2="
				+ searchColumn2 + ", searchColumn3=" + searchColumn3 + ", searchColumn4=" + searchColumn4 + ", searchColumn5=" + searchColumn5
				+ ", searchColumn6=" + searchColumn6 + ", searchColumn7=" + searchColumn7 + ", searchColumn8=" + searchColumn8 + "]";
	}

}
