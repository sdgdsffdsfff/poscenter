package mmb.poscenter.action;

import java.util.List;

import org.apache.struts2.ServletActionContext;

import mmb.poscenter.domain.Supplier;
import mmb.poscenter.service.SupplierService;
import mmb.poscenter.util.ResponseUtils;

import com.opensymphony.xwork2.ActionSupport;
       
public class SupplierAction extends ActionSupport {
	
	private static final long serialVersionUID = 5173235340847756804L;

	private SupplierService ss = new SupplierService();
	private List<Supplier> list;
	private Page<Supplier> page;
	private Supplier supplier = new Supplier();

	public List<Supplier> getList() {
		return list;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public void setList(List<Supplier> list) {
		this.list = list;
	}

	public Page<Supplier> getPage() {
		return page;
	}

	public void setPage(Page<Supplier> page) {
		this.page = page;
	}
	
	/**
	 * 跳转至供应商列表界面
	 * @return
	 */
	public String supplierList(){
		if(this.page == null){
			this.page = new Page<Supplier>();
		}
		setPage(ss.getSupplierList(page));
		setList(this.page.list);
		return SUCCESS;
	}

	/**
	 * 跳转至供应商列表选择界面
	 * @return
	 */
	public String supplierChooseList(){
		if(this.page == null){
			this.page = new Page<Supplier>();
		}
		setPage(ss.getSupplierList(page));
		setList(this.page.list);
		return "choose";
	}
	/**
	 * 跳转到供应商表单页面
	 * @return
	 */
	public String toSupplierFormView() {
		//修改
		if(supplier.getId() != 0) {
			this.supplier = ss.getSupplierById(supplier.getId());
		}
		return INPUT;
	}
	
	/**
	 * 跳转到供应商详情页面
	 * @return
	 */
	public String toSupplierDetailView() {
		//获取商品信息
		this.supplier = ss.getSupplierById(supplier.getId());
		return "detail";
	}
	
	/**
	 * 保存供应商信息
	 * @return
	 */
	public String saveSupplier() {
		//新建
		if(supplier.getId() == 0) {
			ss.addXXX(supplier, "supplier");
		}
		//修改
		else {
			ss.updateSupplier(supplier);
		}
		return this.supplierList();
	}
	
	/**
	 * 删除供应商信息
	 */
	public void deleteSupplier() {
		String result = "success";
		try {
			//判断该供应商是否被商品信息关联
			boolean hasUsed = ss.hasUsed(this.supplier.getId());
			if(hasUsed) {
				result = "该供应商已被商品信息关联！请解除关联后再进行删除操作！";
			} else {
				ss.deleteSupplierById(this.supplier.getId());
			}
		} catch (Exception e) {
			result = "删除供应商信息时出现异常："+e.toString();
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), ""+result);
	}
}
