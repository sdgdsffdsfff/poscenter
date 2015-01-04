package mmb.auth.tag;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import mmb.auth.domain.Resource;
import mmb.auth.domain.User;
import mmb.framework.IConstants;

/**
 * 权限判断标签
 * @author likg
 */
public class AuthTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 当前用户是否有指定的权限
	 */
	private String ifGranted;
	
	public void setIfGranted(String ifGranted) {
		this.ifGranted = ifGranted;
	}

	@Override
	public int doStartTag() throws JspException {
		boolean isShow = false;
		HttpSession session = pageContext.getSession();
		User user = (User) session.getAttribute(IConstants.USER_VIEW_KEY);
		if(user != null) {
			for(Resource r : user.getResourceList()) {
				if(r.getResUrl().equals(ifGranted)) {
					isShow = true;
					break;
				}
			}
		}
		if (isShow) {
			return EVAL_BODY_INCLUDE;
		} else {
			return SKIP_BODY;
		}
	}

}