<%@ page contentType="text/html; charset=utf-8" language="java" pageEncoding="utf-8"%>
<%@ include file="include.inc.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>基本表格模板</title>
<!--框架必需start-->
<script type="text/javascript" src="libs/js/jquery.js"></script>
<script type="text/javascript" src="libs/js/language/cn.js"></script>
<script type="text/javascript" src="libs/js/framework.js"></script>
<link href="libs/css/import_basic.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" id="skin" prePath=""/>
<link rel="stylesheet" type="text/css" id="customSkin"/>
<!--框架必需end-->
<script type="text/javascript" src="ueditorup/ueditor.config.js"></script>
<script type="text/javascript" src="ueditorup/ueditor.all.js"></script>
<link href="libs/css/import_basic.css" rel="stylesheet" type="text/css"/>

<!--数据表格start-->
<script src="libs/js/table/quiGrid.js" type="text/javascript"></script>
<!--数据表格end-->
<!--树组件与弹窗组件start -->
<script type="text/javascript" src="libs/js/tree/ztree/ztree.js"></script>
<link href="libs/js/tree/ztree/ztree.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="libs/js/popup/drag.js"></script>
<script type="text/javascript" src="libs/js/popup/dialog.js"></script>
<!--树组件与弹窗组件end -->
<!-- 树形下拉框start -->
<script type="text/javascript" src="libs/js/form/selectTree.js"></script>
<!-- 树形下拉框end -->
<!--表单异步提交start-->
<script src="libs/js/form/form.js" type="text/javascript"></script>
<!--表单异步提交end-->
<!-- 条件过滤器 start -->
<script type="text/javascript" src="libs/js/form/filter.js"></script>
<!-- 条件过滤器 end -->

<!--箭头分页start-->
<script type="text/javascript" src="libs/js/nav/pageArrow.js"></script>
<!--箭头分页end-->

</head>
<body>
<div class="box2" panelTitle="查询用户" id="searchPanel">
	<form action="user/list" id="queryForm" method="post">
	<input type="hidden" id="deptId" name="deptId" />
	<input type="hidden" name="bm" id="bm"/>
	<input type="hidden" name="page" id="page"/>
	<input type="hidden" name="pageSize" id="pageSize"/>
		<table>
			<tr>
				<td>用户名：</td>
				<td><input type="text" id="name" name="username" /></td>
				 <td>&nbsp;&nbsp;<button type="button" onclick="searchHandler()"><span class="icon_find">查询</span></button></td>
				<td><button type="button" onclick="resetSearch()"><span class="icon_reload">重置</span></button></td>
			</tr>
		</table>
	</form>
</div>

<div id="scrollContent">
	<div class="padding_right5">
		<div id="dataBasic"></div>
    </div>
</div>		

<script type="text/javascript">
	$(function(){
	     if(${sessionScope.allcounts!=null }&&${sessionScope.allcounts!=null }){
	    			var url="<%=basePath%>system/progressbar.jsp";
	         		parent.openlayer2(url,510,50);
	    		}
	});
	
	//实例化编辑器
	var ue = UE.getEditor('editor');
	//弹出文件上传的对话框
	function upFiles(id) {
		var myFiles = ue.getDialog("attachment");
		myFiles.open();
	}

	ue.ready(function() {
		    //隐藏编辑器，因为不会用到这个编辑器实例，所以要隐藏
			ue.hide();
			//设置编辑器不可用
			ue.setDisabled();
			//侦听文件上传，取上传文件列表中第一个上传的文件的路径
			ue.addListener('afterUpfile', function(t, arg) {
			    var fileurl=ue.options.filePath + arg[0].url;
			    alert(fileurl);
				$("#file").val(ue.options.filePath + arg[0].url);
				});
		});

//设定不可编辑的节点id
var noeditArray=["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19"];
	
var selectionSetting2 = {//树属性配置
	check: { enable: true}};
//grid
var grid = null;
var firstDialog = new parent.Dialog(); //声明弹窗

