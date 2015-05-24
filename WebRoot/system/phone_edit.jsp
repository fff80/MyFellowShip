<%@ page contentType="text/html; charset=utf-8" language="java" pageEncoding="utf-8"%>
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
		<input type="hidden" name="id" id="uid" value="${mp.id}" />
		<input type="hidden" id="editType" name="editType" value="${param.editType}" />
		<input type="hidden" id="username" name="username" value="${param.username}" />
		<table class="tableStyle" formMode="line">
			<tr><td width="25%">用户名：</td>
				<td width="75%"><input type="text" id="name" name="name"
						value="${mp.name}" class="validate[required]" style="width: 250px" />
					<span class="star">*</span></td></tr>
			 <tr><td width="25%">手机号：</td>
				<td width="75%"><input type="text" id="mobile" name="mobile"
						value="${mp.mobile}" class="validate[required]" style="width: 250px" />
					<span class="star">*</span></td></tr>
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
function initComplete(){
    var mob= /^1[3|5|8]\d{9}$/;
	var phone = /^0\d{2,3}-?\d{7,8}$/;
	var moblie=$.trim($("#mobile").val()); //验证电话1
	var moblie2=$.trim($("#mobile2").val()); //验证电话2
	/*
	if(moblie!=""){
        if(!mob.test(moblie)&&!phone.test(moblie))
        {
            alert('请输入有效的电话1！');
            $("#moblie").focus();
            return false;
        }
	}
	if(moblie2!=""){
        if(!mob.test(moblie2)&&!phone.test(moblie2))
        {
            alert('请输入有效的联系电话2！');
            $("#moblie2").focus();
            return false;
        }
	}
	*/
	return true;
}

function save(){
	var valid = $('#queryForm').validationEngine({returnIsValid: true});
	if(initComplete()){
		if(valid){
				$.post("system/phone_save.do",$("#queryForm").serializeArray(),function(data){
					if(data){
						top.Dialog.alert("保存成功",function(){
							top.frmright.searchHandler();//执行查询
							//top.frmright.grid.loadData();
							top.frmright.closeFirstDialog();
							});
					}else{
						top.Dialog.alert("保存失败");
					}
				});
		}
	}
}
</script>
		<!-- 异步提交end -->
	</body>

</html>