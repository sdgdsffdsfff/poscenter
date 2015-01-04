<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
var SearchTree = {};

//点击节点
SearchTree.clickNode = function(id){
	if(id == '-1'){return ;}
	//单选，自动关闭窗口
	if($('#_isCheckBox').val()==null || $('#_isCheckBox').val()==''){
		//只能选子节点
		if($('#_childNodeOnly').val()=='true' && SearchTree.tree.hasChildren(id)){
			return ;
		}
		SearchTree.returnValue(id, SearchTree.tree.getItemText(id)); //设置回填值
	}
	
}

//设置回填值
SearchTree.returnValue = function(id, name){
	var _ID = $('#_ID').val();
	if(_ID!=null && _ID!=''){
		$('#'+_ID).val(id);
	}
	var _NAME = $('#_NAME').val();
	if(_NAME!=null && _NAME!=''){
		$('#'+_NAME).val(name);
	}

	$('a.jMsgbox-closeWrap').click(); //关闭弹出框
}

//把非叶子节点设置为不可选状态
SearchTree.unableCheckIfNotLeaf = function(objId){
	$.each(SearchTree.tree.getAllSubItems(objId).split(','), function(index,obj){
		if(SearchTree.tree.hasChildren(obj) != 0){
			SearchTree.tree.disableCheckbox(obj, 1);
		}
	})
	
}

$(document).ready(function() {
	var _className = $('#_className').val();
	var _isCheckBox = $('#_isCheckBox').val();
	var _childNodeOnly = $('#_childNodeOnly').val();

	SearchTree.tree = new dhtmlXTreeObject("dhtmltree_box", "100%", "100%", 0);
	SearchTree.tree.setSkin('dhx_skyblue');
	SearchTree.tree.setImagePath($('#initPath').val()+"/js/dhtmlxTree/imgs/");
	SearchTree.tree.enableDragAndDrop(0);
	SearchTree.tree.setOnClickHandler(function(id){
		SearchTree.clickNode(id);
	});
	SearchTree.tree.setXMLAutoLoading($('#initPath').val()+"/pos/goodsClass!getChildrenXmlById.do");
	SearchTree.tree.loadXML($('#initPath').val()+"/pos/goodsClass!getChildrenXmlById.do", function(){
		//只能选子节点
		if(_childNodeOnly=='true' && _isCheckBox=='true'){
			SearchTree.unableCheckIfNotLeaf(-1);
			SearchTree.tree.disableCheckbox(-1, 1);
		}
	});

	//只能选子节点
	if(_childNodeOnly=='true' && _isCheckBox=='true'){
		SearchTree.tree.setOnOpenEndHandler(function(id){
			SearchTree.unableCheckIfNotLeaf(id);
		});
		
	}
	
	//多选
	if(_isCheckBox!=null && _isCheckBox!=''){
		SearchTree.tree.enableCheckBoxes(true);
	}

	//确定
	$('input[name=OK_BUTTON]').click(function(){
		var ids = SearchTree.tree.getAllChecked();
		var idsArray = ids.split(",");
		
		//未选中数据
		if(ids==null || ids=="" || idsArray.length<=0){alert("请选择相关数据!"); return;}
		
		//循环获取选中节点的名称
		var names = '';
		for(var i=0; i<idsArray.length; i++){
			//根节点
			if(idsArray[i]=="-1"){ continue; }

			names += (i!=0 ? ',' : '') + SearchTree.tree.getItemText(idsArray[i]);
		}
		SearchTree.returnValue(ids, names); //设置回填值
	});
} );
</script>

<input type="hidden" id="_className" value='<c:out value="${param.className}"/>' /><!-- 类名 -->
<input type="hidden" id="_isCheckBox" value="<c:out value="${param.isCheckBox}"/>"/><!-- 显示复选框，并表示为多选 -->
<input type="hidden" id="_childNodeOnly" value="<c:out value="${param.childNodeOnly}"/>"/><!-- 只能选择叶子节点 -->
<input type="hidden" id="_ID" value="<c:out value="${param.IDS}"/>"/><!-- 存放回填ID -->
<input type="hidden" id="_NAME" value="<c:out value="${param.NAMES}"/>"/><!-- 存放回填Name -->

<div style="text-align: center;">
	<input type="button" name="OK_BUTTON" value="确定" />
</div>

<div style="width:100%; border: 1px solid #C8DCF0;">
	<div id="dhtmltree_box"></div>
</div>

<div style="text-align: center; display: none;">
	<input type="button" name="OK_BUTTON" value="确定" />
</div>
