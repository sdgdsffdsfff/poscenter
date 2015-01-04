package mmb.poscenter.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.PurchaseOrder;
import mmb.poscenter.domain.PurchaseOrderItem;
import mmb.poscenter.service.PurchaseOrderItemService;
import mmb.poscenter.service.PurchaseOrderService;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class PurchaseOrderItemAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private PurchaseOrderItemService pois = new PurchaseOrderItemService();
	private Page<PurchaseOrderItem> page = new Page<PurchaseOrderItem>();
	private PurchaseOrder purchaseOrder = new PurchaseOrder();
	private PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
	private int purchaseOrderId;
	
	public Page<PurchaseOrderItem> getPage() {
		return page;
	}

	public void setPage(Page<PurchaseOrderItem> page) {
		this.page = page;
	}
	
	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public PurchaseOrderItem getPurchaseOrderItem() {
		return purchaseOrderItem;
	}

	public void setPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
		this.purchaseOrderItem = purchaseOrderItem;
	}

	public int getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(int purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	/**
	 * 跳转至采购单条目列表界面
	 * @return
	 */
	public String purchaseOrderItemList(){
		//获取采购单信息
		PurchaseOrderService pos = new PurchaseOrderService();
		this.purchaseOrder = pos.getPurchaseOrderById(this.purchaseOrderId);
		
		//获取查询参数
		HttpServletRequest request = ServletActionContext.getRequest();
		String productName = request.getParameter("productName");
		String supplierName = request.getParameter("supplierName");
		request.setAttribute("productName", productName);
		request.setAttribute("supplierName", supplierName);
		
		//分页获取采购单条目列表信息
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("purchaseOrderId", purchaseOrderId);
		param.put("productName", productName);
		param.put("supplierName", supplierName);
		pois.getPurchaseOrderItemPage(page, param);
		return SUCCESS;
	}

	/**
	 * 跳转到采购单条目表单页面
	 * @return
	 */
	public String toPurchaseOrderItemFormView() {
		//获取采购单信息
		PurchaseOrderService pos = new PurchaseOrderService();
		this.purchaseOrder = pos.getPurchaseOrderById(this.purchaseOrderId);
		
		//修改
		if(purchaseOrderItem.getId() != 0) {
			this.purchaseOrderItem = pois.getDetailById(this.purchaseOrderItem.getId());
		}
		return INPUT;
	}
	
	/**
	 * 保存采购单条目信息
	 * @return
	 */
	public String savePurchaseOrderItem() {
		try {
			//新建
			if(purchaseOrderItem.getId() == 0) {
				pois.addXXX(purchaseOrderItem, "purchase_order_item");
			}
			//修改
			else {
				pois.updatePurchaseOrderItem(purchaseOrderItem);
			}
		} catch (Exception e) {
			ServletActionContext.getRequest().setAttribute("message", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
		return this.purchaseOrderItemList();
	}
	
	/**
	 * 删除采购单条目信息
	 * @return
	 */
	public String deletePurchaseOrderItem() {
		//删除
		pois.deletePurchaseOrderItemById(this.purchaseOrderItem.getId());
		return this.purchaseOrderItemList();
	}
	
}
