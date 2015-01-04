package mmb.poscenter.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mmb.poscenter.domain.PurchaseOrder;
import mmb.poscenter.service.PurchaseOrderService;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class PurchaseOrderAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(PurchaseOrderAction.class);
	
	private PurchaseOrderService pos = new PurchaseOrderService();
	private Page<PurchaseOrder> page = new Page<PurchaseOrder>();
	private PurchaseOrder purchaseOrder = new PurchaseOrder();
	
	//导入采购单
	private File excel;
	private String excelFileName;
	
	public Page<PurchaseOrder> getPage() {
		return page;
	}

	public void setPage(Page<PurchaseOrder> page) {
		this.page = page;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	
	public File getExcel() {
		return excel;
	}

	public void setExcel(File excel) {
		this.excel = excel;
	}

	public String getExcelFileName() {
		return excelFileName;
	}

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}

	/**
	 * 跳转至采购单列表界面
	 * @return
	 */
	public String purchaseOrderList(){
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
			param.put("purchaseOrder", purchaseOrder);
			
			//分页获取采购单列表信息
			pos.getPurchaseOrderPage(page, param);
		} catch (ParseException e) {
			log.error("跳转至采购单列表界面时出现异常：", e);
		}
		return SUCCESS;
	}

	/**
	 * 跳转到采购单表单页面
	 * @return
	 */
	public String toPurchaseOrderFormView() {
		//修改
		if(purchaseOrder.getId() != 0) {
			this.purchaseOrder = pos.getPurchaseOrderById(this.purchaseOrder.getId());
		}else{
			long tmp = new Date().getTime();
			this.purchaseOrder.setOrderNumber("DD"+String.valueOf(tmp).substring(2, 10));
		}
		return INPUT;
	}
	
	/**
	 * 保存采购单信息
	 * @return
	 */
	public String savePurchaseOrder() {
		try {
			//新建
			if(purchaseOrder.getId() == 0) {
				purchaseOrder.setCreateTime(new Timestamp(new Date().getTime()));
				pos.addXXX(purchaseOrder, "purchase_order");
			}
			//修改
			else {
				pos.updatePurchaseOrder(purchaseOrder);
			}
		} catch (Exception e) {
			ServletActionContext.getRequest().setAttribute("message", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
		purchaseOrder = new PurchaseOrder();
		return this.purchaseOrderList();
	}
	
	/**
	 * 删除采购单信息
	 * @return
	 */
	public String deletePurchaseOrder() {
		//删除
		pos.deletePurchaseOrderById(this.purchaseOrder.getId());
		return this.purchaseOrderList();
	}
	
	/**
	 * 提交采购单
	 * @return
	 */
	public String submitPurchaseOrder() {
		pos.submitPurchaseOrder(this.purchaseOrder.getId());
		return this.purchaseOrderList();
	}
	
	/**
	 * 导入采购单
	 * @return
	 */
	public String importPurchaseOrder() {
		boolean success = false;
		HttpServletRequest request = ServletActionContext.getRequest();
		
		//文件为空
		if(StringUtils.isBlank(excelFileName)) {
			request.setAttribute("message", "采购单文件为空！");
		}
		//非excel文件
		else if(!excelFileName.toUpperCase().endsWith(".XLS")) {
			request.setAttribute("message", "采购单文件必须为Excel文件！");
		} else {
			try {
				pos.importPurchaseOrder(excel);
				success = true;
			} catch (Exception e) {
				request.setAttribute("message", e);
			}
		}
		
		if(success) {
			return this.purchaseOrderList();
		} else {
			return ERROR;
		}
	}
	
	/**
	 * 导出Excel采购单
	 * @return
	 */
	public void exportExcel() {
		try {
			//获取采购单信息
			PurchaseOrder order = pos.getPurchaseOrderById(this.purchaseOrder.getId());
			
			//获取Excel数据
			byte[] data = pos.exportExcel(order);
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename="+ order.getOrderNumber() + ".xls");
			OutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(data);
			out.flush();
			out.close();
		} catch (Exception e) {
			log.error("导出Excel采购单时出现异常：", e);
		}
	}
	
}
