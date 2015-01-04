package mmb.poscenter.action;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.Event;
import mmb.poscenter.service.EventService;
import mmb.poscenter.service.MoneyDiscountEventService;
import mmb.poscenter.util.ResponseUtils;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class MoneyDiscountEventAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(MoneyDiscountEventAction.class);
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	private MoneyDiscountEventService mdes = new MoneyDiscountEventService();
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
					mdes.saveEvent(event);
				} else {
					mdes.updateEvent(event);
				}
			} else {
				result = "该活动时间内已经存在金额折扣活动，请修改活动时间！";
			}
		} catch (Exception e) {
			result = e.toString();
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
	/**
	 * 跳转到活动表单页面
	 * @return
	 */
	public String toEventFormView() {
		//新增
		if(this.event.getId() == 0) {
			return "add";
		}
		//修改
		else {
			this.event = mdes.getDetailEvent(event.getId());
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
		this.event = mdes.getDetailEvent(event.getId());
		return "detail";
	}
	
	/**
	 * 删除活动
	 */
	public void deleteEvent() {
		try {
			mdes.deleteEvent(this.event.getId());
			ServletActionContext.getResponse().sendRedirect(request.getContextPath()+"/pos/event!eventList.do");
		} catch (Exception e) {
			log.error("删除活动时出现异常：", e);
		}
	}
	
	/**
	 * 删除金额折扣
	 */
	public void deleteMoneyDiscountEvent() {
		String result = "success";
		try {
			int moneyDiscountEventId = Integer.parseInt(request.getParameter("moneyDiscountEventId"));
			mdes.deleteMoneyDiscountEvent(moneyDiscountEventId);
		} catch (Exception e) {
			result = e.toString();
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
}
