package mmb.poscenter.action;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.ReceiveOrder;
import mmb.poscenter.domain.ReceiveOrderItem;
import mmb.poscenter.service.ReceiveOrderItemService;
import mmb.poscenter.service.ReceiveOrderService;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class ReceiveOrderAction extends ActionSupport{
	
	private static final long serialVersionUID = 8337297751555514326L;
	private static Logger log = Logger.getLogger(ReceiveOrderAction.class);
    
	private ReceiveOrderService ros = new ReceiveOrderService();
	private ReceiveOrderItemService rois = new ReceiveOrderItemService(); 
	
	private List<ReceiveOrder> list; 
	private List<ReceiveOrderItem> itemlist; 
	private List<ReceiveOrderItem> recOrItem; 
	private Page<ReceiveOrder> page;
	private ReceiveOrder receiveOrder = new ReceiveOrder();
	
	public List<ReceiveOrder> getList() {
		return list;
	}

	public void setList(List<ReceiveOrder> list) {
		this.list = list;
	}

	public List<ReceiveOrderItem> getItemlist() {
		return itemlist;
	}

	public void setItemlist(List<ReceiveOrderItem> itemlist) {
		this.itemlist = itemlist;
	}

	public List<ReceiveOrderItem> getRecOrItem() {
		return recOrItem;
	}

	public void setRecOrItem(List<ReceiveOrderItem> recOrItem) {
		this.recOrItem = recOrItem;
	}

	public Page<ReceiveOrder> getPage() {
		return page;
	}

	public void setPage(Page<ReceiveOrder> page) {
		this.page = page;
	}

	public ReceiveOrder getReceiveOrder() {
		return receiveOrder;
	}

	public void setReceiveOrder(ReceiveOrder receiveOrder) {
		this.receiveOrder = receiveOrder;
	}

	/**
	 * 跳转至收货单列表界面
	 * @return
	 */
	public String getReceiveOrderList(){
		try {
			//获取查询参数
			HttpServletRequest request = ServletActionContext.getRequest();
			Map<String, Object> param = new HashMap<String, Object>();
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtils.isNotBlank(startTime)) {
				param.put("startTime", new Timestamp(sdf.parse(startTime).getTime()));
				request.setAttribute("startTime", startTime);
			}
			if(StringUtils.isNotBlank(endTime)) {
				param.put("endTime", new Timestamp(sdf.parse(endTime).getTime()+(24*3600000)));
				request.setAttribute("endTime", endTime);
			}
			param.put("receiveOrder", receiveOrder);
			
			//分页获取收货单列表信息
			if(this.page == null){
				this.page = new Page<ReceiveOrder>();
			}
			setPage(ros.getReceiveOrderList(page, param));
			setList(this.page.list);
		} catch (ParseException e) {
			log.error("跳转至收货单列表界面时出现异常：", e);
		}
		return SUCCESS;
	}
	
	public String acquireReceiveOrder(){
		setReceiveOrder(ros.getReceiveOrder(receiveOrder));
		setItemlist(ros.getReceiveOrderItem(receiveOrder));
		return "acquire";
	}
	
	/**
	 * 确认收货
	 * @return
	 */
	public String saveReceiveOrder(){
		try {
			rois.confirmReceive(receiveOrder, recOrItem);
			this.receiveOrder = new ReceiveOrder();
		} catch (Exception e) {
			ServletActionContext.getRequest().setAttribute("message", "确认收货时出现异常："+e);
			return ERROR;
		}
		return this.getReceiveOrderList();
	}
	
}
