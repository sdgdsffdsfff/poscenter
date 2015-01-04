package mmb.poscenter.action;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mmb.poscenter.domain.ShopReturnOrder;
import mmb.poscenter.domain.ShopReturnOrderItem;
import mmb.poscenter.service.ShopReturnOrderService;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class ShopReturnOrderAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ShopReturnOrderAction.class);
	
	private ShopReturnOrderService sros = new ShopReturnOrderService();
	private Page<ShopReturnOrder> page = new Page<ShopReturnOrder>();
	private ShopReturnOrder shopReturnOrder = new ShopReturnOrder();
	private List<ShopReturnOrderItem> itemList;

	public Page<ShopReturnOrder> getPage() {
		return page;
	}

	public void setPage(Page<ShopReturnOrder> page) {
		this.page = page;
	}

	public ShopReturnOrder getShopReturnOrder() {
		return shopReturnOrder;
	}

	public void setShopReturnOrder(ShopReturnOrder shopReturnOrder) {
		this.shopReturnOrder = shopReturnOrder;
	}

	public List<ShopReturnOrderItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<ShopReturnOrderItem> itemList) {
		this.itemList = itemList;
	}

	/**
	 * 跳转至店面退货单列表界面
	 * @return
	 */
	public String shopReturnOrderList(){
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
			request.setAttribute("shopName", shopName);
			param.put("shopName", shopName);
			param.put("shopReturnOrder", shopReturnOrder);
			
			//分页获取店面退货单列表信息
			sros.getShopReturnOrderPage(page, param);
		} catch (ParseException e) {
			log.error("跳转至店面退货单列表界面时出现异常：", e);
		}
		return SUCCESS;
	}

	/**
	 * 确认退货
	 * @return
	 */
	public String confirmReturn() {
		try {
			sros.confirmReturn(this.shopReturnOrder, this.itemList);
		} catch (Exception e) {
			ServletActionContext.getRequest().setAttribute("message", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
		this.shopReturnOrder = new ShopReturnOrder();
		return this.shopReturnOrderList();
	}
	
	/**
	 * 导出Excel店面退货单
	 * @return
	 */
	public void exportExcel() {
		try {
			//获取店面退货单信息
			ShopReturnOrder order = sros.getDetailById(this.shopReturnOrder.getId());
			
			//获取Excel数据
			byte[] data = sros.exportExcel(order);
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename="+ order.getOrderNumber() + ".xls");
			OutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(data);
			out.flush();
			out.close();
		} catch (Exception e) {
			log.error("导出Excel店面退货单时出现异常：", e);
		}
	}
	
}
