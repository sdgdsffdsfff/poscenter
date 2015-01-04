package mmb.poscenter.action;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import mmb.poscenter.domain.Invoice;
import mmb.poscenter.domain.Product;
import mmb.poscenter.service.InvoiceService;

import com.opensymphony.xwork2.ActionSupport;

public class InvoiceAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private InvoiceService is = new InvoiceService();
	private Page<Product> productPage = new Page<Product>();
	private Page<Invoice> page = new Page<Invoice>();
	private Invoice invoice = new Invoice();
	private int productId;

	public Page<Product> getProductPage() {
		return productPage;
	}

	public void setProductPage(Page<Product> productPage) {
		this.productPage = productPage;
	}

	public Page<Invoice> getPage() {
		return page;
	}

	public void setPage(Page<Invoice> page) {
		this.page = page;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	/**
	 * 跳转至商品列表界面
	 * @return
	 */
	public String productList(){
		//获取查询参数
		HttpServletRequest request = ServletActionContext.getRequest();
		String barCode = request.getParameter("barCode");
		String productName = request.getParameter("productName");
		String goodsClassName = request.getParameter("goodsClassName");
		request.setAttribute("barCode", barCode);
		request.setAttribute("productName", productName);
		request.setAttribute("goodsClassName", goodsClassName);
		
		//分页获取盘点商品列表信息
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("barCode", barCode);
		param.put("productName", productName);
		param.put("goodsClassName", goodsClassName);
		this.productPage = is.getProductPage(this.productPage, param);
		return "productList";
	}

	/**
	 * 跳转至进销存列表界面
	 * @return
	 */
	public String invoiceList(){
		this.page = is.getInvoicePage(page, this.productId);
		return SUCCESS;
	}
	
}
