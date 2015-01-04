<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>product detail</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/lodop/LodopFuncs.js"></script>
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop.exe"></embed>
</object> 
<script type="text/javascript">
var product={};
var LODOP;
//打印商品条码
product.printBarcode = function(){
		var reg=new RegExp("^\\d{1,3}$");
		//cssStyle = "<style>div{border:0;}body{font-size:12px;}</style>";
		LODOP = getLodop(document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
		LODOP.PRINT_INIT("产品条码");
		LODOP.SET_PRINT_PAGESIZE(1,"152px","45px","");
		LODOP.SET_PRINT_STYLE("FontSize",5);
		var printNum = document.getElementById("printNum");
		if(printNum.value=="" || printNum.value<=0){
			alert("打印次数不能为空且必须大于0");
			printNum.focus();
			return ;
		}else if(!reg.test(printNum.value)){
			alert("打印次数只能是1-100的整数");
			printNum.focus();
			return ;
		}else if(printNum.value>100){
			alert("打印次数不能大于100");
			printNum.focus();
			return ;
		}
		var num=41;
//		var num1 = printNum.value/2;
		var barcodeWidth=34;
		var tmpbarcode = "<%=request.getAttribute("barCode")%>";
		var tmpcode = "<%=request.getAttribute("name")%>"+" ";
		//var tmpcode = "笔记本电脑联想发生的三星手机随碟附送";
		//var tmporiname = "<%=request.getParameter("oriname")==null?"":request.getParameter("oriname").replace("\"","\"+\"\\\"\"+\"") %>";
		//if(tmpbarcode.length==13){barcodeWidth+=2.2;}
		//else if(tmpbarcode.length==14){barcodeWidth+=4.5;}
		//else if(tmpbarcode.length==15){barcodeWidth+=9;}
		//if(tmporiname.length>26){
		//	tmporiname = tmporiname.substring(0,26);
		//}
		var num1 = printNum.value;
		num1= Math.round(num1);
		LODOP.SET_PRINTER_INDEX(-1);
		if(printNum.value<=1){
			//LODOP.ADD_PRINT_TEXT("13mm","2mm","95%","5.3mm",tmpcode+tmporiname);
			//LODOP.ADD_PRINT_BARCODE("18mm","2mm",barcodeWidth+"mm","16.9mm","128A",tmpbarcode);
			LODOP.ADD_PRINT_TEXT("5px","19px","95%","10px",tmpcode);
			LODOP.SET_PRINT_STYLEA(0,"FontSize",5);
			LODOP.SET_PRINT_STYLEA(0,"Bold",1);
			if(tmpbarcode.length==13){
				LODOP.ADD_PRINT_BARCODE("16px","19px",barcodeWidth+"mm","30px","EAN13",tmpbarcode);
			}else{
				LODOP.ADD_PRINT_BARCODE("16px","19px",barcodeWidth+"mm","30px","2_5interleaved",tmpbarcode);
			}
			
		//	LODOP.ADD_PRINT_HTML(0,0,"44mm","30mm",cssStyle+document.getElementById("barcodeDiv").innerHTML);
		}else{
			for(var i=0;i<num1;i+=1){
				<%--	if(i==num1-1 && printNum.value%2!=0){
					//LODOP.ADD_PRINT_HTML(parseInt(num*(i))+"mm",0,"44mm","30mm",cssStyle+document.getElementById("barcodeDiv").innerHTML);
					LODOP.ADD_PRINT_TEXT(parseInt(num*(i)+15)+"mm","2mm","40mm","5.3mm","+tmpcode+tmporiname+");
					LODOP.ADD_PRINT_BARCODE(parseInt(num*(i)+20)+"mm","2mm",barcodeWidth+"mm","16.9mm","128A","+tmpbarcode+");
				}else{				
					//alert(i);
					//LODOP.ADD_PRINT_HTML(parseInt(num*(i))+"mm",0,"44mm","30mm",cssStyle+document.getElementById("barcodeDiv").innerHTML);
					//LODOP.ADD_PRINT_HTML(parseInt(num*(i))+"mm","45mm","44mm","30mm",cssStyle+document.getElementById("barcodeDiv").innerHTML);
				LODOP.ADD_PRINT_TEXT(parseInt(num*(i)+(i%2==0?15:16))+"mm","5.8mm","40mm","5.3mm","<%=request.getParameter("code")+" "%><%=request.getParameter("oriname") %>");
	//				LODOP.ADD_PRINT_BARCODE(parseInt(num*(i)+(i%2==0?20:21))+"mm","3.2mm","46.4mm","16.9mm","128A","<%=request.getParameter("barcode") %>");
		//			LODOP.ADD_PRINT_TEXT(parseInt(num*(i)+(i%2==0?15:16))+"mm","55.4mm","40mm","5.3mm","<%=request.getParameter("code")+" "%><%=request.getParameter("oriname") %>");
			//		LODOP.ADD_PRINT_BARCODE(parseInt(num*(i)+(i%2==0?20:21))+"mm","53.4mm","46.4mm","16.9mm","128A","<%=request.getParameter("barcode") %>");
					}--%>
					//LODOP.ADD_PRINT_TEXT(num*(i)+(i%15>00?13:12.9)+"mm","2mm","95%","5.3mm",tmpcode+tmporiname);
					//LODOP.ADD_PRINT_BARCODE(num*(i)+(i%15>0?18:17.9)+"mm","2mm",barcodeWidth+"mm","16.9mm","128A",tmpbarcode);
					//LODOP.ADD_PRINT_TEXT("13mm","2mm","95%","5.3mm",tmpcode+tmporiname);
					//LODOP.ADD_PRINT_BARCODE("18mm","2mm",barcodeWidth+"mm","16.9mm","128A",tmpbarcode);
					LODOP.ADD_PRINT_TEXT("5px","19px","95%","10px",tmpcode);
					LODOP.SET_PRINT_STYLEA(0,"FontSize",5);
					LODOP.SET_PRINT_STYLEA(0,"Bold",1);
					if(tmpbarcode.length==13){
						LODOP.ADD_PRINT_BARCODE("16px","19px",barcodeWidth+"mm","30px","EAN13",tmpbarcode);
					}else{
						LODOP.ADD_PRINT_BARCODE("16px","19px",barcodeWidth+"mm","30px","2_5interleaved",tmpbarcode);
					}
					LODOP.NEWPAGE();
					
			}
		}
		LODOP.PRINTB();
	
}

//返回列表
product.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/Product!productList.do';
}
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 商品管理 - 商品条形码</div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
<tbody>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">条形码图片：</td>
		<td width="88%" class="pn-fcontent">
			<c:if test="${barcodeImgPath != null}">
			<img alt="条形码" src="<%=request.getContextPath()%>/${barcodeImgPath}">
			</c:if>
			<c:if test="${barcodeImgPath == null}">
			${message }
			</c:if>
		</td>
	</tr>
    <tr>
    	<td class="pn-flabel pn-flabel-h">打印次数：</td>
		<td class="pn-fcontent"><input type="text" name="printNum" id="printNum" maxlength="3" size="3"/> &nbsp;注：一次最多允许打印100次</td>
    </tr>
	<tr>
		<td align="center" colspan="2" class="pn-fcontent">
			<c:if test="${barcodeImgPath != null}">
			<input type="button" value="打印" onclick="product.printBarcode();" class="generate-static" />
			</c:if>
			<input type="button" value="返回" onclick="product.backList();" class="return-button" />
		</td>
	</tr>
</tbody>
</table>
</body>
</html>