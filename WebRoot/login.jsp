<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
	<meta charset="UTF-8">
	<base href="<%=basePath%>">
	<title>用户手机管理系统</title>
	<meta name="keywords" content=""/>
	<meta name="description" content=""/>
	<meta name="viewport"  content="width=device-width,user-scalable=no">
	<link rel="stylesheet" href="css/H-ui.css"/>
	<link rel="stylesheet" href="css/login.css"/> 
</head>
<body>
<input type="hidden" id="TenantId" name="TenantId" value="" />
<div class="header"><img src="images/hdos2.png">用户手机管理系统</div>
<div class="loginWraper">
  <div class="loginBox">
    <form id="myform" action="">
      <div class="formRow user">
        <input id="username"  name="user.username"  type="text" placeholder="用户名" class="input_text input-big">
      </div>
      <div class="formRow password">
        <input id="password"  name="user.password"  type="password" placeholder="密码" class="input_text input-big">
      </div>
      <div class="formRow yzm">
        <input class="input_text input-big" type="text" placeholder="验证码"  style="width:150px;" id="code" name="code">
        	 <a id="kanbuq" href="javascript:reloadImage();"><img src="js/image.jsp" class="mr-10 ml-10" id="img1">看不清，换一张</a> </div>
      <div class="formRow">
        <input type="button" class="btn radius btn-success btn-login" value="&nbsp;登&nbsp;&nbsp;&nbsp;&nbsp;录&nbsp;" onclick="onLogin();">
      </div>
    </form>
  </div>
</div>
<div class="footer">用户手机管理系统版权所有 粤ICP备：09007778</div>
<script src="js/jquery-1.8.3.min.js"></script>
<script src="layer/layer.min.js"></script>
<script>
$(function(){
	$("#username").focus();
	$("input").keydown(function(event) {
		if (event.keyCode == 13) {
			onLogin();
		}
	});
})
function reloadImage(){
	    <%--  给string增加个len ()方法，计算string的字节数 --%>
	String.prototype.len = function(){ 
    	return this.replace(/[^\x00-\xff]/g, "xx").length; 
	} 
    var times=Math.random();
	document.all.img1.src="${pageContext.request.contextPath}/js/image.jsp?"+times;
}
function onLogin(){
	layer.load("努力登录中...");
	if(validateData()){
		$.post("system/login_login.do",$("#myform").serializeArray(),function(data){
		layer.msg(data.msg);
    		if(data.flag){
    			layer.closeAll();
    			top.window.location="system/main.jsp";
    		}else{
    			layer.msg(data.msg);
    			reloadImage();
    		}
		});
	}
}

function validateData(){
	var username=$.trim($("#username").val());
	var password=$.trim($("#password").val());
	var code=$.trim($("#code").val());
	if(username==""){
		layer.msg("用户名不能为空.");
		return false;
	}else if(password==""){
		layer.msg("密码不能为空.");
		return false;
	}else if(code==""){
		layer.msg("验证码不能为空.");
		return false;
	}
	return true;
}

</script>
</body>
</html>