package mmb.poscenter.action;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mmb.poscenter.domain.PriceCard;
import mmb.poscenter.service.PriceCardService;
import mmb.poscenter.util.ResponseUtils;
import mmboa.util.Secure;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class PriceCardAction extends ActionSupport {

	private static final long serialVersionUID = -1886742717826609109L;
    private static Logger log = Logger.getLogger(PriceCardAction.class);
	
	private PriceCardService pcs = new PriceCardService();
	private PriceCard priceCard = new PriceCard();
	private Page<PriceCard> page = new Page<PriceCard>();
	private List<PriceCard> list = new ArrayList<PriceCard>();
	private String clerkName;
	private String priceCardId;
	private int type;
	private int state;
	
	public PriceCard getPriceCard() {
		return priceCard;
	}
	public void setPriceCard(PriceCard priceCard) {
		this.priceCard = priceCard;
	}
	public Page<PriceCard> getPage() {
		return page;
	}
	public void setPage(Page<PriceCard> page) {
		this.page = page;
	}
	public List<PriceCard> getList() {
		return list;
	}
	public void setList(List<PriceCard> list) {
		this.list = list;
	}
	public String getClerkName() {
		return clerkName;
	}
	public void setClerkName(String clerkName) {
		this.clerkName = clerkName;
	}
	public String getPriceCardId() {
		return priceCardId;
	}
	public void setPriceCardId(String priceCardId) {
		this.priceCardId = priceCardId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	public String priceCardList(){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("state", this.state);
		param.put("clerkname", this.clerkName);
		param.put("pricecardid", this.priceCardId);
		param.put("type", this.type);
		this.setPage(pcs.getPriceCardList(this.page, param));
		this.setList(this.page.getList());
		return SUCCESS;
	}
	//跳转到新建调价卡界面
	/**
	 * 跳转到选择调价卡列表页面（供弹出层调用）
	 * @return
	 */
	public String toSelectPriceCardListView() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("state", 2); //只显示‘可使用’的调价卡
		param.put("clerkname", this.clerkName);
		param.put("pricecardid", this.priceCardId);
		param.put("type", this.type);
		pcs.getPriceCardList(this.page, param);
		return "selectPriceCardList";
	}
	
	public String priceCardForm(){
		return INPUT;
	}
	
	//跳转到开卡界面
	public String openCardForm(){
		this.setPriceCard(pcs.getPriceCardById(this.priceCard.getId()));
		return "open";
	}
	
	//跳转到修改卡界面
	public String updateCardForm(){
		this.setPriceCard(pcs.getPriceCardById(this.priceCard.getId()));
		return "update";
	}
	
	//跳转到红蓝卡详细信息界面
	public String toPriceCardDtail(){
		this.setPriceCard(pcs.getPriceCardById(this.priceCard.getId()));
		return "detail";
	}
	
	
	//新增卡号信息
	public void addPriceCard(){
		String result = "success";
		try {
			//红蓝卡唯一性校验
			boolean isExist = pcs.checkExistId(priceCard.getId(), priceCard.getType());
			if(!isExist) {
				 //如果新建时开卡
				 if(priceCard.getState() == 2){
					 priceCard.setOpenTime(new Timestamp(new Date().getTime()));
				 }
				 if(StringUtils.isNotBlank(priceCard.getPassword())){
					 priceCard.setPassword(Secure.encryptPwd(priceCard.getPassword()));
				 }
			     pcs.addPriceCard(priceCard);
			} else {
				result = "该卡号（"+this.priceCard.getId()+"）已经存在！";
			}
		} catch (Exception e) {
			log.error("保存红蓝卡号信息时出现异常：", e);
			result = "保存红蓝卡号信息时出现异常："+e;
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
	//开卡
	public void openPriceCard(){
		String result = "success";
		try {
			this.priceCard.setPassword(Secure.encryptPwd(priceCard.getPassword()));
			pcs.openPriceCard(this.priceCard);
		} catch (Exception e) {
			log.error("开卡时出现异常：", e);
			result = "开卡时出现异常："+e;
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
	}
	
	//修改卡信息
	public void updatePriceCard(){
		String result = "success";
		try {
			if(StringUtils.isNotBlank(priceCard.getPassword())){
				this.priceCard.setPassword(Secure.encryptPwd(priceCard.getPassword()));
			}
			pcs.updatePriceCard(this.priceCard);
		} catch (Exception e) {
			log.error("开卡时出现异常：", e);
			result = "开卡时出现异常："+e;
		}
		ResponseUtils.renderText(ServletActionContext.getResponse(), result);
		
	}

}
