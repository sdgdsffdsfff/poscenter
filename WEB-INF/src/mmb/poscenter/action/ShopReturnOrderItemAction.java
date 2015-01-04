package mmb.poscenter.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.ShopReturnOrder;
import mmb.poscenter.domain.ShopReturnOrderItem;
import mmb.poscenter.service.ShopReturnOrderItemService;
import mmb.poscenter.service.ShopReturnOrderService;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class ShopReturnOrderItemAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private ShopReturnOrderItemService srois = new ShopReturnOrderItemService();
	private Page<ShopReturnOrderItem> page = new Page<ShopReturnOrderItem>();
	private ShopReturnOrder shopReturnOrder = new ShopReturnOrder();
	private int shopReturnOrderId;
	
	public Page<ShopReturnOrderItem> getPage() {
		return page;
	}

	public void setPage(Page<ShopReturnOrderItem> page) {
		this.page = page;
	}

	public ShopReturnOrder getShopReturnOrder() {
		return shopReturnOrder;
	}

	public void setShopReturnOrder(ShopReturnOrder shopReturnOrder) {
		this.shopReturnOrder = shopReturnOrder;
	}

	public int getShopReturnOrderId() {
		return shopReturnOrderId;
	}

	public void setShopReturnOrderId(int shopReturnOrderId) {
		this.shopReturnOrderId = shopReturnOrderId;
	}

	/**
	 * 跳转至店面退货单条目列表界面
	 * @return
	 */
	public String shopReturnOrderItemList(){
		HttpServletRequest request = ServletActionContext.getRequest();
		//获取店面退货单信息
		ShopReturnOrderService sos = new ShopReturnOrderService();
		this.shopReturnOrder = sos.getDetailById(this.shopReturnOrderId);
		
		//获取店面退货单下的所有店面退货单条目数据
		List<ShopReturnOrderItem> allItemList = srois.getAllItemListByOrderId(shopReturnOrderId);
		request.setAttribute("allItemList", allItemList);
		return SUCCESS;
	}

}
