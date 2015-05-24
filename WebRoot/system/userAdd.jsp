<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
-->
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<!--框架必需start-->
<script type="text/javascript" src="${pageContext.request.contextPath}/web/libs/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/web/libs/js/language/cn.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/web/libs/js/framework.js"></script>
<link href="${pageContext.request.contextPath}/web/libs/css/import_basic.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" id="skin" prePath="${pageContext.request.contextPath}/web/"/>
<link rel="stylesheet" type="text/css" id="customSkin"/>
<!--框架必需end-->

<!-- 日期控件start -->
<script type="text/javascript" src="${pageContext.request.contextPath}/web/libs/js/form/datePicker/WdatePicker.js"></script>
<!-- 日期控件end -->

<!-- 树组件start -->
<script type="text/javascript" src="${pageContext.request.contextPath}/web/libs/js/tree/ztree/ztree.js"></script>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/web/libs/js/tree/ztree/ztree.css"></link>
<!-- 树组件end -->

<!-- 树形下拉框start -->
<script type="text/javascript" src="${pageContext.request.contextPath}/web/libs/js/form/selectTree.js"></script>
<!-- 树形下拉框end -->

<!-- 表单验证start -->
<script src="${pageContext.request.contextPath}/web/libs/js/form/validationRule.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/web/libs/js/form/validation.js" type="text/javascript"></script>
<!-- 表单验证end -->
<!-- 异步提交 -->
<script src="${pageContext.request.contextPath}/web/libs/js/form/form.js" type="text/javascript"></script>

<!-- 双向选择器start -->
<script type="text/javascript" src="${pageContext.request.contextPath}/web/libs/js/form/lister.js"></script>
<!-- 双向选择器end -->

<!--数据表格start-->
<script src="${pageContext.request.contextPath}/web/libs/js/table/quiGrid.js" type="text/javascript"></script>
<!--数据表格end-->



<script type="text/javascript">
function initComplete(){
    //表单异步提交处理
   $('#myFormId').submit(function(){ 
   //判断表单的客户端验证时候通过
       //var valid = $('#myFormId').validationEngine({returnIsValid: true});
       //alert(valid);
       //if(valid){
         var valid =validateForm(myFormId);
         if(valid){
	         $(this).ajaxSubmit({
	               //表单提交成功后的回调
	               success: function(responseText, statusText, xhr, $form){
	                    top.Dialog.alert("保存成功",function(){
	                        top.frmright.getData("id","desc",1,10);
	                        top.Dialog.close();
	                    });
	               }
	           });
	         }
        //}
       //阻止表单默认提交事件
       else{
    	     top.Dialog.alert('表单填写不正确，请按要求填写！');
             //return false; 
       }
         return false;
       });

   
   $.post("frontSystem/role_listjson.action",function(result){
	   var selData={fromList:result,"toList":[]};
		$("#srole").data("data",selData)
		$("#srole").render(); 
	 },"json")
    
  }
	//获取隐藏域值
	function getHiddenValue(){
		top.Dialog.alert($("input:hidden[name='user.srole']").val());
	}
	//手动触发验证，被验证的表单元素是containerId容器里的。 可以验证整个表单，也可以验证部分表单。
	function validateForm(containerId){
	    var valid = $(containerId).validationEngine({returnIsValid: true,showOnMouseOver:false});
		return valid;
	}
</script>
</head>
<body>
	<div class="box1" id="formContent" whiteBg="true">
	<form id="myFormId" action="<%=path %>/user_${param.direction}.action" method="post" showOnMouseOver="false">
	<input type="hidden" value=${user.iid} name="user.iid">
	<table class="tableStyle" formMode="transparent" style="font-size:12px;">
		<tr>
			<td>登陆名:</td>
			<td>
			   <input name="user.suname" value="${user.suname}" type="text" id="inputa1" class="validate[required,custom[noSpecialCaracters]] float_left"  validateField="validate_duser"/><span class="star float_left">*</span>
		       <div id="validate_duser" class="validation_info"></div>
		       <div class="clear"></div>
		    </td>
		</tr>
	   <c:if test="${param.direction=='add'}">
		<tr>
			<td>密码：</td>
			<td><input name="user.spwd"  type="password" id="spwd" class="validate[required,length[6,11],custom[noSpecialCaracters]] float_left"  validateField="validate_dpass"/><span class="star float_left">*</span>
			<div id="validate_dpass" class="validation_info">
			</div><div class="clear"></div>
			
			</td>
		</tr>
		<tr>
			<td>密码确认：</td><td>
			    <input type="password"  class="validate[required,confirm[spwd]] float_left"/><span class="star float_left">*</span>
			    <div class="validation_info"></div>
			    <div class="clear"></div>
			</td>
		</tr>
		</c:if>
		<tr>
			<td >用户类型：</td>
			 <td>
			   <select name="user.itype" >
			    <option value="0" ${user.itype==0?'selected':''} >系统用户</option>
			    <option value="1" ${user.itype==1?'selected':''}>机构用户</option>
			    <option value="2" ${user.itype==2?'selected':''}>商户用户</option>
			  </select>
			</td>
		</tr>
		
		<tr>
			<td >状态：</td>
			<td>
			  <select name="user.istatus">
			    <option value="1" ${user.istatus==1?'selected':''}>正常</option>
			    <option value="0" ${user.istatus==0?'selected':''}>禁用</option>
			  </select>
			</td>
		</tr>
		
		<tr>
			<td >分配角色：</td>
			<td>
			
			<div style="float_left;" >
		    	<div style="border:1px solid #DDD;padding:10px;" class="lister validate[required] float_left" selectedValue="${user.srole}" listerWidth="120" listerHeight="120" name="user.srole" id="srole"></div>
				<!--  <input type="button" value="获取隐藏域选中值" onclick="getHiddenValue()"/>-->
			</div>
			<div class="float_left" style="padding:90px 0 0 0;">
				<span class="star float_left">*</span>	
				<div class="validation_info"></div>
				<div class="clear"></div>		
			</div>	
			</td>
		</tr>
		<c:if test="${uflag!='show'}">
		<tr>
			<td colspan="2">
				<input type="submit" value="提交"/>
				<input type="button" value="取消" onclick='top.Dialog.close()'/>
			</td>
		</tr>
		</c:if>
	</table>
	</form>
	</div>
	
</html>
