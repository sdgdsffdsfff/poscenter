package mmboa.base;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import mmboa.util.Base64x;
import mmboa.util.Constants;
import mmboa.util.RandomUtil;
import mmboa.base.voUser;
import mmb.framework.CustomAction;

public class UserAction extends CustomAction {
	
	public static UserService service = new UserService();

	public UserAction(HttpServletRequest request) {
		super(request);
	}
	
	public static void sendActivateEmail(voUser user) {
		String urlPath = Constants.SITE;
		String code = Base64x.encodeInt(RandomUtil.nextInt(90000000) + 10000000);
		
		UserActivate ua = new UserActivate();
		ua.setCode(code);
		ua.setUserId(user.getId());
		ua.setCreateTime(new Timestamp(System.currentTimeMillis()));
		service.addUserActivate(ua);
		
		String url;
		try {
			url = urlPath + "/enter/acti.jsp?id=" + ua.getId() + "&code=" + URLEncoder.encode(ua.getCode(),"utf8");
		} catch (UnsupportedEncodingException e) {
			return;
		}
		HashMap kv = new HashMap();
		kv.put("name", user.getName());
		kv.put("username", user.getUsername());
		kv.put("link", url);
	}
}