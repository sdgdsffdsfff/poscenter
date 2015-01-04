package mmb.poscenter.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.AdjustPriceBill;
import mmb.poscenter.service.AdjustPriceBillService;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class AdjustPriceBillAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(AdjustPriceBillAction.class);
	private HttpServletRequest request = ServletActionContext.getRequest();

	private AdjustPriceBillService apbs = new AdjustPriceBillService();
	private Page<AdjustPriceBill> page = new Page<AdjustPriceBill>();
	private AdjustPriceBill adjustPriceBill = new AdjustPriceBill();
	
	private String shopCode; //店面编号
	private String shopName; //店面名称
	private String billNumber; //调价单号
	private String productName; //商品名称
	private int auditStatus; //审核状态
	
	public Page<AdjustPriceBill> getPage() {
		return page;
	}

	public void setPage(Page<AdjustPriceBill> page) {
		this.page = page;
	}

	public AdjustPriceBill getAdjustPriceBill() {
		return adjustPriceBill;
	}

	public void setAdjustPriceBill(AdjustPriceBill adjustPriceBill) {
		this.adjustPriceBill = adjustPriceBill;
	}
	
	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}

	/**
	 * 跳转至调价单列表界面
	 * @return
	 */
	public String adjustPriceBillList(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopCode", this.shopCode);
		param.put("shopName", this.shopName);
		param.put("billNumber", this.billNumber);
		param.put("productName", this.productName);
		param.put("auditStatus", this.auditStatus);
		apbs.getAdjustPriceBillPage(page, param);
		return SUCCESS;
	}
	
	/**
	 * 审核充值单
	 * @return
	 */
	public String auditAdjustPriceBill() {
		try {
			apbs.auditAdjustPriceBill(this.adjustPriceBill);
		} catch (Exception e) {
			request.setAttribute("message", "审核充值单时出现异常："+e);
			return ERROR;
		}
		return this.adjustPriceBillList();
	}
	
}
