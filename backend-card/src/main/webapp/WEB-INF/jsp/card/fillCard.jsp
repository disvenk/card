<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style type="text/css">
	.weui_mask {
	  	position: fixed;
	  	z-index: 1;
	  	width: 100%;
	  	height: 100%;
	  	top: 0;
	  	left: 0;
	  	background: rgba(0, 0, 0, 0.6);
	}
	.table.table-bordered tr th {
		text-align: center;
		line-height: 2.3;
	}	
	p {
		margin: 8px 0;
	}
	.weui_dialog_order {
		position: fixed;
		font-size: 20px;
		z-index: 3;
		width: 45%;
		top: 15%;
		left: 0;
		right: 0;
		margin: auto;
		text-align: center;
		background: #fff;
		border-radius: 5px;
		font-family: "微软雅黑";
	}	
	.closeImg {
		position: absolute;
		top: 10px;
		right: 10px;
		width: 35px;
		height: 35px;
	}	
	.pay_header {
		position: relative;
		width: 100%;
		text-align: center;
		display: table;
		font-size: 22px;
		border-bottom: 1px solid #eee;
		padding: 10px 0px;
	}	
	.paymode_body {
		position: relative;
		text-align: left;
		border-bottom: 1px solid #eee;
		padding: 25px;
		color: #5f5e5e;
	}	
	.weui_flex_item {
		text-align: center;
		margin: 8px;
		padding: 10px 5px;
		border: 1px solid #b7b4b4;
		-webkit-box-flex: 1;
		-webkit-flex: 1;
		flex: 1;
		border-radius: 5px;
	}	
	.weui_flex_item.active {
		background-color: #eaffee;
	}	
	.weui_flex_item:first-child {
		margin: 8px 8px 8px 0px;
		;
	}	
	.weui_flex_item:last-child {
		margin: 8px 0px 8px 8px;
	}	
	.weui_flex_item span {
		display: block;
	}	
	.weui_flex_item .headerText {
		font-size: 18px;
		color: #000;
	}	
	.payImg {
		width: 50px;
		height: 50px;
	}	
	.weui_pay {
		display: -webkit-flex;
		display: flex;
		-webkit-justify-content: space-between;
		justify-content: space-between;
		margin-bottom: 15px;
	}	
	.pay_footer {
		position: relative;
		display: -webkit-flex;
		display: flex;
		-webkit-justify-content: space-around;
		justify-content: space-around;
		width: 100%;
		font-size: 22px;
		padding: 20px 0;
		font-weight: bold;
	}	
	.paymode_body .articleCatty {
		width: 28%;
		text-align: right;
		display: inline-block;
	}	
	.flex_charge_left {
		text-align: right;
		display: inline-block;
		width: 15%;
	}	
	.flex_charge_right {
		text-align: left;
		display: inline-block;
		width: 40%;
		margin-left: 15px;
	}	
	.form-control.text {
		width: 30%;
		display: inline-block;
		margin-left: 15px;
	}	
	.flex_pin_item {
		display: -webkit-flex;
		display: flex;
		-webkit-justify-content: space-around;
		justify-content: space-around;
		padding: 10px 0;
	}
