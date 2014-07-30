/**
 * 
 */
package haypi.model.search;

/**
 * @author Cosmin.Mutu
 * 
 */
public class Page {

	private static final long serialVersionUID = 1L;

	private int currentPage;
	private int totalResults;
	private int resultsPerPage;

	public Page() {
		currentPage = 1;
		resultsPerPage = getResultsPerPage();
	}

	public Page(int currentPage, int resultsPerPage) {
		this.currentPage = currentPage;
		this.resultsPerPage = resultsPerPage;
	}

	public Page(int currentPage, int resultsPerPage, int totalResults) {
		this.currentPage = currentPage;
		this.resultsPerPage = resultsPerPage;
		this.totalResults = totalResults;
	}

	public int getFirstResult() {
		return getPrevPage() * resultsPerPage;
	}

	public boolean getHasPrevPage() {
		return (1 < currentPage) ? true : false;
	}

	public int getPrevPage() {
			return currentPage - 1;
	}

	public boolean getHasNextPage() {
		return (currentPage * resultsPerPage < totalResults) ? true : false;
	}

	public int getNextPage() {
			return currentPage + 1;
	}

	public int getTotalPages() {
		return (int) Math.ceil((double) totalResults / (double) resultsPerPage);
	}

	public int getFirstPage() {
		return 1;
	}

	public int getLastPage() {
		return getTotalPages();
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		if (currentPage == null || currentPage.isEmpty()) {
			this.currentPage = 1;
		} else {
			this.currentPage = Integer.parseInt(currentPage);
		}
	}

	public void setCurrentPage(int currentPage) {		
		this.currentPage = currentPage;	
	}

	public int getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

	public void setResultsPerPage(String resultsPerPage) {
		if (resultsPerPage == null || resultsPerPage.isEmpty()) {
			this.resultsPerPage = 10;
		} else {
			this.resultsPerPage = Integer.parseInt(resultsPerPage);
		}
	}

	public int getResultsPerPage() {
		return resultsPerPage;
	}

	public int getFirstEntry() {
		return (currentPage - 1) * resultsPerPage + 1;
	}

	public int getLastEntry() {
		if (totalResults < currentPage * resultsPerPage) {
			return totalResults;
		}
		return currentPage * resultsPerPage;
	}

	public void setResultsPerPage(int resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}

	@Override
	public String toString() {
		return "Page [currentPage=" + currentPage + ", resultsPerPage=" + resultsPerPage + ", totalResults="
				+ totalResults + "]";
	}

}
