package mmb.auth.action;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mmb.auth.domain.Role;
import mmb.auth.domain.User;
import mmb.auth.service.UserService;
import mmb.poscenter.action.Page;
import mmb.poscenter.util.AuthHelper;
import mmb.poscenter.util.ResponseUtils;
import mmboa.util.Secure;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport{

	private static final long serialVersionUID = -5444222174877283456L;
	
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(UserAction.class);
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private UserService us = new UserService();
	
	private Page<User> page = new Page<User>();
	
	private String userName;
	
	private String departmentName;
	
	private User user = new User();
	
	private String roleName;

	public Page<User> getPage() {
		return page;
	}

	public void setPage(Page<User> page) {
		this.page = page;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
    
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * 跳转至用户列表界面
	 * @return
	 */
	public String userList(){
		Map<String,String> param = null;
		if(StringUtils.isNotBlank(userName) || StringUtils.isNotBlank(departmentName)){
			param = new HashMap<String,String>();
			param.put("username", userName);
			param.put("departmentname", departmentName);
		}
		this.page = us.getUserPage(page,param);
		return SUCCESS;
	}

	/**
	 * 跳转到用户表单页面
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String toUserFormView() {
		//修改
		if(this.user.getId() != 0) {
			this.user = us.getUserById(this.user.id);
		}
		return INPUT;
	}
	
	/**
	 * 跳转到用户详情页面
	 * @return
	 */
	public String toUserDetailView() {
		
		try {
			//获取用户信息
			this.user = us.getUserById(this.user.getId());
			StringBuilder sb = new StringBuilder();
			for(Role role : user.getRoleList()){
				sb.append(role.getRoleName()).append("(").append(role.getRoleChName()).append(")");
				sb.append(",");
			}
			if(sb.length() > 1){
				sb.deleteCharAt(sb.length()-1);
			}
			this.setRoleName(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "detail";
	}
	
	/**
	 * 保存用户信息
	 * @return
	 */
	public void saveUser() {
		String result = "success";
		try {
			boolean existsUser = us.checkExistUserName(this.user.getUserName(),this.user.getId());
			if(!existsUser){
				//新建
				if(this.user.getId() == 0) {
					this.user.setCreateTime(new Timestamp(new Date().getTime()));
					us.addUser(this.user);
				//修改
				}else {
					this.user.setCreateTime(new Timestamp(new Date().getTime()));
					us.updateUser(this.user);
				}
			}else{
				result = "该用户名已经存在，请更改！" ;
			}
		} catch (Exception e) {
			ServletActionContext.getRequest().setAttribute("message", e.getMessage());
			e.printStackTrace();
		}finally{
			ResponseUtils.renderText(ServletActionContext.getResponse(), result);
		}
		
	}
	
	/**
	 * 删除用户信息
	 * @return
	 */
	public String deleteUser() {
		//删除
		us.deleteUserById(this.user.getId());
		return this.userList();
	}
	
	/**
	 * 修改用户密码
	 */
	public void updatePassword() {
		String result = "success";
		try {
			//判断旧密码是否正确
			User currentUser = AuthHelper.getCurrentUser();
			String oldPassword = request.getParameter("oldPassword");
			if(currentUser.getUserPassword().equals(Secure.encryptPwd(oldPassword))) {
				us.updatePassword(user);
			} else {
				result = "旧密码输入有误！";
			}
		} catch (Exception e) {
			result = "修改用户密码时出现异常："+e;
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
}
