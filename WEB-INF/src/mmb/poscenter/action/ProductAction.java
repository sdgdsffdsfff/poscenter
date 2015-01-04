package mmb.poscenter.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mmb.poscenter.domain.Product;
import mmb.poscenter.service.ProductService;
import mmb.poscenter.util.BarcodeUtil;
import mmb.poscenter.util.ResponseUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class ProductAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ProductAction.class);
	private HttpServletRequest request = ServletActionContext.getRequest();

	private ProductService ps = new ProductService();
	private List<Product> list;
	private Page<Product> page = new Page<Product>();
	private Product product = new Product();
	
	private String productName;
	private int supplierId;
	private String supplierName;
	private String goodsClassId;
	private String goodsClassName;
	
	//从Excel导入商品
	private File excel;
	private String excelFileName;
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getGoodsClassId() {
		return goodsClassId;
	}

	public void setGoodsClassId(String goodsClassId) {
		this.goodsClassId = goodsClassId;
	}

	public String getGoodsClassName() {
		return goodsClassName;
	}

	public void setGoodsClassName(String goodsClassName) {
		this.goodsClassName = goodsClassName;
	}

	public Page<Product> getPage() {
		return page;
	}

	public void setPage(Page<Product> page) {
		this.page = page;
	}

	public List<Product> getList() {
		return list;
	}

	public void setList(List<Product> list) {
		this.list = list;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Product getProduct() {
		return product;
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
	 * 跳转至商品列表界面
	 * @return
	 */
	public String productList(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("name", this.productName);
		param.put("supplierId", this.supplierId);
		param.put("goodsClassId", this.goodsClassId);
		param.put("supplierName", this.supplierName);
		param.put("goodsClassName", this.goodsClassName);
		setPage(ps.getProductList(page, param));
		setList(this.page.list);
		return SUCCESS;
	}
	
	/**
	 * 跳转到选择商品列表页面（供弹出层调用）
	 * @return
	 */
	public String toSelectProductListView(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("name", this.product.getName());
		setPage(ps.getProductList(page, param));
		return "selectProductList";
	}

	/**
	 * 跳转到商品表单页面
	 * @return
	 * @throws Exception 
	 */
	public String toProductFormView() throws Exception {
		//修改
		if(product.getId() != 0) {
			this.product = ps.getProductDetailById(product.getId());
		}else{
			//验证条形码是否已被占用
			Product p = new Product();
			while(p != null) {
				String barcode = BarcodeUtil.createEAN13Barcode(String.valueOf(System.currentTimeMillis()).substring(0, 12));
				p = ps.getProductByBarcode(barcode);
				this.product.setBarCode(barcode);
			}
		}
		return INPUT;
	}
	
	/**
	 * 跳转到商品详情页面
	 * @return
	 */
	public String toProductDetailView() {
		//获取商品信息
		this.product = ps.getProductDetailById(product.getId());
		return "detail";
	}
	
	/**
	 * 保存商品信息
	 * @return
	 */
	public String saveProduct() {
		try {
			//新建
			if(product.getId() == 0) {
				ps.addXXX(product, "product");
			}
			//修改
			else {
				ps.updateProduct(product);
			}
		} catch (Exception e) {
			request.setAttribute("message", "保存商品信息时出现异常："+e);
			return ERROR;
		}
		return this.productList();
	}
	
	/**
	 * 删除商品信息
	 * @return
	 */
	public String deleteProduct() {
		//删除
		ps.deleteProductById(this.product.getId());
		return this.productList();
	}
	
	/**
	 * 跳转到查看商品条形码页面
	 * @return
	 */
	public String toShowBarcodeView() {
		try {
			//获取条形码图片路径
			String barcodeImgPath = ps.getBarcodeImgPath(this.product.getId());
			request.setAttribute("barcodeImgPath", barcodeImgPath);
			request.setAttribute("barCode", this.product.getBarCode());
			String name = new String(this.product.getName().getBytes("ISO-8859-1"),"UTF-8");
			request.setAttribute("name", name);
		} catch (Exception e) {
			request.setAttribute("message", e.getMessage());
			return ERROR;
		}
		return "showBarcode";
	}
	
	/**
	 * 获取商品的采购参考价
	 */
	public void getPurchaseReferPrice() {
		//获取商品信息
		this.product = ps.getProductById(this.product.getId());
		ResponseUtils.renderText(ServletActionContext.getResponse(), ""+this.product.getPurchaseReferPrice());
	}
	
	/**
	 * 导入Excel商品
	 * @return
	 */
	public String importProduct() {
		boolean success = false;
		
		//文件为空
		if(StringUtils.isBlank(excelFileName)) {
			request.setAttribute("message", "商品文件为空！");
		}
		//非excel文件
		else if(!excelFileName.toUpperCase().endsWith(".XLS")) {
			request.setAttribute("message", "商品导入文件必须为Excel文件！");
		} else {
			try {
				ps.importProduct(excel);
				success = true;
			} catch (Exception e) {
				request.setAttribute("message", e);
			}
		}
		
		if(success) {
			return this.productList();
		} else {
			return ERROR;
		}
	}
	
	/**
	 * 导出Excel商品列表
	 * @return
	 */
	public void exportExcel() {
		try {
			//获取Excel数据
			byte[] data = ps.exportExcel();
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=productList.xls");
			OutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(data);
			out.flush();
			out.close();
		} catch (Exception e) {
			log.error("导出Excel商品列表时出现异常：", e);
		}
	}
	
	/**
	 * 验证条形码是否可用
	 */
	public void validateBarcode() {
		String result = ps.validateBarcode(this.product.getBarCode());
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
}
