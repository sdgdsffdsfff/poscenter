<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
var ResourceTree = {};
var resourceList = {};

//返回到详情页面
resourceList.backDetail = function(){
	$('#resourceInfo').load($('#initPath').val()+'/pos/resource!toResourceDetailView.do', {id: ResourceTree.getSelectedItemId()});
}

$(document).ready(function() {
	ResourceTree = new dhtmlXTreeObject("treebox_tree", "100%", "100%", 0);
	ResourceTree.setSkin('dhx_skyblue');
	ResourceTree.setImagePath($('#initPath').val()+"/js/dhtmlxTree/imgs/");
	ResourceTree.enableDragAndDrop(0);
	ResourceTree.setOnClickHandler(function(id){
		$('#resourceInfo').load($('#initPath').val()+'/pos/resource!toResourceDetailView.do', {'id':id});
	});
	ResourceTree.setXMLAutoLoading($('#initPath').val()+"/pos/resource!getChildrenXmlById.do");
	ResourceTree.loadXML($('#initPath').val()+"/pos/resource!getChildrenXmlById.do");

	//新增子节点
	$('#addresourceBut').click(function(){
		var id = ResourceTree.getSelectedItemId();
		if(id==null || id==''){
			alert('请选择要新增的节点！'); return ;
		}
		var data = {};
		if(id != '-1'){
			data.parentId = id;
		}

		//获取节点的层级数
		data.treeLevel = ResourceTree.getLevel(id);

		$('#resourceInfo').load($('#initPath').val()+'/pos/resource!toResourceFormView.do', data);
	});

	//修改
	$('#updateresourceBut').click(function(){
		var id = ResourceTree.getSelectedItemId();
		if(id==null || id==''){
			alert('请选择要修改的节点！'); return ;
		}else if(id == '-1'){
			alert('该节点不能修改！'); return ;
		}
		$('#resourceInfo').load($('#initPath').val()+'/pos/resource!toResourceFormView.do', {id:id});
	});
	
	//删除
	$('#deleteresourceBut').click(function(){
		var id = ResourceTree.getSelectedItemId();
		if(id==null || id==''){
			alert('请选择要删除的节点！'); return ;
		}
		var msg = '确认删除该节点及其子节点吗？';
		if(id == '-1'){
			msg = '确认删除所有资源节点吗？';
		}
		if(confirm(msg)){
			$.get($('#initPath').val()+'/pos/resource!removeAll.do', {id: (id=='-1'?'':id)}, function(json){
				if(json == 'true'){
					if(id != '-1'){
						var parentId = ResourceTree.getParentId(id);
						//选中父节点
						ResourceTree.selectItem(parentId);
						//刷新树节点
						ResourceTree.refreshItem(parentId);
						//刷新表单域
						$('#resourceInfo').load($('#initPath').val()+'/pos/resource!toResourceDetailView.do', {id: parentId});
					}else{
						//刷新树节点
						ResourceTree.refreshItem('-1');
					}
				}else{
					alert(json);
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
	<div class="rpos">当前位置: 资源管理 - 列表</div>
	<form class="ropt">
		<input type="button" id="addresourceBut" class="submit" value="新增" /> &nbsp; 
		<input type="button" id="updateresourceBut" class="reset" value="修改" /> &nbsp; 
		<input type="button" id="deleteresourceBut" class="del-button" value="删除" /> &nbsp; 
	</form>
	<div class="clear"></div>
</div>
 
<div class="body-box" style="position:absolute; top:35px; bottom:8px;">
	<div style="width:20%; border: 1px solid #C8DCF0; float:left; margin:5px;">
		<div id="treebox_tree"></div>
	</div>
	<div id="resourceInfo" style="float: left; width:79%;"></div>
	<div class="clear"></div>
</div>
</body>
</html>