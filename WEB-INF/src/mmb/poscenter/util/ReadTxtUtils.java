package mmb.poscenter.util;

import java.io.BufferedReader;    
import java.io.File;    
import java.io.FileInputStream;    
import java.io.InputStreamReader;    
import java.util.ArrayList;
import java.util.List;


import mmb.poscenter.service.MemberService;

import org.apache.log4j.Logger;
   
/**   
 * 读取TXT数据     
 */  
public class ReadTxtUtils {   
	
	private static Logger log = Logger.getLogger(ReadTxtUtils.class);
	
	/**
	 * 读取txt文件
	 * @param path 文件路径
	 * @param encoding 编码
	 * @return
	 */
	public static List<String> readTxtToList(String path,String encoding){
		List<String> tmp = new ArrayList<String>();
		try {    
            if(encoding == null || "".equals(encoding)){
            	encoding = "UTF-8";
            }  
            File file = new File(path);    
            if (file.isFile() && file.exists()) {    
               InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);  
               BufferedReader bufferedReader = new BufferedReader(read);    
               String lineTXT = null;    
               while ((lineTXT = bufferedReader.readLine()) != null) {
            	    tmp.add(lineTXT.toString().trim());
                }    
               read.close();  
            }else{   
            	log.error("找不到指定的文件！");
           }    
       } catch (Exception e) {
            log.error("读取文件内容操作出错", e);
       }   
       return tmp;
	}
	
	/**
	 * 读取txt文件 
	 * @param file 文件流 
	 * @param encoding 编码
	 * @return
	 */
	public static List<String> readTxtToList(File file,String encoding){
		List<String> tmp = new ArrayList<String>();
		try {    
            if(encoding == null || "".equals(encoding)){
            	encoding = "UTF-8";
            }  
         
            if (file.isFile() && file.exists()) {    
               InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);  
               BufferedReader bufferedReader = new BufferedReader(read);    
               String lineTXT = null;    
               while ((lineTXT = bufferedReader.readLine()) != null) {
            	    tmp.add(lineTXT.toString().trim());
                }    
               read.close();  
            }else{   
            	log.error("找不到指定的文件！");
           }    
       } catch (Exception e) {
            log.error("读取文件内容操作出错", e);
       }   
       return tmp;
	}
	
	
	
	public  static void main(String[] args){
		List<String> list = readTxtToList("C:\\aa.txt","GBK");
		MemberService ms = new MemberService();
	    ms.batInsertMemberInfo(list);
		System.out.println(list);
	}
}