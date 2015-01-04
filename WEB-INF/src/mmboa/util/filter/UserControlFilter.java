package mmboa.util.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mmb.auth.domain.User;
import mmb.auth.service.UserService;
import mmb.framework.IConstants;
import mmboa.util.Base64x;
import mmboa.util.CookieUtil;
import mmboa.util.LogUtil;
import mmboa.util.Secure;

/**
 * 用户登录控制过滤器
 * @author Bomb
 */
public class UserControlFilter implements Filter {

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hsr = (HttpServletRequest) request;
		HttpSession session = hsr.getSession(false);

		String url = hsr.getServletPath();
		if (url.startsWith("/enter")) {
			chain.doFilter(request, response);
			return;
		}
		User user = null;
		if (session != null) {
			user = (User) session.getAttribute(IConstants.USER_VIEW_KEY);
		}

		if (user != null) {
			chain.doFilter(request, response);
			return;
		}
		// 试图从cookie登陆
		else {
			CookieUtil ck = new CookieUtil(hsr, (HttpServletResponse) response);
			String username = ck.getCookieValue("opau");
			String password = ck.getCookieValue("opap");
			if (password != null) {
				password = Base64x.decodeString(password);
			}
			if (username != null && password != null) {
				UserService us = new UserService();
				user = us.getUserByUserName(username, 1);
				if (user != null && user.getUserPassword().equals(Secure.encryptPwd(password))) {
					// 获取用户详细信息
					user = us.getUserDetail(user.getId());
					session = hsr.getSession(true);
					session.setAttribute(IConstants.USER_VIEW_KEY, user);
					// cookie登陆之前有日志但是没有记录username
					String str = user.getUserName() + "\t" + user.getId() + "\t" + url + "\t" + hsr.getRemoteAddr();
					LogUtil.logAccess(str);
					chain.doFilter(request, response);
					return;
				}
			}
		}
		((HttpServletResponse) response).sendRedirect(hsr.getContextPath() + "/enter/login.jsp");
	}

	public void destroy() {
	}
}
