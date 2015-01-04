package mmb.poscenter.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.CheckStock;
import mmb.poscenter.service.CheckStockService;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class CheckStockAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private CheckStockService css = new CheckStockService();
	private Page<CheckStock> page = new Page<CheckStock>();
	private CheckStock checkStock = new CheckStock();
	
	public Page<CheckStock> getPage() {
		return page;
	}

	public void setPage(Page<CheckStock> page) {
		this.page = page;
	}

	public CheckStock getCheckStock() {
		return checkStock;
	}

	public void setCheckStock(CheckStock checkStock) {
		this.checkStock = checkStock;
	}

	/**
	 * 跳转至盘点列表界面
	 * @return
	 */
	public String checkStockList(){
		HttpServletRequest request = ServletActionContext.getRequest();
		
		//查询日期
		String date = request.getParameter("date");
		
		//查询参数
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("date", date);
		this.page = css.getCheckStockPage(this.page, param);
		request.setAttribute("date", date);
		
		return SUCCESS;
	}
	
	/**
	 * 生成空的商品盘点表
	 * @return 返回到列表页面
	 */
	public String createEmptyCheckStock(){
		css.createEmptyCheckStock();
		return this.checkStockList();
	}
	
}
