package mmb.poscenter.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.Product;
import mmb.poscenter.domain.ShopInvoice;
import mmb.poscenter.service.ProductService;
import mmb.poscenter.service.ShopInvoiceService;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class ShopInvoiceAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private ShopInvoiceService sis = new ShopInvoiceService();
	private Page<Product> productPage = new Page<Product>();
	private Page<ShopInvoice> page = new Page<ShopInvoice>();
	private int productId;
	
	public Page<Product> getProductPage() {
		return productPage;
	}

	public void setProductPage(Page<Product> productPage) {
		this.productPage = productPage;
	}

	public Page<ShopInvoice> getPage() {
		return page;
	}

	public void setPage(Page<ShopInvoice> page) {
		this.page = page;
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
		
		//分页获取进销存商品列表信息
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("barCode", barCode);
		param.put("productName", productName);
		param.put("goodsClassName", goodsClassName);
		this.productPage = sis.getProductPage(this.productPage, param);
		return "productList";
	}

	/**
	 * 跳转至店面进销存列表界面
	 * @return
	 */
	public String shopInvoiceList(){
		//获取商品信息
		HttpServletRequest request = ServletActionContext.getRequest();
		ProductService ps = new ProductService();
		Product product = ps.getProductById(productId);
		request.setAttribute("product", product);
		
		//获取查询参数
		String shopCode = request.getParameter("shopCode");
		String shopName = request.getParameter("shopName");
		request.setAttribute("shopCode", shopCode);
		request.setAttribute("shopName", shopName);
		
		//分页获取店面进销存列表信息
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("productId", productId);
		param.put("shopCode", shopCode);
		param.put("shopName", shopName);
		this.page = sis.getInvoicePage(page, param);
		return SUCCESS;
	}
	
}
