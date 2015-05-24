<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>基本表格模板</title>
<!--框架必需start-->
<script type="text/javascript" src="../../libs/js/jquery.js"></script>
<script type="text/javascript" src="../../libs/js/language/cn.js"></script>
<script type="text/javascript" src="../../libs/js/framework.js"></script>
<link href="../../libs/css/import_basic.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" id="skin" prePath="../../" scrollerY="false"/>
<link rel="stylesheet" type="text/css" id="customSkin"/>
<!--框架必需end-->

<!--树组件start -->
<script type="text/javascript" src="../../libs/js/tree/ztree/ztree.js"></script>
<link href="../../libs/js/tree/ztree/ztree.css" rel="stylesheet" type="text/css"/>
<!--树组件end -->


<!--箭头分页start-->
<script type="text/javascript" src="../../libs/js/nav/pageArrow.js"></script>
<!--箭头分页end-->

<!--布局控件start-->
<script type="text/javascript" src="../../libs/js/nav/layout.js"></script>
<!--布局控件end-->

<!--数据表格start-->
<script src="../../libs/js/table/quiGrid.js" type="text/javascript"></script>
<!--数据表格end-->

<style>
.l-layout-center{
    border:none!important;
}
.l-layout-left{
    border-bottom:none!important;
}
.l-layout-drophandle-left{
    width: 10px;
}
</style>
</head>
<body>
	<div id="layout1">
        <div id="centerCon" position="center" style="">
        	<div class="box2" panelTitle="查询"  showStatus="false">
				<table style="font-size:12px;">
					<tr>
						<td>名称：</td>
						<td><input type="text" id="name" /></td>
						<td><button onclick="search();"><span class="icon_find" id="search">查询</span></button></td>
						<td><button id="resetButton" onclick="reset();"><span class="icon_clear">重置</span></button></td>
					</tr>
				</table>
			</div>
			<div style="margin: 0;padding: 0 5px 0 0;font_12">
				<div id="maingrid"></div>
			</div>
			<div id="pageContent" style = "height:35px"></div>
        </div>
    </div> 
	
	