</style>
<div id="control">

	<div class="weui_dialog_alert" v-if="showChargeDialog">
		<div class="weui_mask" style="z-index:2;"></div>
		<div class="weui_dialog_order" style="width: 45%;">
			<div class="full-height">
				<div class="pay_header">
					<span class="order_middle">支付方式</span>
					<!--<img src="../assets/pages/img/close.png" class="closeImg" alt="关闭" @click="closePayDialog" />-->
				</div>
				<div class="paymode_body">
					<div class="weui_pay">
						<div class="weui_flex_item" @click="payByMode(f)" v-for="f in payModeList" :class="{active:f.state == currentState}">
							<img v-if="f.state == 1" src="../assets/pages/img/wxpay.png" class="payImg" alt="微信支付" />
							<img v-if="f.state == 2" src="../assets/pages/img/alipay.png" class="payImg" alt="支付宝" />
							<img v-if="f.state == 3" src="../assets/pages/img/cashpay.png" class="payImg" style="border-radius: 50%;" alt="现金支付" />
							<img v-if="f.state == 4" src="../assets/pages/img/bank.png" class="payImg" style="border-radius: 50%;" alt="支票支付" />
							<span class="headerText">{{f.name}}</span>
						</div>
					</div>
					<p style="margin: 15px 0;">
						<span>卡工本费</span>
						<span style="margin-left:15%;">10元</span>
					</p>
					<input type="text" autofocus="autofocus" class="form-control" v-model="authCode"
						 @change="payMoney" id="customerCode" placeholder="请扫描顾客出示的二维码" style="height:45px;font-size:20px;" maxlength="18">
				</div>
				<div class="pay_footer">
					<button class="btn btn-default" @click="confirmGetMoney" style="background: #199ed8;width: 80%;color: #fff;font-size:22px;">确认收款</button>
				</div>
			</div>
		</div>
	</div>

	<div class="portlet light bordered">
		<div class="portlet-title">
			<div class="caption">
				<span class="caption-subject bold font-blue-hoki">补卡</span>
			</div>
		</div>
		<div class="portlet-body">
			<div class="form-body">
				<div class="form-group row">
					<label class="col-md-1 control-label">手机号<span style="color:#FF0000;">*</span></label>
					<div class="col-md-3">
						<input type="text" class="form-control" name="cardId" v-model="telephone" placeholder="请输入手机号" @keyup.enter.prevent="searchCardInfo">
					</div>
					<div>
						<input class="btn green" type="submit" @click="searchCardInfo" value="搜索" />
					</div>
				</div>
			</div>

			<table class="table table-bordered" v-if="fillPageShow">
				<caption>卡号信息</caption>
				<thead>
					<tr>
						<th>卡号</th>
						<th>储值卡类型</th>
						<th>单位名称</th>
						<th>姓名</th>
						<th>手机号码</th>
						<th>身份证号</th>
						<th>折扣</th>
						<th>账户余额(元)</th>
						<th>状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th>{{cardInfo.cardId}}</th>
						<th v-if="cardInfo.type == 0">普通卡</th>
						<th v-if="cardInfo.type == 1">员工卡</th>
						<th>{{cardInfo.companyName}}</th>
						<th>{{cardInfo.customerName}}</th>
						<th>{{cardInfo.telephone}}</th>
						<th>{{cardInfo.idCard}}</th>
						<th>{{cardInfo.discountName}}</th>
						<th>{{cardInfo.remain}}</th>
						<th v-if="cardInfo.cardState == 1">正常</th>
						<th v-if="cardInfo.cardState == 2">已挂失</th>
						<th>
							<button type="button" class="btn btn-info" v-if="cardInfo.cardState == 2" @click="fillCard">补卡</button>
							<button type="button" class="btn btn-success" disabled v-if="cardInfo.cardState == 1" style="background: #eee;color: #000;border: 1px solid #606E7A;">补卡</button>
						</th>
					</tr>
				</tbody>
			</table>
		</div>
	</div>

	<!-- 模态框（Modal） -->
	<div class="weui_dialog_alert" v-if="fillCardDialog">
		<div class="weui_mask" style="z-index:2;"></div>
		<div class="weui_dialog_order">
			<div class="full-height">
				<div class="pay_header">
					<span class="order_middle">补卡</span>
				</div>
				<div class="paymode_body">
					<div class="form-group row">
						<label class="col-md-3 control-label" style="text-align: center;font-size: 16px;">新卡号<span style="color:#FF0000;">*</span></label>
						<div class="col-md-6">
							<input type="text" class="form-control" name="cardId" v-model="newCardId" placeholder="请刷卡" @keyup.enter.prevent="searchNewCardInfo">
						</div>
						<div>
							<input class="btn green" type="submit" @click="searchNewCardInfo" value="读卡" />
						</div>
					</div>
				</div>
				<div class="pay_footer">
					<button class="btn btn-default" style="background: #199ed8;width: 40%;color: #fff;font-size:18px;" @click="confirmPinCard">确定</button>
					<button class="btn btn-default" style="background: #199ed8;width: 40%;color: #fff;font-size:18px;" @click="closeFillDialog">取消</button>
				</div>
			</div>
		</div>
	</div>
	<weui-dialog :show.sync="wMessage.show" :msg="wMessage.message" :type="wMessage.type" ></weui-dialog>
</div>

