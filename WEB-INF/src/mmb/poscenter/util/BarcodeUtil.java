package mmb.poscenter.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import mmboa.util.Constants;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.jbarcode.JBarcode;
import org.jbarcode.encode.EAN13Encoder;
import org.jbarcode.encode.InvalidAtributeException;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.WidthCodedPainter;
import org.jbarcode.util.ImageUtil;

public class BarcodeUtil {

	/**
	 * 根据商品编码和流水号生成条形码
	 * <br/>欧洲商品条码(=European Article Number)
	 * @param goodsCode 商品编码和流水号
	 * @return 条形码
	 * @throws Exception 
	 */
	public static String createEAN13Barcode(String goodsCode) throws Exception {
		return goodsCode + EAN13Encoder.getInstance().computeCheckSum(goodsCode);
	}
	
	/**
	 * 根据商品条形码生成条形码图片，并返回图片相对路径
	 * <br/>欧洲商品条码(=European Article Number)
	 * @param barcode 商品条形码
	 * @return 条形码图片相对路径
	 * @throws Exception 
	 */
	public static String createEAN13BarcodeImg(String barcode) throws Exception {
		String path = null;
		FileOutputStream fos = null;
		
		try {
			//商品条码图片存放路径
			String relativePath = Constants.config.getProperty("barcodeImgPath") + barcode + ".gif";
			
			//创建图片
			JBarcode localJBarcode = new JBarcode(EAN13Encoder.getInstance(), WidthCodedPainter.getInstance(), EAN13TextPainter.getInstance());
			BufferedImage localBufferedImage = localJBarcode.createBarcode(barcode.substring(0, 12)); //"693466508769"; //693676288005
			
			//保存条形码图片
			File barcodeImg = new File(ServletActionContext.getServletContext().getRealPath("/") + relativePath);
			if(!barcodeImg.getParentFile().exists()) {
				barcodeImg.getParentFile().mkdirs();
			}
			fos = new FileOutputStream(barcodeImg);
			ImageUtil.encodeAndWrite(localBufferedImage, "gif", fos, 96, 96);
			fos.close();
			
			path = relativePath;
		} catch (Exception e) {
			throw new Exception("生成条形码图片时出现异常！");
		} finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return path;
	}
	
	/**
	 * 验证商品条形码是否有效
	 * @param barcode 商品条形码
	 * @return true:有效；false:无效
	 */
	public static boolean validateEAN13Barcode(String barcode) {
		boolean isValid = true;
		
		//字符长度判断
		if(StringUtils.isBlank(barcode) || barcode.length()!=13) {
			isValid = false;
		}
		//判断检验码是否有效
		else {
			try {
				String lastChar = barcode.substring(12);
				String checkCode = EAN13Encoder.getInstance().computeCheckSum(barcode.substring(0, 12));
				isValid = lastChar.equals(checkCode);
			} catch (InvalidAtributeException e) {
				isValid = false;
			}
		}
		
		return isValid;
	}

	/**
	 * 根据条形码获取条形码图片路径（未生成时返回null）
	 * @param barcode 商品条形码
	 * @return
	 */
	public static String getBarcodeImgPath(String barcode) {
		String path = null;
		
		//商品条码图片存放路径
		String relativePath = Constants.config.getProperty("barcodeImgPath") + barcode + ".gif";
		
		File barcodeImgFile = new File(ServletActionContext.getServletContext().getRealPath("/") + relativePath);
		if(barcodeImgFile.exists()) {
			path = relativePath;
		}
		
		return path;
	}
	
}
