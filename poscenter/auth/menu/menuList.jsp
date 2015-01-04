<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>菜单管理</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/dhtmlxTree/dhtmlxtree.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.metadata.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>

<script src="<%=request.getContextPath()%>/js/dhtmlxTree/dhtmlxcommon.js"></script>
<script src="<%=request.getContextPath()%>/js/dhtmlxTree/dhtmlxtree.js"></script>

<script>
var MenuTree = {};
var menuList = {};

//返回到详情页面
menuList.backDetail = function(){
	$('#menuInfo').load($('#initPath').val()+'/pos/menu!toMenuDetailView.do', {id: MenuTree.getSelectedItemId()});
}

$(document).ready(function() {
	MenuTree = new dhtmlXTreeObject("treebox_tree", "100%", "100%", 0);
	MenuTree.setSkin('dhx_skyblue');
	MenuTree.setImagePath($('#initPath').val()+"/js/dhtmlxTree/imgs/");
	MenuTree.enableDragAndDrop(0);
	MenuTree.setOnClickHandler(function(id){
		$('#menuInfo').load($('#initPath').val()+'/pos/menu!toMenuDetailView.do', {'id':id});

		//暂时规定菜单最多为二级
		var treeLevel = MenuTree.getLevel(id);
		if(treeLevel < 3) {
			$('#addMenuBut').show();
		}else{
			$('#addMenuBut').hide();
		}
	});
	MenuTree.setXMLAutoLoading($('#initPath').val()+"/pos/menu!getChildrenXmlById.do");
	MenuTree.loadXML($('#initPath').val()+"/pos/menu!getChildrenXmlById.do");

	//新增子节点
	$('#addMenuBut').click(function(){
		var id = MenuTree.getSelectedItemId();
		if(id==null || id==''){
			alert('请选择要新增的节点！'); return ;
		}
		var data = {};
		if(id != '-1'){
			data.parentId = id;
		}

		//获取节点的层级数
		data.treeLevel = MenuTree.getLevel(id);

		$('#menuInfo').load($('#initPath').val()+'/pos/menu!toMenuFormView.do', data);
	});

	//修改
	$('#updateMenuBut').click(function(){
		var id = MenuTree.getSelectedItemId();
		if(id==null || id==''){
			alert('请选择要修改的节点！'); return ;
		}else if(id == '-1'){
			alert('该节点不能修改！'); return ;
		}
		$('#menuInfo').load($('#initPath').val()+'/pos/menu!toMenuFormView.do', {id:id});
	});
	
	//删除
	$('#deleteMenuBut').click(function(){
		var id = MenuTree.getSelectedItemId();
		if(id==null || id==''){
			alert('请选择要删除的节点！'); return ;
		}
		var msg = '确认删除该节点及其子节点吗？';
		if(id == '-1'){
			msg = '确认删除所有菜单节点吗？';
		}
		if(confirm(msg)){
			$.getJSON($('#initPath').val()+'/pos/menu!removeAll.do', {id: (id=='-1'?'':id)}, function(json){
				if(json){
					if(id != '-1'){
						var parentId = MenuTree.getParentId(id);
						//选中父节点
						MenuTree.selectItem(parentId);
						//刷新树节点
						MenuTree.refreshItem(parentId);
						//刷新表单域
						$('#menuInfo').load($('#initPath').val()+'/pos/menu!toMenuDetailView.do', {id: parentId});
					}else{
						//刷新树节点
						MenuTree.refreshItem('-1');
					}
				}
			});
		}
	});

});
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 菜单管理 - 列表</div>
	<form class="ropt">
		<input type="button" id="addMenuBut" class="submit" value="新增" /> &nbsp; 
		<input type="button" id="updateMenuBut" class="reset" value="修改" /> &nbsp; 
		<input type="button" id="deleteMenuBut" class="del-button" value="删除" /> &nbsp; 
	</form>
	<div class="clear"></div>
</div>
 
<div class="body-box" style="position:absolute; top:35px; bottom:8px;">
	<div style="width:20%; border: 1px solid #C8DCF0; float:left; margin:5px;">
		<div id="treebox_tree"></div>
	</div>
	<div id="menuInfo" style="float: left; width:79%;"></div>
</div>
</body>
</html>