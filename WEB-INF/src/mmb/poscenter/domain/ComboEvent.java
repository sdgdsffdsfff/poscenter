package mmb.poscenter.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 套餐活动
 */
public class ComboEvent implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 套餐活动id
	 */
	public int id;
	
	/**
	 * 活动id
	 */
	public int eventId;
	
	/**
	 * 套餐价
	 */
	public double comboPrice;
	
	/**
	 * 套餐商品列表
	 */
	private List<ComboProduct> comboProductList = new ArrayList<ComboProduct>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public double getComboPrice() {
		return comboPrice;
	}

	public void setComboPrice(double comboPrice) {
		this.comboPrice = comboPrice;
	}

	public List<ComboProduct> getComboProductList() {
		return comboProductList;
	}

	public void setComboProductList(List<ComboProduct> comboProductList) {
		this.comboProductList = comboProductList;
	}
	
}
