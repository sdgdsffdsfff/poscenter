package mmb.poscenter.action;

import java.sql.Timestamp;
import java.util.Date;

import mmb.poscenter.domain.Shop;
import mmb.poscenter.service.ShopService;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class ShopAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private ShopService ss = new ShopService();
	private Page<Shop> page = new Page<Shop>();
	private Shop shop = new Shop();
	
	public Page<Shop> getPage() {
		return page;
	}

	public void setPage(Page<Shop> page) {
		this.page = page;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}
	
	/**
	 * 跳转至店面列表界面
	 * @return
	 */
	public String shopList(){
		this.page = ss.getShopPage(page);
		return SUCCESS;
	}
	
	/**
	 * 跳转到选择店面列表页面（供弹出层调用）
	 * @return
	 */
	public String toSelectShopListView(){
		this.page = ss.getShopPage(page);
		return "selectShopList";
	}

	/**
	 * 跳转到店面表单页面
	 * @return
	 */
	public String toShopFormView() {
		//修改
		if(this.shop.getId() != 0) {
			this.shop = ss.getShopById(this.shop.getId());
		}
		return INPUT;
	}
	
	/**
	 * 跳转到店面详情页面
	 * @return
	 */
	public String toShopDetailView() {
		//获取店面信息
		this.shop = ss.getShopById(this.shop.getId());
		return "detail";
	}
	
	/**
	 * 保存店面信息
	 * @return
	 */
	public String saveShop() {
		try {
			//新建
			if(this.shop.getId() == 0) {
				this.shop.setCreateTime(new Timestamp(new Date().getTime()));
				ss.addXXX(shop, "shop");
			}
			//修改
			else {
				ss.updateShop(shop);
			}
		} catch (Exception e) {
			ServletActionContext.getRequest().setAttribute("message", e.getMessage());
			e.printStackTrace();
			return ERROR;
		}
		return this.shopList();
	}
	
	/**
	 * 删除店面信息
	 * @return
	 */
	public String deleteShop() {
		//删除
		ss.deleteShopById(this.shop.getId());
		return this.shopList();
	}
	
}
