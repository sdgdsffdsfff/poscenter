package mmb.poscenter.action;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.Event;
import mmb.poscenter.service.ComboEventService;
import mmb.poscenter.service.EventService;
import mmb.poscenter.util.ResponseUtils;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class ComboEventAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ComboEventAction.class);
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	private ComboEventService ces = new ComboEventService();
	
	private Event event = new Event();
	
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	/**
	 * 保存买赠活动
	 */
	public void saveEvent() {
		String result = "success";
		try {
			EventService es = new EventService();
			boolean isUnique = es.isUniqueEvent(event.getType(), event.getStartTime(), event.getEndTime(), event.getId(), event.getShopList());
			if(isUnique) {
				//新增
				if(event.getId() == 0) {
					ces.saveEvent(event);
				} else {
					ces.updateEvent(event);
				}
			} else {
				result = "该活动时间内已经存在套餐活动，请修改活动时间！";
			}
		} catch (Exception e) {
			result = e.toString();
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
	/**
	 * 跳转到买赠活动表单页面
	 * @return
	 */
	public String toEventFormView() {
		//新增
		if(this.event.getId() == 0) {
			return "add";
		}
		//修改
		else {
			this.event = ces.getDetailEvent(event.getId());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			request.setAttribute("startTime", sdf.format(this.event.getStartTime()));
			request.setAttribute("endTime", sdf.format(this.event.getEndTime()));
			return "update";
		}
	}
	
	/**
	 * 跳转到详情页面
	 * @return
	 */
	public String toEventDetailView() {
		//获取活动信息
		this.event = ces.getDetailEvent(event.getId());
		return "detail";
	}
	
	/**
	 * 删除活动
	 */
	public void deleteEvent() {
		try {
			ces.deleteEvent(this.event.getId());
			ServletActionContext.getResponse().sendRedirect(request.getContextPath()+"/pos/event!eventList.do");
		} catch (Exception e) {
			log.error("删除活动时出现异常：", e);
		}
	}
	
	/**
	 * 删除套餐
	 */
	public void deleteComboEvent() {
		String result = "success";
		try {
			int comboEventId = Integer.parseInt(request.getParameter("comboEventId"));
			ces.deleteComboEvent(comboEventId);
		} catch (Exception e) {
			result = e.toString();
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
	/**
	 * 删除套餐商品
	 */
	public void deleteComboProduct() {
		String result = "success";
		try {
			int comboProductId = Integer.parseInt(request.getParameter("comboProductId"));
			ces.deleteComboProduct(comboProductId);
		} catch (Exception e) {
			result = e.toString();
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}

}