function initComplete(){
	initGrid();
}
function clickOrgidNode(bm){//点击机构节点触发函数 
	$("#bm").val(bm);
	searchHandler();
}
function setDept(){
	var deptId=$("#selectTree1").attr("relValue");
	$("#deptId").val(deptId);
	//searchHandler();
}
function initGrid(){
	grid = $("#dataBasic").quiGrid({
		columns:[
			{ display: '用户名', name: 'username',     align: 'left',width:"12%"},
		    { display: '类型', name: 'type', 	 align: 'left',width:"12%",
		    	 render: function (rowdata, rowindex, value, column){
				 	var name=["","系统用户","手机用户"];
              	    return name[value];
	                 }},
       		{ display: '操作', isAllowHide: false, align: 'center', width:"100",
					 render: function (rowdata, rowindex, value, column){
                 	    return '<div class="padding_top4 padding_left5">'
                 	             + '<span class="img_edit hand" title="修改" onclick="onEdit(' + rowdata.id + ')"></span>' 
                                 + '<span class="img_delete hand" title="删除" onclick="onDelete(' + rowdata.id+ ')"></span>'
                                 + '<span class="img_key hand" title="重置密码" onclick="onPwd(' + rowdata.id + ')"></span>' 
                             + '</div>';
	                 }
	        }
		  ],
	 url:'system/user_list.do', sortName: 'id',sortOrder:'desc',rownumbers:true,checkbox:true,
     height: '100%', width:"100%",percentWidthMode:true,params: $("#queryForm").formToArray(),
     usePager:true,page:1,pageSize:10,
     toolbar:{
    	 items:[
    		  {text: '新增', click: addUnit,    iconClass: 'icon_add'},
    		  { line : true },
    		  {text: '上传', click: upFiles,    iconClass: 'icon_export'},
    		  { line : true },
    		  {text: '批量删除', click: deleteUnit, iconClass: 'icon_delete'},
    		  { line: true }, 
              { text: '批量重置密码', click: resetUser, iconClass: 'icon_key' }
    		]
     	},
	});
	
	//监听查询框的回车事件
	 $("#searchInput").keydown(function(event){
	 	if(event.keyCode==13){
			searchHandler();
		}
	 });
	 
	 //box2收缩时，表格高度自适应
	 $("#searchPanel").bind("stateChange",function(e,state){
		grid.resetHeight();
	});
}
//新增
function addUnit() {
	firstDialog.Title = "新增";
	firstDialog.URL = "<%=path%>/system/user_edit.jsp";
	firstDialog.ID = "a1";
	firstDialog.Width=520;
	firstDialog.Height=200;
	firstDialog.show();
}
//修改	
function onEdit(rowid){
	firstDialog.Title = "修改";
	firstDialog.URL = "<%=path%>/system/user_edit.do?id="+rowid;
	firstDialog.ID = "a2";
	firstDialog.Width=520;
	firstDialog.Height=300;
	firstDialog.show();
}

//上传	
function onLoad(rowid){
	firstDialog.Title = "上传";
	firstDialog.URL = "<%=path%>/system/user_excledit.do?id="+rowid;
	firstDialog.ID = "a3";
	firstDialog.Width=700;
	firstDialog.Height=400;
	firstDialog.show();
}

//删除	
function onDelete(rowid){
	top.Dialog.confirm("确定要删除该记录吗？",function(){
  	//删除记录
  	$.post("system/user_delete.do",
  			{"id":rowid},
  			function(data){
  				handleResult(data.result);
			},"json");
	});
}
	
	
//批量删除
function deleteUnit() {
	var rows = grid.getSelectedRows();
	var rowsLength = rows.length;
	if(rowsLength == 0) {
		top.Dialog.alert("请选中要删除的记录!");
		return;
	}
	top.Dialog.confirm("确定要删除吗？",function(){
		$.post("system/user_delete.do",
				//获取所有选中行
				getSelectIds(grid),
				function(data){
  				handleResult(data.result);
			},
				"json");
		});
}
//删除后的提示
function handleResult(result){
	if(result == 1){
		top.Dialog.alert("删除成功！",null,null,null,1);
		grid.loadData();
	}else{
		top.Dialog.alert("删除失败！");
	}
}

//获取所有选中行获取选中行的id 格式为 ids=1&ids=2 
function getSelectIds(grid) {
	var selectedRows = grid.getSelectedRows();
	var ids = "";
	$.each(selectedRows,function(){
		if(ids==""){
			ids = this.id;
		}else{
			ids += ","+this.id;
		}
	});
	return {"id":ids};
}

//查询
function searchHandler(){
	var query = $("#queryForm").formToArray(); 
    grid.setOptions({ params : query});
    //页号重置为1
    grid.setNewPage(1);
    grid.loadData();//加载数据
}

//重置查询
function resetSearch(){
	$("#queryForm")[0].reset();
	$("#sel-role").attr("selectValue","");
	$("#sel-role").render();
	 searchHandler();
}

//刷新表格数据并重置排序和页数
function refresh(isUpdate){
	if(!isUpdate){
		//重置排序
    	grid.options.sortName='id';
    	grid.options.sortOrder="desc";
    	//页号重置为1
		grid.setNewPage(1);
	}
	
	grid.loadData();
}

//处理高度自适应
function customHeightSet(){
	$(".cusBoxContent").height($("#scrollContent").height()+25)
}
//关闭弹窗页面
function closeFirstDialog(){
	firstDialog.close();
}
//重置密码
function onPwd(id){ 
     parent.Dialog.confirm("确定要重置密码吗？",function(){
            $.post("system/user_resetPwd.do",{"id":id},function(result){
                    if(result){
			               parent.Dialog.alert("重置成功!");
                           searchHandler();
		           }else{
			               parent.Dialog.alert("重置失败!");
		          }
	       });
	});
}
//批量重置密码
function resetUser(){
	var rows = grid.getSelectedRows();
	var rowsLength = rows.length;	
	if(rowsLength == 0) {
		parent.Dialog.alert("请选中要重置密码的记录!");
		return;
	}
	parent.Dialog.confirm("确定要重置密码吗？",function(){
		  $.post("system/user_resetPwd.do", getSelectIds(grid), function(result){
					if(result){
			             parent.Dialog.alert("重置成功!");
                         searchHandler();
		            }else{
			              parent.Dialog.alert("重置失败!");
		             }
			});
	});
}
</script>	
<script id="editor" type="text/plain" style="width:1024px;height:500px;"></script>
</body>
</html>