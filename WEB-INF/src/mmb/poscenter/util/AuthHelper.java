package mmb.poscenter.util;

import javax.servlet.http.HttpServletRequest;

import mmb.auth.domain.User;
import mmb.framework.IConstants;

import org.apache.struts2.ServletActionContext;


public class AuthHelper {

	/**
	 * 获取当前用户的信息
	 * @return 当前用户对象
	 */
	public static User getCurrentUser() {
		HttpServletRequest request = ServletActionContext.getRequest();
		return (User) request.getSession().getAttribute(IConstants.USER_VIEW_KEY);
	}

}
