package mmb.poscenter.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.CountDiscountEvent;
import mmb.poscenter.domain.Event;
import mmb.poscenter.service.CountDiscountEventService;
import mmb.poscenter.service.EventService;
import mmb.poscenter.util.ResponseUtils;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class CountDiscountEventAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CountDiscountEventAction.class);
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	private CountDiscountEventService cdes = new CountDiscountEventService();
	
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
					cdes.saveEvent(event);
				} else {
					cdes.updateEvent(event);
				}
			} else {
				result = "该活动时间内已经存在数量折扣活动，请修改活动时间！";
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
			this.event = cdes.getDetailEvent(event.getId());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			request.setAttribute("startTime", sdf.format(this.event.getStartTime()));
			request.setAttribute("endTime", sdf.format(this.event.getEndTime()));
			
			//分开固定折扣与阶梯折扣
			List<CountDiscountEvent> fixedCountDiscountEventList = new ArrayList<CountDiscountEvent>();
			List<List<CountDiscountEvent>> ladderCountDiscountEventListList = new ArrayList<List<CountDiscountEvent>>();
			List<CountDiscountEvent> ladderCountDiscountEventList = new ArrayList<CountDiscountEvent>();
			int nowProductId = 0;
			for(CountDiscountEvent countDiscountEvent : event.getCountDiscountEventList()) {
				if(countDiscountEvent.getType() == 2){
					fixedCountDiscountEventList.add(countDiscountEvent);
				} else {
					if(nowProductId != countDiscountEvent.getProductId()) {
						if(nowProductId != 0){
							ladderCountDiscountEventListList.add(ladderCountDiscountEventList);
						}
						ladderCountDiscountEventList = new ArrayList<CountDiscountEvent>();
					}
					ladderCountDiscountEventList.add(countDiscountEvent);
					nowProductId = countDiscountEvent.getProductId();
				}
			}
			if(fixedCountDiscountEventList.size() != event.getCountDiscountEventList().size()) {
				ladderCountDiscountEventListList.add(ladderCountDiscountEventList);
			}
			request.setAttribute("fixedCountDiscountEventList", fixedCountDiscountEventList);
			request.setAttribute("ladderCountDiscountEventListList", ladderCountDiscountEventListList);
			return "update";
		}
	}
	
	/**
	 * 跳转到详情页面
	 * @return
	 */
	public String toEventDetailView() {
		//获取活动信息
		this.event = cdes.getDetailEvent(event.getId());
		
		//分开固定折扣与阶梯折扣
		List<CountDiscountEvent> fixedCountDiscountEventList = new ArrayList<CountDiscountEvent>();
		List<List<CountDiscountEvent>> ladderCountDiscountEventListList = new ArrayList<List<CountDiscountEvent>>();
		List<CountDiscountEvent> ladderCountDiscountEventList = new ArrayList<CountDiscountEvent>();
		int nowProductId = 0;
		for(CountDiscountEvent countDiscountEvent : event.getCountDiscountEventList()) {
			if(countDiscountEvent.getType() == 2){
				fixedCountDiscountEventList.add(countDiscountEvent);
			} else {
				if(nowProductId != countDiscountEvent.getProductId()) {
					if(nowProductId != 0){
						ladderCountDiscountEventListList.add(ladderCountDiscountEventList);
					}
					ladderCountDiscountEventList = new ArrayList<CountDiscountEvent>();
				}
				ladderCountDiscountEventList.add(countDiscountEvent);
				nowProductId = countDiscountEvent.getProductId();
			}
		}
		if(fixedCountDiscountEventList.size() != event.getCountDiscountEventList().size()) {
			ladderCountDiscountEventListList.add(ladderCountDiscountEventList);
		}
		request.setAttribute("fixedCountDiscountEventList", fixedCountDiscountEventList);
		request.setAttribute("ladderCountDiscountEventListList", ladderCountDiscountEventListList);
		return "detail";
	}
	
	/**
	 * 删除活动
	 */
	public void deleteEvent() {
		try {
			cdes.deleteEvent(this.event.getId());
			ServletActionContext.getResponse().sendRedirect(request.getContextPath()+"/pos/event!eventList.do");
		} catch (Exception e) {
			log.error("删除活动时出现异常：", e);
		}
	}
	
	/**
	 * 删除指定商品的折扣
	 */
	public void deleteByProduct() {
		String result = "success";
		try {
			int productId = Integer.parseInt(request.getParameter("productId"));
			cdes.deleteByProduct(event.getId(), productId);
		} catch (Exception e) {
			result = e.toString();
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
	/**
	 * 删除数量折扣
	 */
	public void deleteCountDiscountEvent() {
		String result = "success";
		try {
			int countDiscountEventId = Integer.parseInt(request.getParameter("countDiscountEventId"));
			cdes.deleteCountDiscountEvent(countDiscountEventId);
		} catch (Exception e) {
			result = e.toString();
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
}
