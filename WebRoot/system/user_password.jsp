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
<table style="width:600px;height:300px">
	<tr>
		<td class="ver01" width="100%">
	         <div class="box1" panelTitle="修改密码" showStatus="false" overflow="false" id="formContent" whiteBg="true">
	              <div id="scrollContent">
	                     <form id="editForm" action="" method="">
	                          <input type="hidden" id="userId" name="userId" />
	                          <input type="password" style="display:none"/>
	                          <fieldset > 
		                  		  <legend>修改密码</legend> 
	 	                          <table class="tableStyle" formMode="transparent">
	 	                                  <tr>
	 	                                          <td width="150">当前用户名：</td>
	 	                                          <td colspan="2"><input type="text"  id="username" name="username" disabled="disabled" style="width:250px;" /></td>
	 	                                 </tr>
		                                 <tr>
		                                         <td width="150">旧密码：</td>
		                                         <td colspan="2"><input type="password" id="oldPwd" name="oldPwd" style="width:250px;"/></td>
			                           </tr>
			                           <tr>
				                                  <td width="150">新密码：</td>
				                                  <td colspan="2"><input type="password" id="newPwd"  name="newPwd" style="width:250px;" /></td>
				                        </tr>
				                        <tr>
				                                 <td width="150">确认新密码：</td>
				                                 <td colspan="2"><input type="password"  id="rePwd" name="rePwd"  style="width:250px;" /></td>
			                            </tr>
			                             <tr>
			                                     <td colspan="2" class="ali02">
			                                                <input type="button" value="提交" onclick="edit()"/>
			                                                <input type="button" value="清空" onclick='resetPwd()'/>
			                                     </td>
			                          </tr>
		                       </table>
	                       </fieldset>
	                 </form>
	             </div>
	      </div>
	  </td>
   </tr>
</table>
<script type="text/javascript">
  //高度处理	
function customHeightSet(contentHeight){
    $("#scrollContent").height(contentHeight-250);
    $("#cusBoxContent1").height(contentHeight-55);
	$("#cusBoxContent2").height(contentHeight-140);
}
function initComplete(){
	initUserInfo();
}
function initUserInfo(){
     var username='<%=session.getAttribute("session_username")%>';
     $("#oldPwd").val("");
     $("#username").val(username);
}
function edit(){
	var oldPwd=$("#oldPwd").val();
	var newPwd=$("#newPwd").val();
	var rePwd=$("#rePwd").val();
	if(!oldPwd){
	    parent.Dialog.alert("请输入旧密码.");
	}else if(!newPwd){
	    parent.Dialog.alert("请输入新密码.");
	}else if(!rePwd){
	    parent.Dialog.alert("请确认新密码.");
	}else if(rePwd!=newPwd){
	    parent.Dialog.alert("请保持新密码输入一致.");
	}else{
	   $.post("system/user_updatePassword.do",$("#editForm").serializeArray(),function(result){
	              resetPwd();
	              parent.Dialog.alert(result.msg);
	   });
	}
}
function resetPwd(){//重置输入的密码
   $("#oldPwd").val("");
   $("#newPwd").val("");
   $("#rePwd").val("");
}
</script>	
</body>
</html>