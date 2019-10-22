<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8"/>
		<base href="<%=basePath%>">
		<title>-登录</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
		<meta http-equiv="Content-type" content="text/html; charset=utf-8">
		<!-- BEGIN GLOBAL MANDATORY STYLES -->
		<link href="assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
		<link href="assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
		<!-- END GLOBAL MANDATORY STYLES -->
		<!-- BEGIN PAGE LEVEL STYLES -->
		<link href="assets/pages/css/login.css" rel="stylesheet" type="text/css"/>
		<!-- END PAGE LEVEL SCRIPTS -->
		<!-- BEGIN THEME STYLES -->
		<link href="assets/global/css/components.css" id="style_components" rel="stylesheet" type="text/css"/>
		<link href="assets/global/css/plugins.css" rel="stylesheet" type="text/css"/>
		<!-- END THEME STYLES -->
		<link rel="shortcut icon" href="assets/pages/img/favicon.ico" />
		<style>
			.verifyCode {
				height: 43px;
				color: firebrick;
				display: inline-block;
				float: right;
				margin-top: -43px;
			}
			.flex-container {
			    display: -webkit-flex;
			    display: flex;
			    width: 100%;
			    text-align: center;
			    height: 50px;
			    line-height: 50px;
			    margin-bottom: 15px;
			}
			.flex-container .flex-item {
				flex: 1;
				font-size: 16px;
				font-family: "微软雅黑";		
				cursor:pointer;		
			}
			.flex-item.active {
				border-bottom: 5px solid #337ab7;
			}
		</style>
	</head>
