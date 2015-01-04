package mmb.auth.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mmb.auth.domain.Company;
import mmb.auth.service.CompanyService;
import mmb.poscenter.action.Page;
import mmb.poscenter.util.ResponseUtils;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class CompanyAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CompanyAction.class);
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private CompanyService cs = new CompanyService();
	private Page<Company> page = new Page<Company>();
	private Company company = new Company();
	private String name;
	
	public Page<Company> getPage() {
		return page;
	}

	public void setPage(Page<Company> page) {
		this.page = page;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 跳转至机构列表界面
	 * @return
	 */
	public String companyList(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("name", this.name);
		this.page = cs.getCompanyPage(page, param);
		return SUCCESS;
	}
	
	/**
	 * 跳转到选择机构列表页面（供弹出层调用）
	 * @return
	 */
	public String toSelectCompanyListView(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("name", name);
		this.page = cs.getCompanyPage(page, param);
		return "selectCompanyList";
	}
	
	/**
	 * 跳转到机构表单页面
	 * @return
	 */
	public String toCompanyFormView() {
		//修改
		if(this.company.getId() != 0) {
			this.company = cs.getCompanyById(this.company.getId());
		}
		return INPUT;
	}
	
	/**
	 * 保存机构信息
	 * @return
	 */
	public String saveCompany() {
		try {
			//新建
			if(this.company.getId() == 0) {
				cs.saveCompany(company);
			}
			//修改
			else {
				cs.updateCompany(company);
			}
		} catch (Exception e) {
			request.setAttribute("message", e.getMessage());
			log.error("保存机构信息时出现异常：", e);
			return ERROR;
		}
		return this.companyList();
	}
	
	/**
	 * 删除机构信息
	 */
	public void deleteCompany() {
		String result = "success";
		try {
			//判断该机构下是否有部门
			boolean hasDepartment = cs.hasDepartment(this.company.getId());
			if(hasDepartment) {
				result = "该机构下包含有部门信息！请先删除该机构下的所有部门。";
			} else {
				//删除
				cs.deleteCompanyById(this.company.getId());
			}
		} catch (Exception e) {
			result = "删除机构信息时出现异常："+e;
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
	/**
	 * 机构名称唯一性校验
	 */
	public void isUniqueCompanyName() {
		String result = null;
		try {
			boolean isUnique = cs.isUniqueCompanyName(this.company.getName(), this.getCompany().getId());
			result = isUnique + "";
		} catch (Exception e) {
			result = e.toString();
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}

}
