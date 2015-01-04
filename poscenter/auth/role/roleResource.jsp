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
var roleResourceTree = {};
var role = {};

//返回角色列表
role.backList = function(){
   window.location.href = $('#initPath').val()+'/pos/role!roleList.do';
}

//提交表单
role.sumbitForm = function(){
	$.ajax({
			type: 'POST',
			url:$('#initPath').val()+'/pos/role!saveRoleResource.do',
			data:{roleId:$('#roleId').val(),resIds:roleResourceTree.getAllCheckedBranches()},
			success: function(json){
				if(json == 'success'){
					alert('保存成功！');
					window.location.href = $('#initPath').val()+'/pos/role!roleList.do';
				}else{
					alert(json);
				}
			},
			error: function(msg){
				alert(msg);
			}
	});
}

$(document).ready(function() {
	roleResourceTree = new dhtmlXTreeObject("treebox_tree", "100%", "100%", 0);
	roleResourceTree.setSkin('dhx_skyblue');
	roleResourceTree.setImagePath($('#initPath').val()+"/js/dhtmlxTree/imgs/");
	roleResourceTree.enableDragAndDrop(0);
    roleResourceTree.enableCheckBoxes(true);
    roleResourceTree.loadXML($('#initPath').val()+"/pos/resource!getAllTreeXmlByRole.do?roleId=${roleId}", function(){
        roleResourceTree.openAllItems(-1);
		roleResourceTree.enableCheckBoxes(true);
		roleResourceTree.enableThreeStateCheckboxes(1);
	});
});
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 角色管理 - 分配权限</div>
	<div class="clear"></div>
</div>
 
<div class="body-box" style="">
	<div style="margin-top:5px; margin-right:5px;" >
		<input type="hidden" id="roleId" name="roleId" value="${roleId}"></input>
		<input type="button" value="提交" onclick="role.sumbitForm();" class="submit" ></input>&nbsp;
		<input type="button" value="返回" onclick="role.backList();" class="return-button" ></input>
	</div>
	<div style="position:absolute; top:65px; bottom:8px; width:300px; border: 1px solid #C8DCF0; margin:1px;">
		<div id="treebox_tree"></div>
	</div>
</div>
</body>
</html>