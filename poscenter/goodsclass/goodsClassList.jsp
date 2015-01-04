<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.metadata.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/dhtmlxTree/dhtmlxtree.css" />

<script src="<%=request.getContextPath()%>/js/dhtmlxTree/dhtmlxcommon.js"></script>
<script src="<%=request.getContextPath()%>/js/dhtmlxTree/dhtmlxtree.js"></script>

<script>
var GoodsClassTree = {};
$(document).ready(function() {
	GoodsClassTree = new dhtmlXTreeObject("treebox_tree", "100%", "100%", 0);
	GoodsClassTree.setSkin('dhx_skyblue');
	GoodsClassTree.setImagePath($('#initPath').val()+"/js/dhtmlxTree/imgs/");
	GoodsClassTree.enableDragAndDrop(0);
	GoodsClassTree.setOnClickHandler(function(id){
		$('#goodsClassInfo').load($('#initPath').val()+'/pos/goodsClass!toGoodsClassDetailView.do', {'id':id});

		//暂时规定商品分类最多为三级
		var treeLevel = GoodsClassTree.getLevel(id);
		if(treeLevel < 4) {
			$('#addGoodsClassBut').show();
		}else{
			$('#addGoodsClassBut').hide();
		}
	});
	GoodsClassTree.setXMLAutoLoading($('#initPath').val()+"/pos/goodsClass!getChildrenXmlById.do");
	GoodsClassTree.loadXML($('#initPath').val()+"/pos/goodsClass!getChildrenXmlById.do");

	//新增子节点
	$('#addGoodsClassBut').click(function(){
		var id = GoodsClassTree.getSelectedItemId();
		if(id==null || id==''){
			alert('请选择要新增的节点！'); return ;
		}
		var data = {};
		if(id != '-1'){
			data.parentId = id;
		}

		//获取节点的层级数
		data.treeLevel = GoodsClassTree.getLevel(id);

		$('#goodsClassInfo').load($('#initPath').val()+'/pos/goodsClass!toGoodsClassFormView.do', data);
	});

	//修改
	$('#updateGoodsClassBut').click(function(){
		var id = GoodsClassTree.getSelectedItemId();
		if(id==null || id==''){
			alert('请选择要修改的节点！'); return ;
		}else if(id == '-1'){
			alert('该节点不能修改！'); return ;
		}
		$('#goodsClassInfo').load($('#initPath').val()+'/pos/goodsClass!toGoodsClassFormView.do', {id:id});
	});
	
	//删除
	$('#deleteGoodsClassBut').click(function(){
		var id = GoodsClassTree.getSelectedItemId();
		if(id==null || id==''){
			alert('请选择要删除的节点！'); return ;
		}
		var msg = '确认删除该节点及其子节点吗？';
		if(id == '-1'){
			msg = '确认删除所有商品分类节点吗？';
		}
		if(confirm(msg)){
			$.getJSON($('#initPath').val()+'/pos/goodsClass!removeAll.do', {id: (id=='-1'?'':id)}, function(json){
				if(json){
					if(id != '-1'){
						var parentId = GoodsClassTree.getParentId(id);
						//选中父节点
						GoodsClassTree.selectItem(parentId);
						//刷新树节点
						GoodsClassTree.refreshItem(parentId);
						//刷新表单域
						$('#goodsClassInfo').load($('#initPath').val()+'/pos/goodsClass!toGoodsClassDetailView.do', {id: parentId});
					}else{
						//刷新树节点
						GoodsClassTree.refreshItem('-1');
					}
				}
			});
		}
	});
	
});
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 商品分类管理 - 列表</div>
	<form class="ropt">
		<auth:authorize ifGranted="addGoodsClass">
		<input type="button" id="addGoodsClassBut" class="submit" value="新增" /> &nbsp; 
		</auth:authorize>
		<auth:authorize ifGranted="updateGoodsClass">
		<input type="button" id="updateGoodsClassBut" class="reset" value="修改" /> &nbsp;
		</auth:authorize>
		<auth:authorize ifGranted="deleteGoodsClass"> 
		<input type="button" id="deleteGoodsClassBut" class="del-button" value="删除" /> &nbsp; 
		</auth:authorize>
	</form>
	<div class="clear"></div>
</div>
 
<div class="body-box" style="position:absolute; top:35px; bottom:8px;">
	<div style="width:20%; border: 1px solid #C8DCF0; float:left; margin:5px;">
		<div id="treebox_tree"></div>
	</div>
	<div id="goodsClassInfo" style="float: left; width:79%;"></div>
</div>
</body>
</html>