<!-- BEGIN BODY -->
	<body class="login">
		<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
		<div class="menu-toggler sidebar-toggler">
		</div>
		<!-- END SIDEBAR TOGGLER BUTTON -->
		<!-- BEGIN LOGO -->
		<div class="logo">
			<!--<a href="index.html">-->
			<img src="assets/pages/img/Resto+.png" style="height: 40px;" alt=""/>
			</a>
		</div>
		<!-- END LOGO -->
		<!-- BEGIN LOGIN -->
		<div class="content">
			<!-- BEGIN LOGIN FORM -->
			<div class="login-form">
				<div class="form-title" style="text-align: center;margin-bottom: 15px;">
					<strong>欢迎使用上海餐加企业管理后台系统</strong>
				</div>
				<div class="alert alert-danger display-hide">
					<button class="close" data-close="alert"></button>
					<span>${reslut}</span>
				</div>
				<div class="flex-container" id="login_tab">
					<div class="flex-item active">手机登录</div>
					<div class="flex-item">账号密码登录</div>
				</div>
				
				<div id="loginByPhone" class="loginContent">
					<div class="form-group">
						<label class="control-label visible-ie8 visible-ie9">手机号</label>
						<input class="form-control form-control-solid placeholder-no-fix" type="text"  placeholder="手机号" name="telephone"/>
					</div>
					<div class="form-group">
						<div>
							<input class="form-control form-control-solid placeholder-no-fix" type="text" placeholder="验证码" name="code" style="width: 70%" />
							<input class="verifyCode" type="button" id="btn" value="获取验证码" />
						</div>
					</div>
				</div>
				
				<div id="loginByPassword" class="loginContent" style="display: none;">
					<div class="form-group">
						<label class="control-label visible-ie8 visible-ie9">手机号</label>
						<input class="form-control form-control-solid placeholder-no-fix" type="text"  placeholder="请输入手机号" name="account"/>
					</div>
					<div class="form-group">
						<label class="control-label visible-ie8 visible-ie9">密码</label>
						<input class="form-control form-control-solid placeholder-no-fix" type="password"  placeholder="请输入密码" name="password"/>
					</div>
				</div>
				
				<div class="form-actions">
					<button type="button" id="subBtn" class="btn btn-primary btn-block uppercase">登录</button>
				</div>
			</div>
			<!-- END LOGIN FORM -->
		</div>
		<div class="copyright">
			&copy;  上海餐加企业管理咨询有限公司
		</div>
		<!-- END LOGIN -->
		<script src="assets/global/plugins/jquery.min.js" type="text/javascript"></script>
		<script src="assets/global/plugins/jquery-migrate.min.js" type="text/javascript"></script>
		<script src="assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
		<!-- END CORE PLUGINS -->
		<!-- BEGIN PAGE LEVEL PLUGINS -->
		<script src="assets/global/plugins/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script>
		<script src="assets/global/plugins/jquery-validation/js/localization/messages_zh.js" type="text/javascript" charset="utf-8"></script>
		<!-- END PAGE LEVEL PLUGINS -->
		<script>
	        var loginByPhone = true;
			var loginByPassword = false;
	        			
		</script>
		<script>
			//使用js面向对象切换登录方式 --- (明明JQ几行能搞定你偏要...0.0)		
			
			window.onload = function(){
				var tab = new Tab("login_tab");
				tab.init();								
			}		
			
			function Tab(id){
				this.oNode = document.getElementById(id);
				this.aItem = this.oNode.getElementsByTagName("div");
				this.content = document.getElementsByClassName("loginContent");
			}						
			Tab.prototype.init = function(){
				var that = this;
				for(var i=0;i<this.aItem.length;i++){
					this.aItem[i].index = i;
					
					this.aItem[i].onclick = function(){
						that.change(this);
					}
				}
			}	
			
			Tab.prototype.change = function(obj){
				var that = this;
				for(var i=0;i<this.aItem.length;i++){
					this.aItem[i].className = "flex-item";
					this.content[i].style.display = "none";					
				}
				obj.className = "flex-item active";
				this.content[obj.index].style.display = "block";	
				if(obj.index == 0){
					loginByPhone = true;
					loginByPassword = false;
				}else if(obj.index == 1){
					loginByPhone = false;
					loginByPassword = true;
				}
			}						
			//发送验证码
			console.log(loginByPhone);
			console.log(loginByPassword);
			$("#btn").click(function () {
				var telephone = $("input[name='telephone']").val();
				if (telephone == null || telephone == ''){
					$('.alert-danger').html("手机号码不能为空").show();
					return;
				}
                var myreg = /^(((13[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
                if(!myreg.test(telephone)){
                    $('.alert-danger').html("请输入有效的手机号码").show();
                    return;
                }
                settime();
				$.ajax({
					type:"GET",
					url:'sendCode/1/' + telephone,
					contentType:"application/json",
					success:function(data){ //成功后返回
						if (data.success){
							$('.alert-success').html("发送成功").show();
						}else {
							$('.alert-danger').html(data.message).show();
						}
					},
					error: function(){ //失败后执行
						$('.alert-danger').html("发送失败").show();
					}
				});
			});

            var countdown = 60;
            function settime() {
                if(countdown == 0) {
                    $("#btn").attr("disabled", false);
                    $("#btn").attr("value", "获取验证码");
                    countdown = 60;
                    return;
                } else {
                    $("#btn").attr("disabled", true);
                    $("#btn").attr("value", "重新发送(" + countdown + ")");
                    countdown--;
                }
                setTimeout(settime, 1000)
            }
            //确认登录
            $("#subBtn").click(function(){
            	var telephone = $("input[name='telephone']").val();
            	var account = $("input[name='account']").val();
            	var code = $("input[name='code']").val();
            	var password = $("input[name='password']").val();
//          	
//				if (telephone == '' && loginByPhone){
//					$('.alert-danger').html("手机号码不能为空").show();
//					return;
//				}
//              var myreg = /^(((13[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
//              if(!myreg.test(telephone)){
//              	debugger;
//                  $('.alert-danger').html("请输入有效的手机号码").show();
//                  return;
//              }
//              if(password == '' && !loginByPhone){
//                  $('.alert-danger').html("请输入密码").show();
//                  return;
//              }
				
				if(loginByPhone){
					var userLoginDto = {
						telephone:telephone,
						password:'',
						code:code
					}
				}else{
					var userLoginDto = {
						telephone:account,
						password:password,
						code:''
					}
				}
												
        		$.ajax({
					type:"post",
					url:'/login',
					data:JSON.stringify(userLoginDto),
					contentType:"application/json",
					success:function(data){ //成功后返回
						if (data.success){
							window.location.href = "index";
						}else {
							$('.alert-danger').html(data.message).show();
						}
					},
					error: function(){ //失败后执行
						$('.alert-danger').html("发送失败").show();
					}
				});

            })
		</script>
	</body>
</html>