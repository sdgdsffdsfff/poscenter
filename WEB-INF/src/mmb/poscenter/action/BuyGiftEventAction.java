package mmb.poscenter.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.BuyGiftEvent;
import mmb.poscenter.domain.Event;
import mmb.poscenter.service.BuyGiftEventService;
import mmb.poscenter.service.EventService;
import mmb.poscenter.util.ResponseUtils;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class BuyGiftEventAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(BuyGiftEventAction.class);
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	private BuyGiftEventService bfes = new BuyGiftEventService();
	
	private Event event = new Event();
	private List<BuyGiftEvent> buyGiftEventList = new ArrayList<BuyGiftEvent>();
	
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<BuyGiftEvent> getBuyGiftEventList() {
		return buyGiftEventList;
	}

	public void setBuyGiftEventList(List<BuyGiftEvent> buyGiftEventList) {
		this.buyGiftEventList = buyGiftEventList;
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
					bfes.saveEvent(event, buyGiftEventList);
				} else {
					bfes.updateEvent(event, buyGiftEventList);
				}
			} else {
				result = "该活动时间内已经存在买赠活动，请修改活动时间！";
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
			this.event = bfes.getDetailEvent(event.getId());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			request.setAttribute("startTime", sdf.format(this.event.getStartTime()));
			request.setAttribute("endTime", sdf.format(this.event.getEndTime()));
			return "update";
		}
	}
	
	/**
	 * 跳转到买赠活动详情页面
	 * @return
	 */
	public String toEventDetailView() {
		//获取活动信息
		this.event = bfes.getDetailEvent(event.getId());
		return "detail";
	}
	
	/**
	 * 删除活动
	 */
	public void deleteEvent() {
		try {
			bfes.deleteEvent(this.event.getId());
			ServletActionContext.getResponse().sendRedirect(request.getContextPath()+"/pos/event!eventList.do");
		} catch (Exception e) {
			log.error("删除活动时出现异常：", e);
		}
	}
	
	/**
	 * 删除促销商品
	 */
	public void deleteBuyGiftEvent() {
		String result = "success";
		try {
			int buyGiftEventId = Integer.parseInt(request.getParameter("buyGiftEventId"));
			bfes.deleteBuyGiftEvent(buyGiftEventId);
		} catch (Exception e) {
			result = e.toString();
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
	/**
	 * 删除赠品
	 */
	public void deleteBuyGiftProduct() {
		String result = "success";
		try {
			int buyGiftProductId = Integer.parseInt(request.getParameter("buyGiftProductId"));
			bfes.deleteBuyGiftProduct(buyGiftProductId);
		} catch (Exception e) {
			result = e.toString();
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}

}
