package mmb.poscenter.action;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.SendOrder;
import mmb.poscenter.service.SendOrderService;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class SendOrderAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(SendOrderAction.class);
	
	private SendOrderService sos = new SendOrderService();
	private Page<SendOrder> page = new Page<SendOrder>();
	private SendOrder sendOrder = new SendOrder();

	public Page<SendOrder> getPage() {
		return page;
	}

	public void setPage(Page<SendOrder> page) {
		this.page = page;
	}

	public SendOrder getSendOrder() {
		return sendOrder;
	}

	public void setSendOrder(SendOrder sendOrder) {
		this.sendOrder = sendOrder;
	}

	/**
	 * 跳转至发货单列表界面
	 * @return
	 */
	public String sendOrderList(){
		try {
			//获取查询参数
			HttpServletRequest request = ServletActionContext.getRequest();
			Map<String, Object> param = new HashMap<String, Object>();
			String shopName = request.getParameter("shopName");
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
			param.put("shopName", shopName);
			request.setAttribute("shopName", shopName);
			param.put("sendOrder", sendOrder);
			
			//分页获取发货单列表信息
			sos.getSendOrderPage(page, param);
		} catch (ParseException e) {
			log.error("跳转至采购单列表界面时出现异常：", e);
		}
		return SUCCESS;
	}

	/**
	 * 跳转到发货单表单页面
	 * @return
	 */
	public String toSendOrderFormView() {
		//修改
		if(sendOrder.getId() != 0) {
			this.sendOrder = sos.getDetailById(this.sendOrder.getId());
		}else{
			long tmp = new Date().getTime();
			this.sendOrder.setOrderNumber("FH"+String.valueOf(tmp).substring(2, 10));
		}
		return INPUT;
	}
	
	/**
	 * 保存发货单信息
	 * @return
	 */
	public String saveSendOrder() {
		try {
			//新建
			if(sendOrder.getId() == 0) {
				sendOrder.setCreateTime(new Timestamp(new Date().getTime()));
				sos.addXXX(sendOrder, "send_order");
			}
			//修改
			else {
				sos.updateSendOrder(sendOrder);
			}
		} catch (Exception e) {
			ServletActionContext.getRequest().setAttribute("message", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
		this.sendOrder = new SendOrder();
		return this.sendOrderList();
	}
	
	/**
	 * 删除发货单信息
	 * @return
	 */
	public String deleteSendOrder() {
		//删除
		sos.deleteSendOrderById(this.sendOrder.getId());
		return this.sendOrderList();
	}
	
	/**
	 * 提交发货单
	 * @return
	 */
	public String submitSendOrder() {
		try {
			sos.submitSendOrder(this.sendOrder.getId());
		} catch (Exception e) {
			ServletActionContext.getRequest().setAttribute("message", "提交发货单时出现异常："+e);
			return ERROR;
		}
		return this.sendOrderList();
	}
	
}
