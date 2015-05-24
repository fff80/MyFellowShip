<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<base href="<%=basePath%>">
<!--框架必需start-->
<script type="text/javascript" src="libs/js/jquery.js"></script>
<script type="text/javascript" src="libs/js/language/cn.js"></script>
<script type="text/javascript" src="libs/js/framework.js"></script>
<link href="libs/css/import_basic.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" id="skin" prePath="" scrollerY="false"/>
<link rel="stylesheet" type="text/css" id="customSkin"/>
<!--框架必需end-->

<!-- 树start-->
<script type="text/javascript" src="libs/js/tree/ztree/ztree.js"></script>
<link href="libs/js/tree/ztree/ztree.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
    var setting = {
        callback: {
            onClick: onClick
        }
    };

    var str='<%=session.getAttribute("session_type")%>';
    var zNodes;
      zNodes = [
        { id:3000, parentId:0, name:"系统管理", isParent: true,open:true},
		{ id:3005, parentId:3000, name:"用户维护",url:"system/user_list.jsp", target:"frmright"},
	   	{ id:3010, parentId:3000, name:"通讯录维护",url:"system/phone_list.jsp", target:"frmright"},
	   	{ id:3015, parentId:3000, name:"修改密码",url:"system/user_password.jsp", target:"frmright"},
	    ];
	
    function initComplete() {
       $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        /*$.post("frontSystem/user_hasPrivilege.action",function(result){
            //alert(result.length);
            $.fn.zTree.init($("#treeDemo"), setting, result);
            showContent();
        },"json")*/
        //每次刷新时保持上次打开的
    }
    function showContent() {
        var treeNodeId = jQuery.jCookie('leftTreeNodeId');
        if (treeNodeId == false || treeNodeId == "false") { }
        else {
            var zTree = $.fn.zTree.getZTreeObj("treeDemo");
            var node = zTree.getNodeByParam("id", treeNodeId);
            zTree.selectNode(node);
            if (node.url) {
                //每次刷新时设置当前位置内容
               // if (node.name == "当前位置") {
                    top.positionType = "normal";
                    top.positionContent = "当前位置：" + node.getParentNode().name + ">>" + node.name;
                //}
                //else {
                   // top.positionType = "none";
               // }
                top.frmright.location = node.url;
            }
        }
    }

    function onClick(e, treeId, treeNode) {
        //单击展开
        var zTree = $.fn.zTree.getZTreeObj("treeDemo");
        zTree.expandNode(treeNode);
        //出现进度条
        if (treeNode.url != null && treeNode.showProgess != false) {
             showProgressBar();
             top.positionContent = "当前位置：" + treeNode.getParentNode().name + ">>" + treeNode.name;
             top.positionType = "normal";
        }

        //可以设置某些页面出现或者某些页面不出现当前位置组件
        //if (treeNode.name == "当前位置") {
            //每次点击时设置当前位置内容
            //top.positionContent = "当前位置：" + treeNode.getParentNode().name + ">>" + treeNode.name;
           // top.positionType = "normal";
       // }
       // else {
            //top.positionType = "none";
       // }

        //存储点击节点id
        jQuery.jCookie('leftTreeNodeId', treeNode.id.toString());
    }

    function customHeightSet(contentHeight){
		var windowWidth=document.documentElement.clientWidth;
		$("#scrollContent").width(windowWidth-4);
		$("#scrollContent").height(contentHeight-30);
	}
	function homeHandler(){
		 var treeObj = $.fn.zTree.getZTreeObj("treeDemo")
        treeObj.expandAll(false);
		top.positionType="none";
		jQuery.jCookie('leftTreeNodeId',"false");
	}
</script>
<!-- 树end -->
</head>

<body leftFrame="true">
<div id="scrollContent" style="overflow-x:hidden;">
	<div>
		<ul id="treeDemo" class="ztree"></ul>
	</div>
</div>				
</body>
</html>
