package mmb.poscenter.action;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.RechargeOrder;
import mmb.poscenter.service.RechargeOrderService;
import mmb.poscenter.util.ResponseUtils;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class RechargeOrderAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(RechargeOrderAction.class);
	private HttpServletRequest request = ServletActionContext.getRequest();

	private RechargeOrderService ros = new RechargeOrderService();
	private Page<RechargeOrder> page = new Page<RechargeOrder>();
	private RechargeOrder rechargeOrder = new RechargeOrder();
	
	private String priceCardId; //调价卡号
	private int priceCardType; //调价卡类型
	private int auditStatus; //审核状态
	private int useStatus; //使用状态
	
	public Page<RechargeOrder> getPage() {
		return page;
	}

	public void setPage(Page<RechargeOrder> page) {
		this.page = page;
	}

	public RechargeOrder getRechargeOrder() {
		return rechargeOrder;
	}

	public void setRechargeOrder(RechargeOrder rechargeOrder) {
		this.rechargeOrder = rechargeOrder;
	}

	public String getPriceCardId() {
		return priceCardId;
	}

	public void setPriceCardId(String priceCardId) {
		this.priceCardId = priceCardId;
	}

	public int getPriceCardType() {
		return priceCardType;
	}

	public void setPriceCardType(int priceCardType) {
		this.priceCardType = priceCardType;
	}

	public int getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}

	public int getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(int useStatus) {
		this.useStatus = useStatus;
	}
	
	
	/**
	 * 跳转至充值单列表界面
	 * @return
	 */
	public String rechargeOrderList(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("priceCardId", this.priceCardId);
		param.put("priceCardType", this.priceCardType);
		param.put("auditStatus", this.auditStatus);
		param.put("useStatus", this.useStatus);
		ros.getRechargeOrderPage(page, param);
		return SUCCESS;
	}
	
	/**
	 * 跳转至审核充值单列表界面
	 * @return
	 */
	public String auditRechargeOrderList(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("priceCardId", this.priceCardId);
		param.put("priceCardType", this.priceCardType);
		param.put("auditStatus", 1);
		param.put("useStatus", 2);
		ros.getRechargeOrderPage(page, param);
		return "auditList";
	}
	
	/**
	 * 跳转到充值单表单页面
	 * @return
	 */
	public String toRechargeOrderFormView() {
		//修改
		if(rechargeOrder.getId() != 0) {
			this.rechargeOrder = ros.getDetailById(rechargeOrder.getId());
		}
		return INPUT;
	}
	
	/**
	 * 保存充值单信息
	 * @return
	 */
	public void saveRechargeOrder() {
		String result = "success";
		try {
			//新建
			if(rechargeOrder.getId() == 0) {
				rechargeOrder.setCreateTime(new Timestamp(new Date().getTime()));
				ros.addXXX(rechargeOrder, "recharge_order");
			}
			//修改
			else {
				ros.updateRechargeOrder(rechargeOrder);
			}
		} catch (Exception e) {
			log.error("保存充值单信息时出现异常：", e);
			result = "保存充值单信息时出现异常："+e;
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
	/**
	 * 删除充值单信息
	 * @return
	 */
	public String deleteRechargeOrder() {
		ros.deleteById(this.rechargeOrder.getId());
		return this.rechargeOrderList();
	}
	
	/**
	 * 审核充值单
	 * @return
	 */
	public String auditRechargeOrder() {
		try {
			ros.auditRechargeOrder(this.rechargeOrder.getId(), this.rechargeOrder.getAuditStatus());
		} catch (Exception e) {
			request.setAttribute("message", "审核充值单时出现异常："+e);
			return ERROR;
		}
		return this.auditRechargeOrderList();
	}
	
}
