package mmboa.util;

import java.util.Properties;


/**
 * @author zhouj
 *  
 */
public class Constants {
    
	// 新路径配置
	public static String SITE = "";
	public static String WEB_ROOT = "";
	public static String UPLOAD_ROOT = "/server_root/adult/upload/";
	public static String RESOURCE_PRODUCT_IMAGE = "http://rep.mmb.cn/wap/upload/productImage/";
	public static String RESOURCE_WPRODUCT_IMAGE = "/wap/upload/wproductImage/";
	public static String DOWNLOAD_PATH = "";
	public static String UPLOAD_DIARY= "";
	
	//上传图片的地址
	public static String UPLOAD_PRODUCT_IMAGE = UPLOAD_ROOT + "productImage/";
	public static String UPLOAD_WPRODUCT_IMAGE = UPLOAD_ROOT + "wproductImage/";
	public static String UPLOAD_WPOOL_FILE = UPLOAD_ROOT + "wpoolFile/";
	
	public static String UPLOAD_ARTICLE_IMAGE = UPLOAD_ROOT + "articleImage/";

	public static String UPLOAD_PRODUCT_VIDEO = UPLOAD_ROOT + "productVideo/";

	public static String UPLOAD_ORDER_IMAGE = UPLOAD_ROOT + "orderImage/";
	
	public static String CONFIG_PATH = null;
	
	public static Properties config = new Properties();
	
	// 旧参数

    public final static String DB = "mcoolwap";

    public final static String DBServer = "localhost";

    public final static String DBUser = "root";

    public final static String DBPassword = "mcqaly";

    public final static String LOGIN_USER_KEY = "loginUser";

    public final static String ACTION_SUCCESS_KEY = "success";

    public final static String ACTION_FAILURE_KEY = "failure";

    public final static String SYSTEM_FAILURE_KEY = "systemFailure";

    /**/
      public static String APP_ROOT = WEB_ROOT + "/adult";
      
      public static String RESOURCE_ROOT_URL = WEB_ROOT + "/adult-admin2/rep";
      
      public static float[] PRICE_DELIVER_LINE = { 150, 300, 300 }; // 免邮费的底线
      
      public static float[] PRICE_DELIVER_ADD = { 10, 20, 15 }; // 邮费
      
      public static String RESOURCE_ARTICLE_IMAGE = "/wap/upload/articleImage/";

      public static String SHOP_NAME = "云雨堂";

      public static String UPLOAD_FORUM_IMAGE = UPLOAD_ROOT + "forumImage/";

      public static String RESOURCE_FORUM_IMAGE = "/wap/upload/forumImage/";

      public static String ORDER_IMAGE = UPLOAD_ROOT + "orderImage/";
    /**/ 
    /**
    public static String APP_ROOT = WEB_ROOT + "/man";

    public static String RESOURCE_ROOT_URL = WEB_ROOT + "/man-admin2/rep";
    
    public static String RESOURCE_ARTICLE_IMAGE = "/man/upload/articleImage/";

    public static float[] PRICE_DELIVER_LINE = { 255, 400, 300 }; // 免邮费的底线

    public static float[] PRICE_DELIVER_ADD = { 10, 20, 15 }; // 邮费

    public static String SHOP_NAME = "男人帮";
    
    public static String UPLOAD_ROOT = "E:/workspace/adult/upload/";

    public static String UPLOAD_FORUM_IMAGE = UPLOAD_ROOT + "forumImage/";

    public static String RESOURCE_FORUM_IMAGE = "/adult/upload/forumImage/";
    **/

//    public final static String URL_PREFIX = BaseAction.URL_PREFIX;

    public final static String JA_ICON_RESOURCE_ROOT_URL = RESOURCE_ROOT_URL
            + "/icon/";

