package mmb.poscenter.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.ReceiveOrderItem;
import mmb.poscenter.service.ReceiveOrderItemService;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class ReceiveOrderItemAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private ReceiveOrderItemService rois = new ReceiveOrderItemService();
	private Page<ReceiveOrderItem> page = new Page<ReceiveOrderItem>();
	private ReceiveOrderItem roi = new ReceiveOrderItem();
	
	public Page<ReceiveOrderItem> getPage() {
		return page;
	}

	public void setPage(Page<ReceiveOrderItem> page) {
		this.page = page;
	}

	public ReceiveOrderItem getRoi() {
		return roi;
	}

	public void setRoi(ReceiveOrderItem roi) {
		this.roi = roi;
	}

	public String getReceiveOrderItemList() {
		//获取查询参数
		HttpServletRequest request = ServletActionContext.getRequest();
		String barCode = request.getParameter("barCode");
		String productName = request.getParameter("productName");
		String supplierName = request.getParameter("supplierName");
		request.setAttribute("barCode", barCode);
		request.setAttribute("productName", productName);
		request.setAttribute("supplierName", supplierName);
		
		//分页获取采购单条目列表信息
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("receiveOrderId", this.roi.getOrderId());
		param.put("barCode", barCode);
		param.put("productName", productName);
		param.put("supplierName", supplierName);
		this.page = rois.getReceiveOrderItemPage(page, param);
		return SUCCESS;
	}

}
