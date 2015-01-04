package mmb.poscenter.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.SendOrder;
import mmb.poscenter.domain.SendOrderItem;
import mmb.poscenter.service.SendOrderItemService;
import mmb.poscenter.service.SendOrderService;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class SendOrderItemAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private SendOrderItemService sois = new SendOrderItemService();
	private Page<SendOrderItem> page = new Page<SendOrderItem>();
	private SendOrder sendOrder = new SendOrder();
	private SendOrderItem sendOrderItem = new SendOrderItem();
	private int sendOrderId;
	
	public Page<SendOrderItem> getPage() {
		return page;
	}

	public void setPage(Page<SendOrderItem> page) {
		this.page = page;
	}

	public SendOrder getSendOrder() {
		return sendOrder;
	}

	public void setSendOrder(SendOrder sendOrder) {
		this.sendOrder = sendOrder;
	}

	public SendOrderItem getSendOrderItem() {
		return sendOrderItem;
	}

	public void setSendOrderItem(SendOrderItem sendOrderItem) {
		this.sendOrderItem = sendOrderItem;
	}

	public int getSendOrderId() {
		return sendOrderId;
	}

	public void setSendOrderId(int sendOrderId) {
		this.sendOrderId = sendOrderId;
	}

	/**
	 * 跳转至发货单条目列表界面
	 * @return
	 */
	public String sendOrderItemList(){
		//获取发货单信息
		SendOrderService sos = new SendOrderService();
		this.sendOrder = sos.getSendOrderById(this.sendOrderId);
		
		//获取查询参数
		HttpServletRequest request = ServletActionContext.getRequest();
		String productName = request.getParameter("productName");
		request.setAttribute("productName", productName);
		
		//分页获取发货单条目列表信息
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("productName", productName);
		param.put("sendOrderId", sendOrderId);
		sois.getSendOrderItemPage(page, param);
		return SUCCESS;
	}

	/**
	 * 跳转到发货单条目表单页面
	 * @return
	 */
	public String toSendOrderItemFormView() {
		//获取发货单信息
		SendOrderService sos = new SendOrderService();
		this.sendOrder = sos.getSendOrderById(this.sendOrderId);
		
		//修改
		if(sendOrderItem.getId() != 0) {
			this.sendOrderItem = sois.getDetailById(this.sendOrderItem.getId());
		}
		return INPUT;
	}
	
	/**
	 * 保存发货单条目信息
	 * @return
	 */
	public String saveSendOrderItem() {
		try {
			//新建
			if(sendOrderItem.getId() == 0) {
				sois.addXXX(sendOrderItem, "send_order_item");
			}
			//修改
			else {
				sois.updateSendOrderItem(sendOrderItem);
			}
		} catch (Exception e) {
			ServletActionContext.getRequest().setAttribute("message", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
		return this.sendOrderItemList();
	}
	
	/**
	 * 删除发货单条目信息
	 * @return
	 */
	public String deleteSendOrderItem() {
		//删除
		sois.deleteSendOrderItemById(this.sendOrderItem.getId());
		return this.sendOrderItemList();
	}
	
}
