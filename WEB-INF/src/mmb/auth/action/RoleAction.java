package mmb.auth.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mmb.auth.domain.Role;
import mmb.auth.service.RoleService;
import mmb.poscenter.action.Page;
import mmb.poscenter.util.ResponseUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class RoleAction extends ActionSupport {

	private static final long serialVersionUID = 9154799399106055114L;
	private static Logger log = Logger.getLogger(RoleAction.class);
    private HttpServletRequest request = ServletActionContext.getRequest();
	
	private RoleService rs = new RoleService();
	private Page<Role> page = new Page<Role>();
	private Role role =  new Role();
	private String roleName;
	private String roleChName;
	private int userId;
	private int roleId;
	private String resIds;
	
	public Page<Role> getPage() {
		return page;
	}

	public void setPage(Page<Role> page) {
		this.page = page;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleChName() {
		return roleChName;
	}

	public void setRoleChName(String roleChName) {
		this.roleChName = roleChName;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getResIds() {
		return resIds;
	}

	public void setResIds(String resIds) {
		this.resIds = resIds;
	}

	/**
	 * 跳转至角色列表界面
	 * @return
	 */
	public String roleList(){
		Map<String,String> param = null;
		if(StringUtils.isNotBlank(roleName) || StringUtils.isNotBlank(roleChName)){
			param = new HashMap<String,String>();
			param.put("rolename", roleName);
			param.put("rolechname", roleChName);
		}
		this.page = rs.getRolePage(page,param);
		return SUCCESS;
	}
	
	/**
	 * 跳转到为角色分配资源的页面
	 * @return
	 */
	public String roleResourceListView(){
		return "roleResource";
	}
	/**
	 * 跳转到角色表单页面
	 * @return
	 */
	public String toRoleFormView() {
		//修改
		if(this.role.getId() != 0) {
			this.role = rs.getRoleById(this.role.id);
		}
		return INPUT;
	}
	
	/**
	 * 跳转到角色详情页面
	 * @return
	 */
	public String toRoleDetailView() {
		//获取角色信息
		this.role = rs.getRoleById(this.role.getId());
		return "detail";
	}
	
	/**
	 * 保存角色信息
	 * @return
	 */
	public void saveRole() {
		String result = "success";
		try {
			boolean existsRole = rs.checkExistRoleName(this.role.getRoleName(), this.role.getId());
			if(!existsRole){
				//新建
				if(this.role.getId() == 0) {
					this.role.setCreateTime(new Timestamp(new Date().getTime()));
					rs.addRole(this.role);
				//修改
				}else {
					rs.updateRole(this.role);
				}
			}else{
				result = "该角色名称已经存在！";
			}
		} catch (Exception e) {
			log.error("保存角色信息时出现异常:", e);
		}finally{
			ResponseUtils.renderText(ServletActionContext.getResponse(), result);
		}
	}
	
	/**
	 * 删除角色信息
	 * @return
	 */
	public String deleteRole() {
		//删除
		rs.deleteRoleById(this.role.getId());
		return this.roleList();
	}
	
	/**
	 * 为用户分配角色页面
	 * @return
	 */
	public String toAllotRoleView() {
		try {
			//获取所有角色列表数据
			this.page = new Page<Role>(1, Integer.MAX_VALUE);
			this.page = rs.getRolePage(page, null);
			
			//获取用户已分配的角色
			List<Role> allottedRoleList = rs.getRoleListByUser(userId);
			request.setAttribute("allottedRoleList", allottedRoleList);
		} catch (Exception e) {
			request.setAttribute("message", "获取用户已分配的角色时出现异常：" + e.toString());
			return ERROR;
		}
		return "allotRole";
	}
	
	/**
	 * 分配角色
	 */
	public void allotRole() {
		String result = "success";
		try {
			List<Integer> roleIdList = new ArrayList<Integer>();
			String roleIds = request.getParameter("roleIds");
			if(StringUtils.isNotBlank(roleIds)) {
				for(String roleId : roleIds.split(",")) {
					roleIdList.add(Integer.parseInt(roleId));
				}
			}
			rs.allotRole(this.userId, roleIdList);
		} catch (Exception e) {
			result = "分配角色时出现异常：" + e;
		} finally {
			ResponseUtils.renderText(ServletActionContext.getResponse(), result);
		}
	}
	
	/**
	 * 为角色分配资源
	 */
	public void saveRoleResource(){
		String result = "success";
		try{
		   if(!rs.saveRoleResource(roleId, resIds)){
			 result = "保存角色资源信息出错";
		   }
		}catch(Exception e){
			result = "保存角色资源信息异常：" + e;
		}finally{
			ResponseUtils.renderText(ServletActionContext.getResponse(), result);
		}
	}

}
