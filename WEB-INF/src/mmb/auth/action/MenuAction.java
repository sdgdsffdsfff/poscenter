package mmb.auth.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import mmb.auth.domain.Menu;
import mmb.auth.domain.User;
import mmb.auth.service.MenuService;
import mmb.framework.IConstants;
import mmb.poscenter.util.ResponseUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class MenuAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private MenuService ms = new MenuService();
	private Menu menu = new Menu();
	private String id;
	
	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 跳转到菜单表单页面
	 * @return
	 */
	public String toMenuFormView(){
		//新增
		if(StringUtils.isBlank(id)) {
			//获取父节点信息，若父节点id为空则增加一级节点
			String parentId = request.getParameter("parentId");
			if(StringUtils.isNotBlank(parentId)) {
				Menu parentMenu = ms.getMenuById(parentId);
				menu.setParentId(parentId);
				menu.setParent(parentMenu);
			}
			
			//设置层级
			menu.setTreeLevel(Integer.parseInt(request.getParameter("treeLevel")));
		}
		else {
			menu = ms.getMenuById(id);
			if(StringUtils.isNotBlank(menu.getParentId())) {
				Menu parentMenu = ms.getMenuById(menu.getParentId());
				menu.setParent(parentMenu);
			}
		}
		return INPUT;
	}
	
	/**
	 * 跳转到菜单详情页面
	 * @return
	 * @throws Exception
	 */
	public String toMenuDetailView() {
		//获取菜单信息
		this.menu = ms.getMenuById(id);
		//获取父节点信息
		if(this.menu!=null && StringUtils.isNotBlank(this.menu.getParentId())) {
			Menu parentMenu = ms.getMenuById(this.menu.getParentId());
			menu.setParent(parentMenu);
		}
		return "detail";
	}
	
	/**
	 * 保存菜单信息
	 */
	public void save() {
		String success = "true";
		try {
			//保存菜单
			ms.saveMenu(menu);
		} catch (Exception e) {
			success = "保存菜单信息时出现异常：" + e.toString();
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), ""+success);
	}
	
	/**
	 * 删除节点及其所有子孙节点
	 */
	public void removeAll() {
		String success = "true";
		try {
			ms.removeAll(id);
		} catch (Exception e) {
			success = e.toString();
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), ""+success);
	}
	
	/**
	 * 根据id获取XML格式的子节点列表数据
	 */
	public void getChildrenXmlById() {
		//获取XML格式的子节点列表数据
		String xml = ms.listChildrenXml(id);
		ResponseUtils.renderXml(ServletActionContext.getResponse(), xml);
	}
	
	/**
	 * 跳转二级菜单列表页面
	 * @return
	 */
	public String toSecondLevelMenuListView() {
		//从session中获取二级菜单数据
		User user = (User) request.getSession().getAttribute(IConstants.USER_VIEW_KEY);
		List<Menu> secondMenuList = null;
		for(Menu m : user.getMenuList()) {
			if(m.getId().equals(id)) {
				secondMenuList = m.getChildren();
				break;
			}
		}
		request.setAttribute("secondMenuList", secondMenuList);
		return "secondLevelMenuList";
	}

}