    // 文章、产品、分类信息、树状页面的缓存组的KEY值
    public final static String CACHEKEY_CATALOGLIST = "catalogListCache";
    public final static String CACHEKEY_CATALOG = "catalogCache";
    public final static String CACHEKEY_PRODUCTLIST = "productListCache";
    public final static String CACHEKEY_PRODUCT = "productCache";
    public final static String CACHEKEY_ARTICLELIST = "articleListCache";
    public final static String CACHEKEY_ARTICLE = "articleCache";
    public final static String CACHEKEY_COLUMNLIST = "columnListCache";
    // 存放文章、产品、分类信息、树状页面 的数量的KEY值
    public final static String CACHEKEY_COMMON = "commonCache";

	public static final String CACHEKEY_VIDEOLIST = "videoList";
	public static final String CACHEKEY_VIDEO = "video";

	public static String RESOURCE_VIDEO_FILE = "/wap/upload/productVideo/";


    public final static int ONLINE_USER_PER_PAGE = 10;
    public final static int TEMPLATE_USER_ID = 138;
    public final static int MESSAGE_PER_PAGE = 10;
    
    public final static int BLOG_ARTICLE_PER_PAGE = 10;
    public final static int BLOG_IMAGE_PER_PAGE = 10;
    public final static int PGAME_PER_PAGE = 10;
    
    public final static int NEWS_WORD_PER_PAGE = 500;
    public final static int NEWS_PER_PAGE = 10;
    public final static int IMAGE_PER_PAGE = 5;
    public final static int BBS_ARTICLE_PER_PAGE = 10;    
    public final static int GUESTBOOK_ARTICLE_PER_PAGE = 10;
    
    
    public static String RESOURCE_ROOT = null;
    public static String ADMIN_QQ = null;
    public static String CONTEXT_PATH = null;
    public final static String NEWS_RESOURCE_ROOT_URL = RESOURCE_ROOT_URL + "news/";
    public final static String JA_RING_RESOURCE_ROOT_URL = RESOURCE_ROOT_URL + "joycoolAdmin/ring/";
    public static String IMAGE_RESOURCE_ROOT_URL = "../rep/image/";
    public static String EBOOK_RESOURCE_ROOT_URL = "../rep/ebook/";
    public final static String GAME_RESOURCE_ROOT_URL = RESOURCE_ROOT_URL + "game/";
    
    public final static String PGAME_RESOURCE_ROOT_URL = RESOURCE_ROOT_URL + "pgame/";
    
    public static String URL_CONTEXT_PREFIX = "/adult";
    
    public static String EBOOK_FILE_URL = "E:/workspace/adult/rep/ebook/";
   
    public final static String SPECIAL_MARK = "%JCSP%";
    public final static String SPECIAL_MONTH_MARK = "%JCSP%MONTH";
    public final static String SPECIAL_DAY_MARK = "%JCSP%DAY";
    public final static String SPECIAL_ONLINE_USER_COUNT_MARK = "%JCSP%ONLINE_USER_COUNT";
    public final static String SPECIAL_NEW_MESSAGE_COUNT_MARK = "%JCSP%NEW_MESSAGE_COUNT";
    public final static String SPECIAL_ONLINE_USER_NICKNAME_MARK = "%JCSP%ONLINE_USER_NICKNAME";
    public final static String SPECIAL_ONLINE_USER_NAME_MARK = "%JCSP%ONLINE_USER_NAME";
    public final static String SPECIAL_INDEX_SEARCH_MARK = "%JCSP%INDEX_SEARCH";


	public static final String JC_MID = "jc_mid";

	/**
	 * 区域代码<br/>
	 * 1. 北京
	 */
	public static int AREA_NO_BEIJING = 1;

	/**
	 * 区域代码<br/>
	 * 2. 广东（除广州以外）<br/>
	 */
	public static int AREA_NO_GUANGDONG = 2;

	/**
	 * 区域代码<br/>
	 * 3. 广州
	 */
	public static int AREA_NO_GUANGZHOU = 3;

	/**
	 * 区域代码<br/>
	 * 0. 其它<br/>
	 */
	public static int AREA_NO_QITA = 0;