<script>
	(function() {
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			//          ajax : {
			//              url : "card/rechargeData"
			//          }
		});
		var C = new Controller(null, tb);
		var vueObj = new Vue({
			el: "#control",
			data: {
				telephone: null,
				fillPageShow: false, 		//查询补卡信息
				cardInfo: null,
				showChargeDialog: false, 	//充值
				fillCardDialog: false, 		//查询补卡信息
				newCardId: null, 			//补卡后的新卡号
				currentState: 1,
				payModeList: [{
						name: "微信支付",
						state: 1
					},
					{
						name: "支付宝",
						state: 2
					},
					{
						name: "现金支付",
						state: 3
					},
					{
						name: "支票支付",
						state: 4
					}
				],
				authCode:null,			//支付二维码
				wMessage: {show: 0, message: "",type:null}, //加载中弹窗
			},
			events: {
	        	"successMessage": function (msg,time) {
		            this.showMessage(msg,1,time);
		        },
	            "errorMessage": function (msg,time) {
	                this.showMessage(msg,2,time);
	            },
		        "closeMessage": function () {
		            this.closeMessage();
		        },
		        "loadingMessage": function (msg,time) {
		            this.showMessage(msg,3,time);
		        }
		    },
			methods: {
				showMessage: function (msg,type,time) {
	            	this.wMessage.show = true;
		            this.wMessage.type = type;
		            this.wMessage.message = msg;
		            var that = this;
		            setTimeout(function () {
		                that.wMessage.show = 0;
		            }, time || 1000);
		        },
		        closeMessage:function(){
		        	this.wMessage.show = false;
		        },
				searchCardInfo: function() {
					var that = this;
					var myreg = /^(((13[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
	                if(!this.telephone){
	                	C.systemButtonNo('error','请填写手机号');
	                	return;
	                }else if(!myreg.test(this.telephone)){
	                	C.systemButtonNo('error','请输入有效的手机号码');
	                	return;
	                }
					$.ajax({
						type: "get",
						url: 'cardCustomer/byTelephone',
						data: {
							telephone: that.telephone
						},
						contentType: "application/json",
						success: function(data) { //成功后返回
							if(data.success) {
								C.systemButtonNo('success', '查询成功');
								that.cardInfo = data.data;
								that.fillPageShow = true;
								console.log(JSON.stringify(that.cardInfo));
							} else {
								C.systemButtonNo('error', data.message);
							}
						},
						error: function() { //失败后执行
							C.systemButtonNo('error', '查询失败');
						}
					});
				},
				searchNewCardInfo: function() {
					var that = this;
					getCardNum(function(num){
		                if(num == null || num == ''){
							C.systemButtonNo('error',"连接读卡服务失败");
		                    return;
		                }else{
		                	that.newCardId = num;
							C.systemButtonNo('success', '读卡成功');							
		                }
					})
				},
				fillCard: function() {
					this.fillCardDialog = true;
				},
				confirmPinCard: function() {
					var that = this;
					
					$.ajax({
						type: "get",
						url: 'cardCustomer/modifyCard',
						data: {
							id: this.cardInfo.id,
							type: this.cardInfo.type,
							cardId: this.newCardId,
							cardState: 1
						},
						contentType: "application/json",
						success: function(data) { //成功后返回
							if(data.success) {
								C.systemButtonNo('success', '补卡成功');
								that.fillCardDialog = false;
								that.showChargeDialog = true;
								console.log(JSON.stringify(that.cardInfo.cardState));
							} else {
								C.systemButtonNo('error', data.message);
							}
						},
						error: function() { //失败后执行
							C.systemButtonNo('error', '返回失败');
						}
					});
				},
				payMoney:function(){
                	var that = this;
                	if (this.currentState == 3 || this.currentState == 4) {
	                    this.authCode = null;
	                    that.$dispatch("errorMessage","请选择微信支付或支付宝支付",3000);
	                    return;
	                }
	            	if ($('#customerCode').val().length != 18) {
	                    this.authCode = null;
	                    that.$dispatch("errorMessage","请扫描正确的付款码",3000);
	                    return;
	                }
                	var scanParam = {
                		payType:this.currentState,
						authCode:this.authCode,
						paymentAmount:10, 		//充值的总金额不包括工本费						
                	};
					//发起支付请求					
					that.scanCodePayment(scanParam, function (result) {
						if (result.isPolling){
							that.$dispatch("loadingMessage","正在支付中，请稍候",30000);
						    //进入轮循
							var payaram = {
							    payType : that.currentState,
                                outTradeNo : result.outTradeNo
							};
							//表示当前轮循次数
							var number = 0;																
							var time = setInterval(function(){
								that.confirmPayment(payaram, function (conResult) {
									number += 1;
									console.log(number);
									if(number > 6){
										//轮循超时，退出轮循                                            
                                      	clearInterval(time);
                                      	that.$dispatch("errorMessage","支付超时，正在撤销支付请求",3000);
                                      	//调用撤销订单接口
                                      	that.revocationOfOrder(payaram, function (revokeResult) {
											if (revokeResult.success){
                                              	that.$dispatch("successMessage","撤销订单成功",3000);
											}else{
                                              	that.$dispatch("errorMessage",revokeResult.message,3000);
											}
                                      	});
                                      	return;
									}
									
									if (!conResult.isPolling && !conResult.success){
										//支付出错，退出轮循
										clearInterval(time);
                                      	that.$dispatch("errorMessage",conResult.message,3000);
									}else if (!conResult.isPolling && conResult.success){
									    //支付成功，退出轮循
                                      	clearInterval(time);
									    //调用充值接口插入充值记录
									    that.$dispatch("closeMessage");
									    
                                        var options = {		
                                        	cost:10,
						            		payType:that.currentState,
						            		companyId:that.cardInfo.companyId,
						            		cardId:that.cardInfo.cardId,
						            		payData:conResult.payData,
						            		cardType: this.cardInfo.type
						            	};
						            	console.log(JSON.stringify(options));
						            	//将充值明细传给三哥
						            	$.ajax({
						                    type:"post",
						                    url:'card/addCardRecharge',
						                    data:JSON.stringify(options),
						                    contentType:"application/json",
						                    success:function(data){ //成功后返回
						                        if (data.success){
						                        	that.showChargeDialog = false;
						                            that.$dispatch("successMessage","充值成功",3000);							                            
						                        }else {
						                            that.$dispatch("errorMessage","充值失败",3000);
						                        }
						                    },
						                    error: function(){ //失败后执行
						                        that.$dispatch("errorMessage","充值失败",3000);
						                    }
						                });
										console.log(conResult.payData);
									}                                    
                            	})
							},5000);

						}else{
                            that.$dispatch("errorMessage", result.message);
						}
                    });
                },
                scanCodePayment : function (param,cbk) {
                    $.post("rechargePay/scanCodePayment", param, function (result) {
                        cbk&&cbk(result);
                    });
                },
                confirmPayment : function (param, cbk) {
                    $.post("rechargePay/confirmPayment", param, function (result) {
                        cbk&&cbk(result);
                    });
                },
                revocationOfOrder : function (param, cbk) {
                    $.post("rechargePay/revocationOfOrder", param, function (result) {
                        cbk&&cbk(result);
                    });
                },
				confirmGetMoney: function() {
					var that = this;
					
					if(this.currentState == 1 || this.currentState == 2){
						that.$dispatch("errorMessage", '请扫描手机二维码支付',3000);
	            		return;
	            	};					
					var options = {
	            		cost: 10,
	            		payType: this.currentState,
	            		companyId: this.cardInfo.companyId,
	            		cardId:this.newCardId,
	            		cardType: this.cardInfo.type
	            	}
	            	that.$dispatch("loadingMessage","正在支付中，请稍候",30000);
	            	//当支付方式选择支票或现金时
            		$.ajax({
	                    type:"post",
	                    url:'card/addCardRecharge',
	                    data:JSON.stringify(options),
	                    contentType:"application/json",
	                    success:function(data){ //成功后返回
	                        if (data.success){	                            
	                            that.showChargeDialog = false;
//	                            that.cardInfo.cardState = 1;
								$.ajax({
									type: "get",
									url: 'cardCustomer/byTelephone',
									data: {
										telephone: that.telephone
									},
									contentType: "application/json",
									success: function(data) { //成功后返回
										if(data.success) {
											that.cardInfo = data.data;
											console.log(JSON.stringify(that.cardInfo));
										} else {
											C.systemButtonNo('error', data.message);
										}
									},
									error: function() { //失败后执行
										C.systemButtonNo('error', '查询失败');
									}
								});
	                            that.$dispatch("successMessage","充值成功",3000);
	                        }else {
	                            that.$dispatch("errorMessage","充值失败",3000);
	                        }
	                    },
	                    error: function(){ //失败后执行
	                        that.$dispatch("errorMessage","充值失败",3000);
	                    }
	                });	            					
				},
				closePayDialog: function() {
					this.showChargeDialog = false;
				},
				closeFillDialog: function() {
					this.fillCardDialog = false;
				},
				payByMode: function(f) {
					$('#customerCode').focus();
					this.currentState = f.state;
				},
			},
			components: {
	        	"weui-dialog":{
		        	props: ['show','msg','type'],
				    template: '<div class="weui_loading_toast" v-if="show">' +
				    '<div class="weui_mask_transparent"></div>' +
				    '<div class="weui_toast msg-dialog">' +
						'<img src="../assets/pages/img/correctImg.png" class="msgImg" v-if="type && type == 1" />' +
						'<img src="../assets/pages/img/errorMsg.png" class="msgImg" v-if="type && type == 2" />' +
						'<img src="../assets/pages/img/loading.gif" class="msgImg" v-if="type && type == 3" />' +
				    	'<p style="top:0px;margin:0 8px;">{{msg}}</p>' +
				    '</div>' +
				    '</div>',
				    methods: {
				        close: function () {
			                this.show = false;	            	
				        }
				    }
		        }
		    },
			ready: function() {

			},
			created: function() {}
		});
		C.vue = vueObj;

	}());
</script>