package mmb.poscenter.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mmb.poscenter.domain.ShopVersion;
import mmb.poscenter.service.AdjustPriceBillService;
import mmb.poscenter.service.EventService;
import mmb.poscenter.service.GoodsClassService;
import mmb.poscenter.service.MemberService;
import mmb.poscenter.service.PriceCardService;
import mmb.poscenter.service.ProductService;
import mmb.poscenter.service.SendOrderService;
import mmb.poscenter.service.ShopInvoiceService;
import mmb.poscenter.service.ShopLeaseOrderService;
import mmb.poscenter.service.ShopReturnOrderService;
import mmb.poscenter.service.ShopSaledOrderService;
import mmb.poscenter.service.ShopService;
import mmb.poscenter.service.ShopStockService;
import mmb.poscenter.service.ShopVersionService;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.opensymphony.xwork2.ActionSupport;

/**
 * poscenter与posadmin接口 
 *
 */
public class PosadminAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(PosadminAction.class);
	
	/**
	 * 店面同步商品信息
	 */
	public void syncProduct(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(new ProductService().getAllProductJson());
		} catch (Exception e) {
			log.error("店面同步商品信息时出现异常：", e);
		}
	}
	
	/**
	 * 店面同步商品分类信息
	 */
	public void syncGoodsClass(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(new GoodsClassService().getAllGoodsClassJson());
		} catch (Exception e) {
			log.error("店面同步商品分类信息时出现异常：", e);
		}
	}

	/**
	 * posadmin同步发货单信息
	 */
	public void syncSendOrder() {
		SendOrderService sos = new SendOrderService();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			//请求数据json字符串
			String json = this.inputStreamToString(ServletActionContext.getRequest());
			response.getWriter().write(sos.syncSendOrder(json));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * posadmin同步发货单状态信息
	 * <br/>posadmin在提交保存收货单事务之前调中此接口，中心库修改发货单的状态
	 */
	public void syncSendOrderStatus() {
		SendOrderService sos = new SendOrderService();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			String json = this.inputStreamToString(ServletActionContext.getRequest());
			response.getWriter().write(sos.syncSendOrderStatus(json)+"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * posadmin检查系统更新
	 * <br/>获取最新系统版本信息
	 */
	public void getLatestShopVersion() {
		ShopVersionService svs = new ShopVersionService();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			String json = svs.shopGetLatestShopVersion();
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * posadmin检查系统更新
	 * <br/>获取最新系统版本信息
	 */
	public void downloadShopVersion() {
		ShopVersionService svs = new ShopVersionService();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			//请求数据json字符串
			String json = this.inputStreamToString(ServletActionContext.getRequest());
			ShopVersion shopVersion = new Gson().fromJson(json, ShopVersion.class);
			
			//获取版本信息
			shopVersion = svs.getByVersionNumber(shopVersion.getVersionNumber());
			
			//更新包
			File deployFile = new File(ServletActionContext.getServletContext().getRealPath("/") + shopVersion.getFilePath());
			
			response.getOutputStream().write(FileUtils.readFileToByteArray(deployFile));
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 向posadmin机同步会员变动的全部信息
	 */
	public void syncMemberToPosadmin(){
		MemberService ms = new MemberService();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			//请求数据json字符串
			String json = this.inputStreamToString(ServletActionContext.getRequest());
	        //System.out.println(ms.getUpdateMemberInfo(ms.parseAdminRequestJson(json)));
			response.getWriter().write(ms.getUpdateMemberInfo(ms.parseAdminRequestJson(json)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 接收posadmin店面店同步会员变动的全部信息
	 */
	public void receivePosAdminMember(){
		MemberService ms = new MemberService();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			//请求数据json字符串
			String json = this.inputStreamToString(ServletActionContext.getRequest());
			boolean success = ms.batUpdateMemberInfo(ms.parseMemberJsonFromAdmin(json));
			if(success){
				 response.getWriter().write("ok");
			}else{
				 response.getWriter().write("error");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * posadmin提交店面库存
	 */
	public void submitShopStock() {
		ShopStockService sss = new ShopStockService();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			//请求数据json字符串
			String json = this.inputStreamToString(ServletActionContext.getRequest());
			response.getWriter().write(sss.submitShopStock(json));
		} catch (Exception e) {
			log.error("posadmin提交店面库存时出现异常：", e);
		}
	}
	
	/**
	 * 接收posadmin店面店同步的进销存记录信息
	 */
	public void receiveShopInvoiceInfo() {
		//请求数据JSON字符串
		String json = this.inputStreamToString(ServletActionContext.getRequest());
		
		String result = "error";
		if (StringUtils.isNotBlank(json)) {
			boolean success = new ShopInvoiceService().batInsert(json);
			if (success) {
				result = "ok";
			}
		}

		//返回结果
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 接收posadmin店面店同步的 租赁订单信息
	 */
	public void receiveShopLeaseOrderInfo(){
		ShopLeaseOrderService slos = new ShopLeaseOrderService();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			//请求数据json字符串
			String json = this.inputStreamToString(ServletActionContext.getRequest());
			if("".equals(json) || json == null){
				 response.getWriter().write("error");
			}
			boolean success = slos.batInserData(slos.parsePosadminJson(json));
			if(success){
				 response.getWriter().write("ok");
			}else{
				 response.getWriter().write("error");
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 接收posadmin店面店同步的 销售订单信息
	 */
	public void receiveShopSaledOrderInfo(){
		ShopSaledOrderService ssos = new ShopSaledOrderService();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			//请求数据json字符串
			String json = this.inputStreamToString(ServletActionContext.getRequest());
			if("".equals(json) || json == null){
				 response.getWriter().write("error");
			}
			boolean success = ssos.batInserData(ssos.parsePosadminJson(json));
			if(success){
				 response.getWriter().write("ok");
			}else{
				 response.getWriter().write("error");
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * posadmin提交店面退货单
	 */
	public void submitShopReturnOrder() {
		ShopReturnOrderService sros = new ShopReturnOrderService();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			//请求数据json字符串
			String json = this.inputStreamToString(ServletActionContext.getRequest());
			response.getWriter().write(sros.submitShopReturnOrder(json));
		} catch (Exception e) {
			log.error("posadmin提交店面退货单时出现异常：", e);
		}
	}
	
	public void updateShopIpAddress(){
		  ShopService ss = new ShopService();
		  HttpServletResponse response = ServletActionContext.getResponse();
		  response.setCharacterEncoding("utf-8");
			try {
				//请求数据json字符串
				String json = this.inputStreamToString(ServletActionContext.getRequest());
				response.getWriter().write(ss.updateShopIpAddress(json));
			} catch (Exception e) {
				log.error("posadmin提交店面退货单时出现异常：", e);
			}
		  
	}
	
	/**
	 * 同步促销活动
	 */
	public void syncEvent() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			//请求数据JSON字符串
			String json = this.inputStreamToString(ServletActionContext.getRequest());
			response.getWriter().write(new EventService().syncEvent(json));
		} catch (Exception e) {
			log.error("店面同步促销活动时出现异常：", e);
		}
	}
	/**
	 * 同步红蓝卡信息
	 */
	public void syncPriceCardInfo(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			//请求数据JSON字符串
			String json = this.inputStreamToString(ServletActionContext.getRequest());
			response.getWriter().write(new PriceCardService().syncPriceCardInfo(json));
		} catch (Exception e) {
			log.error("店面同步红蓝卡信息时出现异常：", e);
		}
		
	}
	/**
	 * 删除红蓝卡充值临时记录信息
	 * 
	 */
	public void SyncTempPriceCardRecordDel(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		String delstat = "false";
	
		try {
			//请求数据JSON字符串
			String json = this.inputStreamToString(ServletActionContext.getRequest());
			JsonReader jr = new JsonReader(new StringReader(json));
			String shopCode = null;
			String status = null;
			Map<String,Double> points = new HashMap<String,Double>();
			jr.beginObject();
			String attrName = null;
			while(jr.hasNext()){
				attrName = jr.nextName();
				if("shopCode".equals(attrName)){
					shopCode = jr.nextString();
				}else if("status".equals(attrName)){
					status = jr.nextString();
				}else if("pricePointList".equals(attrName)){
					jr.beginArray();
					while(jr.hasNext()){
					jr.beginObject();
					String id = "";
					double point = 0;
					while (jr.hasNext()) {
						attrName = jr.nextName();
						if("priceCardId".equals(attrName)){
							id = jr.nextString();
						}else if("point".equals(attrName)){
							point = jr.nextDouble();
						}
					}
					points.put(id, point);
					jr.endObject();
					}
					jr.endArray();
				}
			}
			if("success".equals(status)){
				PriceCardService pcs = new PriceCardService();
				if(pcs.deleteTempPriceCardAndUpdatePoint(shopCode,points)){
					delstat = "true";
				}
			}
			response.getWriter().write(delstat);
		} catch (Exception e) {
			log.error("删除红蓝卡临时信息时出现异常：", e);
		}
		
	}

	
	/**
	 * 店面提交调价单
	 */
	public void submitAdjustPriceBill() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			//请求数据JSON字符串
			String json = this.inputStreamToString(ServletActionContext.getRequest());
			response.getWriter().write(new AdjustPriceBillService().submitAdjustPriceBill(json));
		} catch (Exception e) {
			log.error("店面提交调价单时出现异常：", e);
		}
	}
	
	/**
	 * 店面同步调价单的审核状态
	 */
	public void syncAdjustPriceBill() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			//请求数据JSON字符串
			String json = this.inputStreamToString(ServletActionContext.getRequest());
			response.getWriter().write(new AdjustPriceBillService().syncAdjustPriceBill(json));
		} catch (Exception e) {
			log.error("店面同步调价单的审核状态时出现异常：", e);
		}
	}
	
	/**
	 * 把请求输入流转化为字符串
	 * @param request 请求对象
	 * @return
	 */
	private String inputStreamToString(HttpServletRequest request) {
		StringBuilder json = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			String line;
			while((line=br.readLine()) != null) {
				json.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {}
			}
		}
		return json.toString();
	}
	
}
