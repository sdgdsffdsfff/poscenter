package mmb.poscenter.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.CheckStock;
import mmb.poscenter.domain.CheckStockItem;
import mmb.poscenter.service.CheckStockItemService;
import mmb.poscenter.service.CheckStockService;
import mmb.poscenter.util.ResponseUtils;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class CheckStockItemAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private CheckStockItemService csis = new CheckStockItemService();
	private Page<CheckStockItem> page = new Page<CheckStockItem>();
	private CheckStock checkStock = new CheckStock();
	private CheckStockItem checkStockItem = new CheckStockItem();
	private int checkStockId;
	private List<CheckStockItem> checkStockItemList = new ArrayList<CheckStockItem>();
	
	public Page<CheckStockItem> getPage() {
		return page;
	}

	public void setPage(Page<CheckStockItem> page) {
		this.page = page;
	}
	
	public CheckStock getCheckStock() {
		return checkStock;
	}

	public void setCheckStock(CheckStock checkStock) {
		this.checkStock = checkStock;
	}

	public CheckStockItem getCheckStockItem() {
		return checkStockItem;
	}

	public void setCheckStockItem(CheckStockItem checkStockItem) {
		this.checkStockItem = checkStockItem;
	}

	public int getCheckStockId() {
		return checkStockId;
	}

	public void setCheckStockId(int checkStockId) {
		this.checkStockId = checkStockId;
	}
	
	public List<CheckStockItem> getCheckStockItemList() {
		return checkStockItemList;
	}

	public void setCheckStockItemList(List<CheckStockItem> checkStockItemList) {
		this.checkStockItemList = checkStockItemList;
	}

	/**
	 * 跳转至盘点列表界面
	 * @return
	 */
	public String checkStockItemList(){
		HttpServletRequest request = ServletActionContext.getRequest();
		
		//获取盘点信息
		CheckStockService css = new CheckStockService();
		this.checkStock = css.getCheckStockById(checkStockId);
		
		//查询参数
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("checkStockId", checkStockId);
		this.page = csis.getCheckStockItemPage(this.page, param);
		
		//按商品进行分组
		if(this.page!=null && this.page.getList()!=null && !this.page.getList().isEmpty()) {
			List<List<CheckStockItem>> checkStockItemListList = new ArrayList<List<CheckStockItem>>();
			int nowProductId = 0;
			List<CheckStockItem> CheckStockItemList = new ArrayList<CheckStockItem>();
			for(CheckStockItem cs : this.page.getList()) {
				//新商品
				if(nowProductId != cs.getProductId()) {
					if(nowProductId != 0) {
						checkStockItemListList.add(CheckStockItemList);
					}
					CheckStockItemList = new ArrayList<CheckStockItem>();
					CheckStockItemList.add(cs);
				} else {
					CheckStockItemList.add(cs);
				}
				nowProductId = cs.getProductId();
			}
			//最后一次
			checkStockItemListList.add(CheckStockItemList);
			
			request.setAttribute("checkStockItemListList", checkStockItemListList);
		}
		
		//盘点是否可以提交本次盘点任务
		if(checkStock.getUseStatus() == 0) {
			boolean canSubmit = csis.canSubmitCheckStock(checkStockId);
			request.setAttribute("canSubmit", canSubmit);
		}
		
		return SUCCESS;
	}
	
	/**
	 * 保存初盘信息
	 * @return
	 */
	public void saveFirstCheck(){
		//保存初盘信息
		boolean success = csis.updateCheckStockItem(checkStockItem);
		ResponseUtils.renderText(ServletActionContext.getResponse(), success+"");
	}
	
	
	/**
	 * 保存复盘信息
	 * @return
	 */
	public void saveAgainCheck(){
		//日期
		checkStockItem.setCheckTime(new Timestamp(new Date().getTime()));
		boolean success = csis.addXXX(checkStockItem, "check_stock_item");
		ResponseUtils.renderText(ServletActionContext.getResponse(), success+"");
	}
	
	/**
	 * 批量保存盘点信息
	 * @return
	 */
	public void saveBatchCheck(){
		boolean success = csis.saveBatchCheck(this.checkStockItemList);
		ResponseUtils.renderText(ServletActionContext.getResponse(), success+"");
	}
	
	/**
	 * 跳转到打印盘点表页面
	 * @return
	 */
	public String toPrintCheckStockItemView(){
		HttpServletRequest request = ServletActionContext.getRequest();
		
		//获取盘点信息
		CheckStockService css = new CheckStockService();
		this.checkStock = css.getCheckStockById(checkStockId);
		
		//获取盘点条目数据
		List<Object[]> checkStockItemList = csis.getPrintCheckStockItemData(this.checkStockId);
		request.setAttribute("checkStockItemList", checkStockItemList);
		
		return "printCheckStockView";
	}
	
	/**
	 * 更新商品库存信息
	 */
	public void updateProductStock() {
		//获取请求信息
		HttpServletRequest request = ServletActionContext.getRequest();
		int productId = Integer.parseInt(request.getParameter("productId"));
		String date = request.getParameter("date");
		boolean success = csis.updateProductStock(productId, date);
		ResponseUtils.renderText(ServletActionContext.getResponse(), success+"");
	}
	
	/**
	 * 提交盘点任务
	 */
	public String submitCheckStock() {
		//获取请求信息
		csis.submitCheckStock(this.checkStock);
		return this.checkStockItemList();
	}

}
