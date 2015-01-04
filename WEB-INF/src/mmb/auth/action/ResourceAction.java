package mmb.auth.action;

import javax.servlet.http.HttpServletRequest;

import mmb.auth.domain.Resource;
import mmb.auth.service.ResourceService;
import mmb.poscenter.util.ResponseUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class ResourceAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private ResourceService rs = new ResourceService();
	private Resource resource = new Resource();
	private String id;
	
	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource Resource) {
		this.resource = Resource;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 跳转到资源表单页面
	 * @return
	 */
	public String toResourceFormView(){
		//新增
		if(StringUtils.isBlank(id)) {
			//获取父节点信息，若父节点id为空则增加一级节点
			String parentId = request.getParameter("parentId");
			if(!StringUtils.isBlank(parentId)) {
				Resource parentResource = rs.getResourceById(parentId);
				resource.setParent(parentResource);
			}
			
			//设置层级
			resource.setTreeLevel(Integer.parseInt(request.getParameter("treeLevel")));
		}
		else {
			resource = rs.getResourceById(id);
			if(StringUtils.isNotBlank(resource.getParentId())) {
				Resource parentResource = rs.getResourceById(resource.getParentId());
				resource.setParent(parentResource);
			}
		}
		return INPUT;
	}
	
	/**
	 * 跳转到资源详情页面
	 * @return
	 * @throws Exception
	 */
	public String toResourceDetailView() {
		//获取资源信息
		this.resource = rs.getResourceById(id);
		//获取父节点信息
		if(this.resource!=null && StringUtils.isNotBlank(this.resource.getParentId())) {
			Resource parentResource = rs.getResourceById(this.resource.getParentId());
			resource.setParent(parentResource);
		}
		return "detail";
	}
	
	/**
	 * 保存资源信息
	 */
	public void save() {
		String success = "true";
		try {
			//保存资源
			rs.saveResource(resource);
		} catch (Exception e) {
			success = "保存资源信息时出现异常：" + e.toString();
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), ""+success);
	}
	
	/**
	 * 删除节点及其所有子孙节点
	 */
	public void removeAll() {
		String success = "true";
		try {
			//判断该资源和子孙资源是否已被菜单关联
			boolean hasUsed = rs.hasUsedByMenu(id);
			if(hasUsed) {
				success = "该资源或下级资源已被菜单关联！请解除关联后再进行删除操作！";
			} else {
				rs.removeAll(id);
			}
		} catch (Exception e) {
			success = "删除节点时出现异常："+e.toString();
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), ""+success);
	}
	
	/**
	 * 根据id获取XML格式的子节点列表数据
	 */
	public void getChildrenXmlById() {
		//获取XML格式的子节点列表数据
		String xml = rs.listChildrenXml(id);
		ResponseUtils.renderXml(ServletActionContext.getResponse(), xml);
	}
	
	/**
	 * 获取整个资源树的XML数据，
	 */
	public void getAllTreeXmlByRole() {
		int roleId = Integer.parseInt(request.getParameter("roleId"));
		String xml = rs.getAllTreeXmlByRole(roleId);
		ResponseUtils.renderXml(ServletActionContext.getResponse(), xml);
	}

}
