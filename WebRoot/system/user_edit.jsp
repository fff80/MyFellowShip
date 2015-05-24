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
		<table class="tableStyle" formMode="line">
			<tr><td width="25%">用户名：</td>
				<td width="75%"><input type="text" id="username" name="user.username"
						value="${user.username}" class="validate[required]" style="width: 150px"  onkeyup="value=value.replace(/[^a-zA-Z]/g,'')"/>
					<span class="star">*<font size="2">只能输入英文</font></span></td></tr>
			 <tr><td> 用户类型： </td>
					<td><select id="type" name="user.type" selWidth="150">
							<option value="1" ${user.type==1?'selected':''}>系统用户 </option> 
							<option value="2" ${user.type==2?'selected':''}>手机用户 </option>
						</select></td></tr>
		 <!-- 	<tr>
			    <td>关联通讯录：</td>
			    <td>
				    <div style="heigth:500px" id="txl">
	                      <ul id="tree-1" class="ztree"></ul>
	                </div>
			    </td>
			</tr>  -->
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
	function save(){
	    var type=$("#type").val();
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