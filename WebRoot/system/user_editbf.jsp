<%@ page contentType="text/html; charset=utf-8" language="java"
	pageEncoding="utf-8"%>
<%@ include file="include.inc.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<base href="<%=basePath%>" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title></title>
	<!--框架必需start-->
	<script type="text/javascript" src="libs/js/jquery.js"></script>
	<script type="text/javascript" src="libs/js/language/cn.js"></script>
	<script type="text/javascript" src="libs/js/framework.js"></script>
	<link href="libs/css/import_basic.css" rel="stylesheet" type="text/css" />
	<link href="libs/js/tree/ztree/ztree.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" id="skin" prePath="" />
	<link rel="stylesheet" type="text/css" id="customSkin" />
	<!--框架必需end-->

	<!-- 日期控件start -->
	<script type="text/javascript"
		src="libs/js/form/datePicker/WdatePicker.js"></script>
	<!-- 日期控件end -->

	<!-- 树组件start -->
	<script type="text/javascript" src="libs/js/tree/ztree/ztree.js"></script>
	<link type="text/css" rel="stylesheet"
		href="libs/js/tree/ztree/ztree.css"></link>
	<!-- 树组件end -->

	<!-- 树形下拉框start -->
	<script type="text/javascript" src="libs/js/form/selectTree.js"></script>
	<!-- 树形下拉框end -->

	<!-- 表单验证start -->
	<script src="libs/js/form/validationRule.js" type="text/javascript"></script>
	<script src="libs/js/form/validation.js" type="text/javascript"></script>
	<!-- 表单验证end -->


</head>

<body>
<div class="box1" id="formContent" whiteBg="true" overflow="true">
	<form id="queryForm" action="" method="post" target="frmright">
		<input type="hidden" name="user.id" id="uid" value="${user.id}" />
		<input type="hidden" name="user.userid" id="userid" value="${user.userid}" />
		<input type="hidden" id="editType" name="editType" value="${param.editType}" />
		<input type="hidden" id="wz" name="wz"/>
		<table class="tableStyle" formMode="line">
			<tr><td width="25%">用户名：</td>
				<td width="75%"><input type="text" id="username" name="user.username"
						value="${user.username}" class="validate[required]" style="width: 250px" />
					<span class="star">*</span></td></tr>
			 <tr><td> 用户类型： </td>
					<td><select id="type" name="user.type" selWidth="150" onchange="showtxl();">
							<option value="1" ${user.type==1?'selected':''}>系统用户 </option> 
							<option value="2" ${user.type==2?'selected':''}>手机用户 </option>
						</select><font color="red" size="1.5">选择系统用户，通讯录默认为空</font></td></tr>
			<tr>
			    <td>关联通讯录：</td>
			    <td>
				    <div style="heigth:500px" id="txl">
	                      <ul id="tree-1" class="ztree"></ul>
	                </div>
			    </td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="button" value="提交" onclick="save()" />
					<input type="button" value="取消" onclick='top.Dialog.close()' />
				</td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
/*  $(function(){
     var name='${user.username}';
     if(name!=""){
        $("#username").attr("disabled",true);
     }
   })
   */

	function initComplete(){
	   var userid=$("#userid").val();
	   showtxl();
       //$.fn.zTree.init($("#tree-1"), setting1, nodes1);
       $.post("<%=path%>/system/phone_radiolist.do", {"userid":userid}, function(result){
		    $.fn.zTree.init($("#tree-1"), setting1, result);
		}, "json");
    }
    
    var setting1 = {
       check: {
        enable: true
	   },
	   view: {
        showIcon: false
       }
	};
    
	var nodes1 = [
		    { id:1,  parentId:0, name:"部门1", open: true, checked: true},
		    { id:11, parentId:1, name:"员工1"},
		    { id:12, parentId:1, name:"员工2"},
		    { id:13, parentId:1, name:"员工3", checked:true},
		    { id:2,  parentId:0, name:"部门2", open: true},
		    { id:21, parentId:2, name:"员工4(没有复选框)", nocheck: true},
		    { id:22, parentId:2, name:"员工5"},
		    { id:23, parentId:2, name:"员工6"},
		    { id:24, parentId:2, name:"员工7(有复选框，但不可选)", chkDisabled: true}
        ];
        
    var xz;
    function getSelectValue(){
    //获取zTree对象
    var zTree = $.fn.zTree.getZTreeObj("tree-1");
    //得到选中的数据集
    var checkedNodes = zTree.getCheckedNodes(true);
    //得到未选中的数据集
    var checkedNodes2 = zTree.getCheckedNodes(false);
    var msg = "";
    var msg2 = "";
    for(var i = 0; i < checkedNodes.length; i++){
        msg += "," + checkedNodes[i].id;
    }
    if(msg == ""){
        msg = "wxz";
    }else{
        msg = msg.substring(1);
    }
    for(var j = 0; j < checkedNodes2.length; j++){
        msg2 = msg2 + "," + checkedNodes2[j].id;
    }
    if(msg2 == ""){
        msg2 = "无选择";
    }else{
        msg2 = msg2.substring(1);
    }
    xz=msg;
   // top.Dialog.alert("已选中:<br/>"+msg+"<br/>未选中：<br/>"+msg2);
}
 
    function showtxl(){
        var type=$("#type").val();
	    if(type==1) $("#txl").hide();
	    if(type==2) $("#txl").show();
    }
 
	/*
	function gettxl(){
		   $.ajax({
		          url:"system/phone_radiolist.do",
		          type:"post",
		          dataType:"json",
		          success:function(list){
		          alert(JSON.stringify(list));
		               var content="";
		               for(var i=0;i<list.length;i++){
		                  content+="<input type='checkbox' name='touch.txlid' value='"+list[i].id+"'>"+list[i].name+"&nbsp;&nbsp;&nbsp;"+list[i].mibile+"</input>";
		               }
		               content+="";
		               $("#txl").html(content);
		          }
		   });
		}
		*/
		
	

	function save(){
	    getSelectValue();
	    $("#wz").val(xz);
	    var type=$("#type").val();
	    if(type==1) $("#wz").val("wxz");
		var valid = $('#queryForm').validationEngine({returnIsValid: true});
		if(valid){
				$.post("system/user_save.do",$("#queryForm").serializeArray(),function(data){
					if(data.flag){
						top.Dialog.alert("保存成功",function(){
							top.frmright.searchHandler();//执行查询
							//top.frmright.grid.loadData();
							top.frmright.closeFirstDialog();
							});
					}else{
						top.Dialog.alert(data.msg);
					}
				});
		}
	}
</script>
		<!-- 异步提交end -->
	</body>

</html>