package mmb.auth.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mmb.auth.domain.Department;
import mmb.auth.service.DepartmentService;
import mmb.poscenter.action.Page;
import mmb.poscenter.util.ResponseUtils;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class DepartmentAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(DepartmentAction.class);
	@SuppressWarnings("unused")
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private DepartmentService ds = new DepartmentService();
	private Page<Department> page = new Page<Department>();
	private Department department = new Department();
	private int companyId;
	private String companyName;
	private String name;
	
	public Page<Department> getPage() {
		return page;
	}

	public void setPage(Page<Department> page) {
		this.page = page;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	
	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 跳转至部门列表界面
	 * @return
	 */
	public String departmentList(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("companyName", this.companyName);
		param.put("name", this.name);
		this.page = ds.getDepartmentPage(page, param);
		return SUCCESS;
	}
	
	/**
	 * 跳转至部门列表界面
	 * @return
	 */
	public String departmentChooseList(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("companyName", this.companyName);
		param.put("name", this.name);
		this.page = ds.getDepartmentPage(page, param);
		return "choose";
	}
	/**
	 * 跳转到部门表单页面
	 * @return
	 */
	public String toDepartmentFormView() {
		//修改
		if(this.department.getId() != 0) {
			this.department = ds.getDepartmentById(this.department.getId());
		}
		return INPUT;
	}
	
	/**
	 * 保存部门信息
	 */
	public void saveDepartment() {
		String result = "success";
		try {
			//部门名称唯一性校验（同一机构下）
			boolean isUnique = ds.isUniqueDepartmentName(this.department.getCompanyId(), this.department.getName(), this.department.getId());
			if(isUnique) {
				//新建
				if(this.department.getId() == 0) {
					ds.saveDepartment(department);
				}
				//修改
				else {
					ds.updateDepartment(department);
				}
			} else {
				result = "该机构下已存在部门（"+this.department.getName()+"）！";
			}
		} catch (Exception e) {
			log.error("保存部门信息时出现异常：", e);
			result = "保存部门信息时出现异常："+e;
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
	/**
	 * 删除部门信息
	 */
	public void deleteDepartment() {
		String result = "success";
		try {
			//判断该部门下是否有用户
			boolean hasUser = ds.hasUser(this.department.getId());
			if(hasUser) {
				result = "该机构下包含有用户信息！请先删除该部门下的所有用户。";
			} else {
				//删除
				ds.deleteDepartmentById(this.department.getId());
			}
		} catch (Exception e) {
			result = "删除部门信息时出现异常："+e;
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
}