	/**
	 * 购买方式<br/>
	 * 0. 货到付款<br/>
	 */
	public static int BUY_TYPE_HUODAOFUKUAN = 0;

	/**
	 * 购买方式<br/>
	 * 1. 邮购<br/>
	 * <br/>邮购方式已取消，现该数字代表钱包支付。<br/>
	 */
	public static int BUY_TYPE_YOUGOU = 1;

	/**
	 * 购买方式<br/>
	 * 2. 上门自取<br/>
	 */
	public static int BUY_TYPE_SHANGMENZIQU = 2;

	/**
	 * 购买方式<br/>
	 * 3. 换货订单<br/>
	 */
	public static int BUY_TYPE_NIFFER =3 ;
	
	/**
	 * 货到付款订单允许最小价格<br/>
	 */
	public static int minPrice = 98;

	/**
	 * 货到付款订单允许最大价格<br/>
	 */
	public static int maxPrice = 2000;

	/**
	 * 运费<br/>
	 * 不同地区，不同送货方式的运费
	 */
	public static int[][] postage = new int[10][5];

	/**
	 * 免运费的订单价格<br/>
	 * 不同地区，不同送货方式下，免运费的订单总价格
	 */
	public static int[][] noPostagePrice = new int[10][5];

	static{
		postage[AREA_NO_BEIJING][BUY_TYPE_HUODAOFUKUAN] = 10;
		postage[AREA_NO_BEIJING][BUY_TYPE_YOUGOU] = 0;
		postage[AREA_NO_BEIJING][BUY_TYPE_SHANGMENZIQU] = 0;
		postage[AREA_NO_GUANGDONG][BUY_TYPE_HUODAOFUKUAN] = 15;
		postage[AREA_NO_GUANGDONG][BUY_TYPE_YOUGOU] = 10;
		postage[AREA_NO_GUANGDONG][BUY_TYPE_SHANGMENZIQU] = 0;
		postage[AREA_NO_GUANGZHOU][BUY_TYPE_HUODAOFUKUAN] = 15;
		postage[AREA_NO_GUANGZHOU][BUY_TYPE_YOUGOU] = 0;
		postage[AREA_NO_GUANGZHOU][BUY_TYPE_SHANGMENZIQU] = 0;
		postage[AREA_NO_QITA][BUY_TYPE_HUODAOFUKUAN] = 25;
		postage[AREA_NO_QITA][BUY_TYPE_YOUGOU] = 10;
		postage[AREA_NO_QITA][BUY_TYPE_SHANGMENZIQU] = 10;

		noPostagePrice[AREA_NO_BEIJING][BUY_TYPE_HUODAOFUKUAN] = 0;
		noPostagePrice[AREA_NO_BEIJING][BUY_TYPE_YOUGOU] = 0;
		noPostagePrice[AREA_NO_BEIJING][BUY_TYPE_SHANGMENZIQU] = 0;
		noPostagePrice[AREA_NO_GUANGDONG][BUY_TYPE_HUODAOFUKUAN] = 0;
		noPostagePrice[AREA_NO_GUANGDONG][BUY_TYPE_YOUGOU] = 98;
		noPostagePrice[AREA_NO_GUANGDONG][BUY_TYPE_SHANGMENZIQU] = 0;
		noPostagePrice[AREA_NO_GUANGZHOU][BUY_TYPE_HUODAOFUKUAN] = 0;
		noPostagePrice[AREA_NO_GUANGZHOU][BUY_TYPE_YOUGOU] = 0;
		noPostagePrice[AREA_NO_GUANGZHOU][BUY_TYPE_SHANGMENZIQU] = 0;
		noPostagePrice[AREA_NO_QITA][BUY_TYPE_HUODAOFUKUAN] = 0;
		noPostagePrice[AREA_NO_QITA][BUY_TYPE_YOUGOU] = 98;
		noPostagePrice[AREA_NO_QITA][BUY_TYPE_SHANGMENZIQU] = 0;
	}
	

}