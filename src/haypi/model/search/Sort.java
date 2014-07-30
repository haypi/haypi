/**
 * 
 */
package haypi.model.search;

import java.util.ArrayList;

import org.hibernate.criterion.Order;

/**
 * @author Cosmin.Mutu
 * 
 */
public class Sort {

	public final static String ASCENDING = "ASC";
	public final static String DESCENDING = "DESC";

	private String columnName;
	private String columnOrder;

	private ArrayList<Order> orders;

	public Sort() {
	}
	
	public Sort(Order order) {
		orders = new ArrayList<Order>();
		orders.add(order);
	}

	public Sort( ArrayList<Order> orders) {
		this.orders = orders;
	}
	
	public void addOrder(Order order) {
		if (orders == null) {
			orders = new ArrayList<Order>();
		}
		orders.add(order);
	}

	public ArrayList<Order> getOrders() {
		return orders;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setColumnOrder(String columnOrder) {
		this.columnOrder = columnOrder;
	}

	public String getColumnOrder() {
		return columnOrder;
	}

	@Override
	public String toString() {
		return "Sort [columnName=" + columnName + ", columnOrder=" + columnOrder + ", orders=" + orders + "]";
	}

}
