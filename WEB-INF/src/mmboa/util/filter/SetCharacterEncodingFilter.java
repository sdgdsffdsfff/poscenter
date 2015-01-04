package mmboa.util.filter;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mmb.auth.domain.User;
import mmb.framework.IConstants;
import mmb.poscenter.action.SendOrderAction;
import mmboa.util.Constants;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 编码设置过滤器
 */
public class SetCharacterEncodingFilter implements Filter {
	
	private static Logger log = Logger.getLogger(SendOrderAction.class);

	/**
	 * The default character encoding to set for requests that pass through this
	 * filter.
	 */
	protected String encoding = null;

	/**
	 * The filter configuration object we are associated with. If this value is
	 * null, this filter instance is not currently configured.
	 */
	protected FilterConfig filterConfig = null;

	/**
	 * Should a character encoding specified by the client be ignored?
	 */
	protected boolean ignore = true;

	/**
	 * Take this filter out of service.
	 */
	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}

	/**
	 * Select and set (if specified) the character encoding to be used to
	 * interpret request parameters for this request.
	 * 
	 * @param request
	 *            The servlet request we are processing
	 * @param result
	 *            The servlet response we are creating
	 * @param chain
	 *            The filter chain we are processing
	 * 
	 * @exception IOException
	 *                if an input/output error occurs
	 * @exception ServletException
	 *                if a servlet error occurs
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// Conditionally select and set the character encoding to be used
		if (ignore || (request.getCharacterEncoding() == null)) {
			String encoding = selectEncoding(request);
			if (encoding != null) {
				request.setCharacterEncoding(encoding);
			}
		}

		// 用户访问记录
		try {
			HttpServletRequest hsr = (HttpServletRequest) request;
			String url = hsr.getRequestURL().toString();
			if ((url.toLowerCase().endsWith(".jsp") || url.toLowerCase().endsWith(".do")) && !url.endsWith("login.do")) {
				String qs = hsr.getQueryString();
				HttpSession session = hsr.getSession();
				if (qs != null) {
					url += "?" + qs;
				}
				User user = (User) session.getAttribute(IConstants.USER_VIEW_KEY);
				int userId = 0;
				String userName = "null";
				if (user != null) {
					userId = user.getId();
					userName = user.getUserName();
				}
				String str = userName + "\t" + userId + "\t" + url + "\t" + hsr.getRemoteAddr();
				log.info(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		chain.doFilter(request, response);
	}

	/**
	 * Place this filter into service.
	 * 
	 * @param filterConfig
	 *            The filter configuration object
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.encoding = filterConfig.getInitParameter("encoding");
		String value = filterConfig.getInitParameter("ignore");
		if (value == null)
			this.ignore = true;
		else if (value.equalsIgnoreCase("true"))
			this.ignore = true;
		else if (value.equalsIgnoreCase("yes"))
			this.ignore = true;
		else
			this.ignore = false;

		loadOther(filterConfig.getServletContext());
	}

	private static void loadOther(ServletContext context) {

		String conf = context.getInitParameter("conf");
		if (conf != null) {
			Constants.CONFIG_PATH = conf;
		}

		try {
			loadConfig();
		} catch (Exception e) {
			System.out.println("[ERROR]config file load failed : " + Constants.CONFIG_PATH + "conf.properties");
			e.printStackTrace();
		}
	}

	/**
	 * 加载配置文件信息
	 * @throws Exception
	 */
	public static void loadConfig() throws Exception {
		if (Constants.CONFIG_PATH == null) {
			// 默认配置文件在项目路径下
			Constants.CONFIG_PATH = Constants.class.getResource("/").toURI().resolve("../config").getPath()+ "/";
		}

		// 修改log4j配置文件位置
		PropertyConfigurator.configure(Constants.CONFIG_PATH + "log4j.properties");

		FileInputStream fis = new FileInputStream(Constants.CONFIG_PATH + "conf.properties");
		Constants.config.load(fis);
		fis.close();

		System.out.println("[MSG]config file loaded : " + Constants.CONFIG_PATH + "conf.properties");
	}

	/**
	 * Select an appropriate character encoding to be used, based on the
	 * characteristics of the current request and/or filter initialization
	 * parameters. If no character encoding should be set, return
	 * <code>null</code>.
	 * <p>
	 * The default implementation unconditionally returns the value configured
	 * by the <strong>encoding </strong> initialization parameter for this
	 * filter.
	 * 
	 * @param request
	 *            The servlet request we are processing
	 */
	protected String selectEncoding(ServletRequest request) {
		return (this.encoding);
	}

}
