package mmboa.enter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mmb.auth.domain.User;
import mmb.auth.service.UserService;
import mmb.framework.IConstants;
import mmboa.util.Base64x;
import mmboa.util.CookieUtil;
import mmboa.util.Secure;
import mmboa.util.StringUtil;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Namespace;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 登录入口
 */
@Namespace("/enter")
public class LoginAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();

	public String execute() throws Exception {
		String username = StringUtil.dealParam(request.getParameter("username"));
		String password = request.getParameter("password");

		// 写入cookie
		CookieUtil ck = new CookieUtil(request, response);
		if (username != null) { // post提交才有效
			boolean ru = request.getParameter("ru") != null;
			if (ru) { // 记住用户名
				if (username.length() > 0) {
					ck.setCookie("u", username, 2000000000);
				}
				ck.setCookie("ru", "1", 2000000000);
			} else {
				ck.removeCookie("u");
				ck.setCookie("ru", "0", 2000000000);
			}
		} else {
			return SUCCESS;
		}
		
		try {
			//判断用户名和密码是否正确
			UserService us = new UserService();
			User user = us.getUserByUserName(username, 0);
			if (user == null) {
				request.setAttribute("message", "用户名不存在！");
				return SUCCESS;
			}
			else if (user.getUseStatus() == 2) {
				request.setAttribute("message", "用户名已被禁用！");
				return SUCCESS;
			}
			else if (!user.getUserPassword().equals(Secure.encryptPwd(password))) {
				request.setAttribute("message", "密码错误！");
				return SUCCESS;
			}
			
			//获取用户详细信息
			user = us.getUserDetail(user.getId());
			
			// 自动登录（7天）
			boolean rp = request.getParameter("rp") != null;
			if (rp) {
				ck.setCookieSafe("opau", username, 86400 * 7);
				ck.setCookieSafe("opap", Base64x.encodeString(password), 86400 * 7);
				ck.setCookie("rp", "1", 86400 * 365);
			} else {
				ck.setCookieSafe("opau", username, -1);
				ck.setCookieSafe("opap", Base64x.encodeString(password), -1);
				ck.setCookie("rp", "2", 86400 * 365);
			}

			//把用户信息保存到session中
			request.getSession().setAttribute(IConstants.USER_VIEW_KEY, user);
		} catch (Exception e) {
			return SUCCESS;
		}
		
		response.sendRedirect(request.getContextPath() + "/poscenter/desktop/index.jsp");
		return null;
	}
}
