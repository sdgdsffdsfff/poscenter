package mmb.poscenter.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.Event;
import mmb.poscenter.domain.SwapBuyEvent;
import mmb.poscenter.service.EventService;
import mmb.poscenter.service.SwapBuyEventService;
import mmb.poscenter.util.ResponseUtils;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class SwapBuyEventAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(SwapBuyEventAction.class);
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private SwapBuyEventService sbes = new SwapBuyEventService();
	
	private Event event = new Event();
	private List<SwapBuyEvent> swapBuyEventList = new ArrayList<SwapBuyEvent>();
	private int swapBuyProductId;
	private int swapBuyEventId;
	
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<SwapBuyEvent> getSwapBuyEventList() {
		return swapBuyEventList;
	}

	public void setSwapBuyEventList(List<SwapBuyEvent> swapBuyEventList) {
		this.swapBuyEventList = swapBuyEventList;
	}

	public int getSwapBuyProductId() {
		return swapBuyProductId;
	}

	public void setSwapBuyProductId(int swapBuyProductId) {
		this.swapBuyProductId = swapBuyProductId;
	}

	public int getSwapBuyEventId() {
		return swapBuyEventId;
	}

	public void setSwapBuyEventId(int swapBuyEventId) {
		this.swapBuyEventId = swapBuyEventId;
	}
	
	/**
	 * 跳转到换购活动表单页面
	 * @return
	 */
	public String toSwapBuyEventFormView() {
		//新增
		if(this.event.getId() == 0) {
			return INPUT;
		}
		//修改
		else {
			this.event = sbes.getDetailEvent(event.getId());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("startTime", sdf.format(this.event.getStartTime()));
			request.setAttribute("endTime", sdf.format(this.event.getEndTime()));
			return "update";
		}
	}

	/**
	 * 跳转到查看页面
	 */
	public String catSwapBuyEventDetail(){
		this.event = sbes.getDetailEvent(event.getId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("startTime", sdf.format(this.event.getStartTime()));
		request.setAttribute("endTime", sdf.format(this.event.getEndTime()));
		return "cat";
	}
	
	/**
	 * 保存换购活动信息 
	 */
	public void saveEvent(){
		String result = "success";
		try {
			EventService es = new EventService();
			boolean isUnique = es.isUniqueEvent(event.getType(), event.getStartTime(), event.getEndTime(), event.getId(), event.getShopList());
			if(isUnique){
				if(event.getId() != 0 ){
					sbes.updateEvent(event, swapBuyEventList);
				}else{
					sbes.saveEvent(event, swapBuyEventList);
				}
			}else{
				result = "该活动时间内已经存在换购活动，请修改活动时间！";
			}
		} catch (Exception e) {
			result = e.toString();
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
	/**
	 * 删除换购商品信息
	 */
	public void deleteSwapBuyProduct(){
		String result = "false";
		if(swapBuyProductId != 0){
			try {
				sbes.deleteSwapBuyProduct(swapBuyProductId);
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
				result = "false";
			}
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
	/**
	 * 删除换购活动信息
	 */
	public void deleteSwapBuyEvent(){
		String result = "false";
		if(swapBuyEventId != 0){
			try {
				sbes.deleteSwapBuyEvent(swapBuyEventId);
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
				result = "false";
			}
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
	/**
	 * 删除活动信息
	 */
	public void deleteEvent(){
		if(event.id != 0){
			try {
				sbes.deleteEvent(event.id);
				ServletActionContext.getResponse().sendRedirect(request.getContextPath()+"/pos/event!eventList.do");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
