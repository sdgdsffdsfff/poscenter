package mmb.poscenter.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.Event;
import mmb.poscenter.service.EventService;
import mmb.poscenter.util.ResponseUtils;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class EventAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(EventAction.class);
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	private EventService es = new EventService();
	private Event event = new Event();
	private Page<Event> page = new Page<Event>();
	
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Page<Event> getPage() {
		return page;
	}

	public void setPage(Page<Event> page) {
		this.page = page;
	}

	/**
	 * 跳转至活动列表界面
	 * @return
	 */
	public String eventList() {
		//修改过期活动的状态
		try {
			es.checkOutdatedEvent();
		} catch (Exception e) {
			request.setAttribute("message", "修改过期活动的状态时出现异常："+e);
			return ERROR;
		}
		
		//获取查询参数
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("event", event);
	
		
		//分页获取活动列表信息
		es.getEventPage(page, param);
		
		return SUCCESS;
	}
	
	/**
	 * 判断指定时间段中同一类型的活动是否唯一
	 */
	public void isUniqueEvent() {
		String result = null;
		try {
			boolean isUnique = es.isUniqueEvent(event.getType(), event.getStartTime(), event.getEndTime(), event.getId(), event.getShopList());
			result = isUnique + "";
		} catch (Exception e) {
			result = e.toString();
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}

}
