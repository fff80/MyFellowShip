<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta charset="UTF-8">
	<base href="<%=basePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" ></meta>
<title></title>
<!--框架必需start-->
<link href="libs/css/import_basic.css" rel="stylesheet" type="text/css"/>
<link href="libs/skins/blue/style.css" rel="stylesheet" type="text/css" id="theme" themeColor="blue" positionTarget="positionContent"/>
<link href="system/skin/style.css" rel="stylesheet" type="text/css" id="skin"  skinPath="system/skin/"/>
<script type="text/javascript" src="libs/js/jquery.js"></script>
<script type="text/javascript" src="libs/js/language/cn.js"></script>
<script type="text/javascript" src="libs/js/main.js"></script>
<!--框架必需end-->
	<script type="text/javascript" src="layer/layer.min.js"></script>
<!--引入弹窗组件start-->
<script type="text/javascript" src="libs/js/popup/drag.js"></script>
<script type="text/javascript" src="libs/js/popup/dialog.js"></script>
<!--引入弹窗组件end-->
<script>
function openlayer2(){
		var URL=arguments[0];
		var WIDTH=arguments[1]==null?"800px":(arguments[1]+"px");
		var HEIGHT=arguments[2]==null?"500px":(arguments[2]+"px");
		$.layer({
		    type: 2,
		    shadeClose: false,
		    title: false,
		    closeBtn: [1, false],
		    shade: [0.5, '#000'],
		    border: [0, 0.3, '#000'],
		    offset: ['',''],
		    area: [WIDTH,HEIGHT],
		    iframe: {src:URL}
		});
	}
function closelayer(){
   	 	 layer.closeAll();
      }
function bookmarksite(title, url){
    if (window.sidebar) // firefox
        window.sidebar.addPanel(title, url, "");
    else 
        if (window.opera && window.print) { // opera
            var elem = document.createElement('a');
            elem.setAttribute('href', url);
            elem.setAttribute('title', title);
            elem.setAttribute('rel', 'sidebar');
            elem.click();
        }
        else 
            if (document.all)// ie
                window.external.AddFavorite(url, title);
}
function exitHandler(){
	top.Dialog.confirm("确定要退出系统吗",function(){
		 window.location.href="<%=path %>/login.jsp";
		 $.post("system/login_logout.do",function(result){});
    });
}

$(function(){
	/*if(broswerFlag!="IE6"){
		var cookTip=jQuery.jCookie('closeTip');
		if(!cookTip){
			$("#lbox").tip({content: "分隔条现在可以左右拖拽啦！<span class='red'><a onclick='closeTip()'>我知道了</a></span>。",distanceY:100,distanceX:10,arrowDirection:"left",width:160,showCloseBtn:true,onClose:function(){
				jQuery.jCookie('closeTip',"sure");
			}});
		}
	}*/
})
function closeTip(){
	jQuery.jCookie('closeTip',"sure");
	$("#lbox").hideTip();
}

</script>
</head>
<body>
<div id="mainFrame">
<!--头部与导航start-->
<div id="hbox">
	<div id="bs_bannercenter">
	<div id="bs_bannerright">
	<div id="bs_bannerleft">	
		<div class="bs_bannerhd"><img src="images/hdos2.png">用户手机管理系统</div>
	</div>
	</div>
	</div>
	<div id="bs_navcenter">
	<div id="bs_navleft">
	<div id="bs_navright">
		<div class="bs_nav">
			
			<div class="float_left padding_top2 padding_left5">
				【今天是
				<script>
					var weekDayLabels = new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
					var now = new Date();
				    var year=now.getFullYear();
					var month=now.getMonth()+1;
					var day=now.getDate()
				    var currentime = year+"年"+month+"月"+day+"日 "+weekDayLabels[now.getDay()]
					document.write(currentime)
				</script>】
			</div>	
			<div class="float_left" style="padding:2px 0 0 20px;" id="positionContent"></div>	
			<div class="float_right padding_top2 padding_right5">
				<div class="bs_navleft">
					<li class="fontTitle">字号:</li>
					<li class="fontChange"><span><a href="javascript:;" setFont="16">大</a></span></li>
					<li class="fontChange"><span><a href="javascript:;" setFont="14">中</a></span></li>
					<li class="fontChange"><span><a href="javascript:;" setFont="12">小</a></span></li>
					<div class="clear"></div>	
				</div>	
				
				<div class="bs_navleft">
					<li class="fontTitle">字体:</li>
					<li class="fontFamily"><span><a href="javascript:;" setFont="宋体">宋</a></span></li>
					<li class="fontFamily"><span><a href="javascript:;" setFont="微软雅黑">雅</a></span></li>
					<div class="clear"></div>	
				</div>	
				
				
				<span class="icon_fullscreen hand" id="fullSrceen" hideNav="true">开启全屏</span>
				<span class="icon_exit hand" onclick='exitHandler();'>退出系统</span>
			</div>
			<div class="clear"></div>
		</div>
	</div>
	</div>
	</div>
</div>
<!--头部与导航end-->

 <div id="mainLayout">
    <div position="left">
			<div id="lbox">
				<div id="lbox_topcenter">
				<div id="lbox_topleft">
				<div id="lbox_topright">
				</div>
				</div>
				</div>
				<div id="lbox_middlecenter">
				<div id="lbox_middleleft">
				<div id="lbox_middleright">
						<div id="bs_left" style="width:100%;">
						<IFRAME height="100%" width="100%"  frameBorder=0 id=frmleft name=frmleft src="system/left.jsp"  allowTransparency="true"></IFRAME>
						</div>
						<!--更改左侧栏的宽度需要修改id="bs_left"的样式-->
				</div>
				</div>
				</div>
				<div id="lbox_bottomcenter">
				<div id="lbox_bottomleft">
				<div id="lbox_bottomright">
					<div class="lbox_foot"></div>
				</div>
				</div>
				</div>
			</div>
    </div>
    <div position="center">
   		<div class="ali01 ver01"  width="100%">
			<div id="rbox">
				<div id="rbox_topcenter">
				<div id="rbox_topleft">
				<div id="rbox_topright">
				</div>
				</div>
				</div>
				<div id="rbox_middlecenter">
				<div id="rbox_middleleft">
				<div id="rbox_middleright">
					<div id="bs_right">
					       <IFRAME height="100%" width="100%" frameBorder=0 id=frmright name=frmright src="system/open.jsp"  allowTransparency="true"></IFRAME>
					</div>
				</div>
				</div>
				</div>
				<div id="rbox_bottomcenter" >
				<div id="rbox_bottomleft">
				<div id="rbox_bottomright">

				</div>
				</div>
				</div>
			</div>
		</div>
    </div>
</div> 

<!--尾部区域start-->
<div id="fbox">
	<div id="bs_footcenter">
	<div id="bs_footleft">
	<div id="bs_footright">
		用户手机管理系统  版权所有 粤ICP备：09007778
	</div>
	</div>
	</div>
</div>
</div>
<!--尾部区域end-->


<!--窗口任务栏区域start-->
<div id="dialogTask" class="dialogTaskBg" style="display:none;"></div>
<!--窗口任务栏区域end-->

<!--浏览器resize事件修正start-->
<div id="resizeFix"></div>
<!--浏览器resize事件修正end-->

<!--载进度条start-->
<div class="progressBg" id="progress" style="display:none;"><div class="progressBar"></div></div>
<!--载进度条end-->
</body>
</html>