<script type="text/javascript">
	//数据表格使用
	 var g_pageNo = 1;
	 var g_pageSize = 10;
	 var g;

	function initComplete(){
  		//alert("init");
		$("#layout1").layout({ leftWidth: 150,onEndResize:function(){
			  	g.resetWidth();
		}}); 
		
		//本地数据源
		g = $("#maingrid").quiGrid({
        columns: [ 
	                { display: '登陆名', name: 'suname',     align: 'center', width: "20%",isSort:false},
	                /*{ display: '角色', name: 'srole',      align: 'center', width: "15%",isSort:false
		                ,render: function (rowdata, rowindex, value, column){
	                	var sname = '';
	                    $.ajax({ 
                              type : "post", 
                              url : "frontSystem/user_getRole.action", 
                              data : "srole=" + value, 
                              async : false,
                              success : function(result){ 
		                    	for(var i=0;i<result.length;i++){
		                               sname =  sname+result[i].sname;
		                        };
                             },
                             dataType: "json"//返回json格式的数据
                       });  
	                	return sname;   
                     }},*/
	                { display: '用户类型', name: 'itype', align: 'center', width: "20%", isSort:false
                    	 ,render: function (rowdata, rowindex, value, column){
                         if(value==0)  return "系统用户" ;
                         if(value==1)  return "机构用户" ;
                         if(value==2)  return "商户用户" ;
                         return "其他用户";
	                  }},
	                { display: '登陆时间', name: 'dlogintime',   align: 'center', width: "20%" ,isSort:false
                          ,render: function (rowdata, rowindex, value, column){
		                         if(value!=null){
		                        	 var logintime = new Array();
			                         logintime = value.split("T");
			                         value = logintime[0]+" "+ logintime[1];
			                     }
		                         return value;
	                      }
		  	        },
	                { display: '注销时间', name: 'dlogouttime',   align: 'center', width: "20%" ,isSort:false
		  	        	,render: function (rowdata, rowindex, value, column){
                        if(value!=null){
                       	 var logintime = new Array();
	                         logintime = value.split("T");
	                         value = logintime[0]+" "+ logintime[1];
	                     }
                        return value;
                        }
		            },
	                { display: '状态', name: 'istatus',   align: 'center', width: "20%" 
		                  ,render: function (rowdata, rowindex, value, column){
                                if(value==1)  return "正常" ;
                                return "<font color='FF0000'>禁用</font>";
		                  }
	                 },
                	{ display: '操作', isAllowHide: false, align: 'center', width: 80,
						 //操作列渲染为图标
						 render: function (rowdata, rowindex, value, column){
	                 	    return '<div class="padding_top4 padding_left5">'
	                                 + '<span class="img_view hand" title="查看" onclick="onView(' + rowdata.iid + ')"></span>'
	                                 + '<span class="img_edit hand" title="修改" onclick="onEdit(' + rowdata.iid + ')"></span>' 
	                                 + '<span class="img_delete hand" title="删除" onclick="onDelete(' + rowdata.iid+','+rowindex + ')"></span>'
	                             + '</div>';
		                 }
		            }
         ], 
        data:[], 
        pageNum:1,
        pageSize: 10, 
        sortName: 'iid',
        rownumbers:true,
        checkbox:true,
        //userPager:false,
        height: '100%', width:"100%",
        percentWidthMode:true,
        //顶部图标按钮栏
		toolbar: 
			{ 
			items: [
                { text: '新增', click: addUser, iconClass: 'icon_add' },
                { line: true },
                { text: '批量删除', click: deleteUser, iconClass: 'icon_delete' },
                { line: true },
                { text: '导入', click: importUser, iconClass: 'icon_import' },
                { line: true },
                { text: '导出当前页', click: exportUser, iconClass: 'icon_export' },
                { line: true },
                { text: '导出所有', click: exportUser2, iconClass: 'icon_export' }
            ]
        },
        onChangeSort : function(){
            //alert(123);
            getData(g.options.sortName,g.options.sortOrder,g_pageNo,g_pageSize);
            return false;

        },
        onReload:function(){
        	refresh(false);
        }
      
         });
	}
      $(function(){
          //alert("getData");
          g.options.sortName = "iid";
          g.options.sortOrder = "desc";
          //var name = $("#name").val();
          getData(g.options.sortName,g.options.sortOrder,g_pageNo,g_pageSize);

      })
      //重置查询条件
      function reset(){
	    $("input").val("");
	    $("select").resetValue(); 
      }
        //批量删除
      	function deleteUser() {
        	var ids = "";
      		//选中一行或多行
      	    var rows = g.getSelectedRows();
      	    if (rows.length == 0) {
             	   top.Dialog.alert('请至少选择一行'); 
             	   return;
             };
      	    for(var index in rows){
          	    //alert(rows[index].iid);
          	    ids = ids + rows[index].iid +",";
      	        g.deleteRow(rows[index]);
      	    };
      	    //alert(ids);
            $.post("frontSystem/user_batchdelete.action",{ids:ids},function(result){
                 if(result){}else{top.Dialog.alert("网络异常,请稍后删除...");}
             },"json")
      	    
      		
      	};

    	

      function getData(sort,direction,pageNo,pageSize){
          $.post("<%=path%>/user_list.action",
                  {"sort":sort,
                   "direction":direction,
                   "pageNum":pageNo,
                   "pageSize":pageSize
                  },
                  function(result){
                      //alert(result);
                      gridData = result;
                      //刷新表格
                      var data = {
                   		  "pager.pageNo":pageNo,
                   		  "pager.totalRows":pageSize,
                   		  "rows":gridData
                      };
                      g.loadData(data);
                  },"json");
      }

     

      
	//添加
	function addUser() {
		top.Dialog.open({URL:"usermanager/userAdd.jsp?direction=add",Title:"新增",Width:600,Height:400}); 
	}
	
	
	
	//导入
	function importUser() {
		top.Dialog.alert("见JAVA版或.NET版演示。");
	}
	//导出
	function exportUser() {
		top.Dialog.alert("见JAVA版或.NET版演示。");
	}
	
	//导出所有
	function exportUser2() {
		top.Dialog.alert("见JAVA版或.NET版演示。");
	}
	
	//查看
	function onView(rowid){
		//alert("选择的记录Id是:" + rowid );
		top.Dialog.open({URL:"<%=path%>/user_editUI.action?uflag=show&user.iid="+rowid,Title:"查看",Width:500,Height:330}); 
	}
	
	//修改
	function onEdit(rowid){
		//top.Dialog.alert("见JAVA版或.NET版演示。");
		top.Dialog.open({URL:"<%=path%>/user_editUI.action?direction=edit&uflag=update&user.iid="+rowid,Title:"修改",Width:500,Height:330}); 
	}
	
	//删除
	function onDelete(rowid,rowidx){
		top.Dialog.confirm("确定要删除该记录吗？",function(){
			//alert(rowid);
		  	//top.Dialog.alert("向后台发送ajax请求来删除。见JAVA版或.NET版演示。");
		  	$.post("<%=path%>/user_delete.action",
	                  {"user.iid":rowid},
	                  function(result){
	                	 getData("iid","desc",1,10);
	                     top.Dialog.close();
	                  },"json");
		});
	};
    //查询
	function search(){
		var name = $("#name").val();
        //alert(name);
        $.post("<%=path%>/user_findUserByName.action",{"name":name},function(result){
	        	//alert(result);
	            var gridData = result;
	            //刷新表格
	            var data = {
	         		  "pager.pageNo":g_pageNo,
	         		  "pager.totalRows":g_pageSize,
	         		  "rows":gridData
	            };
	            g.loadData(data);
        },"json");
    };

  //刷新表格数据并重置排序和页数
    function refresh(isUpdate){
        //alert(isUpdate);
    	if(!isUpdate){
    		//重置排序
    		
        	g.options.sortName='iid';
        	g.options.sortOrder="desc";
        	//页号重置为1
        	//alert("222222");
    		g.setNewPage(1);
    	}
    	getData(g.options.sortName,g.options.sortOrder,g.options.pageNum,g.options.pageSize);
    }

	
function customHeightSet(contentHeight){
		$(".cusBoxContent").height(contentHeight-55)
		$(".orgTreeContainer").height(contentHeight-30);
	}
</script>	
</body>
</html>
