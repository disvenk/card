<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style type="text/css">
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
	
	#verifyCode {
		height: 34px;
		display: inline-block;
		float: right;
		margin-top: -34px;
		width: 100px;
	}
</style>

<div id="control">

	<div class="weui_dialog_alert" v-show="showTypeDialog">
		<div class="weui_mask" style="z-index:2;"></div>
		<div class="weui_dialog_order">
			<div class="full-height">
				<div class="pay_header">
					<span class="order_middle">支付方式</span>
					<img src="../assets/pages/img/close.png" class="closeImg" alt="关闭" @click="closePayDialog" />
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
					<p v-if="!chargeInfo.chargeMoney && !chargeInfo.chargeSettingId" style="margin: 15px 0;">
						<span>卡工本费</span>
						<span style="margin-left:15%;">10元</span>
					</p>
					<div v-if="chargeInfo.chargeMoney">
						<p>
							<span class="flex_money_left">充值金额:</span>
							<span class="flex_money_right">{{addCard.money}}元</span>
						</p>
						<p>
							<span class="flex_money_left">卡内余额:</span>
							<span class="flex_money_right">{{addCard.actualMoney}}元</span>
						</p>
						<p>
							<span class="flex_money_left">实收金额:</span>
							<span class="flex_money_right">{{addCard.money}}元</span>
						</p>
					</div>
					<div v-if="chargeInfo.chargeSettingId">
						<p>
							<span class="flex_money_left">充值金额:</span>
							<span class="flex_money_right">{{addCard.money}}元</span>
						</p>
						<p>
							<span class="flex_money_left">卡内余额:</span>
							<span class="flex_money_right">{{addCard.actualMoney}}元</span>
						</p>
						<p>
							<span class="flex_money_left">实收金额:</span>
							<span class="flex_money_right">{{addCard.money}}元</span>
						</p>
					</div>

					<input type="text" autofocus="autofocus" class="form-control" @change="payMoney" id="customerCode" v-model="authCode" placeholder="请扫描顾客出示的二维码" style="height:45px;font-size:20px;" maxlength="18">
				</div>
				<div class="pay_footer">
					<button class="btn btn-default" @click="confirmGetMoney" style="background: #199ed8;width: 80%;color: #fff;font-size:22px;">确认收款</button>
				</div>
			</div>
		</div>
	</div>

	<div class="portlet light bordered" v-if="chargePageShow">
		<div class="portlet-title">
			<div class="caption">
				<span class="caption-subject bold font-blue-hoki">充值</span>
			</div>
		</div>
		<div class="portlet-body">
			<div class="form-body">
				<div class="form-group row">
					<label class="col-md-1 control-label">卡信息<span style="color:#FF0000;">*</span></label>
					<div class="col-md-3">
						<input type="text" class="form-control" name="cardId" v-model="parameter.cardId" required="required" placeholder="请刷卡" v-bind:readonly="true" @keyup.enter.prevent="andTop">
					</div>
					<div>
						<input class="btn green" type="submit" @click="andTop" value="开始充值" />
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="portlet light bordered" v-else style="font-size: 16px;">
		<p>
			<span class="flex_charge_left">卡号:</span>
			<span class="flex_charge_right">{{cardInfo.cardId}}</span>
		</p>
		<p>
			<span class="flex_charge_left">储值卡类型:</span>
			<span class="flex_charge_right" v-if="cardInfo.cardType == 0">普通卡</span>
			<span class="flex_charge_right" v-if="cardInfo.cardType == 1">员工卡</span>
			<span class="flex_charge_right" v-if="cardInfo.cardType == 2">临时卡</span>
		</p>
		<p v-if="cardInfo.cardType == 1">
			<span class="flex_charge_left">单位名称:</span>
			<span class="flex_charge_right">{{cardInfo.companyName}}</span>
		</p>
		<p v-if="cardInfo.cardType != 2">
			<span class="flex_charge_left">姓名:</span>
			<span class="flex_charge_right">{{cardInfo.customerName}}</span>
		</p>
		<p v-if="cardInfo.cardType != 2">
			<span class="flex_charge_left">手机号:</span>
			<span class="flex_charge_right">{{cardInfo.telephone}}</span>
		</p>
		<p v-if="cardInfo.cardType != 2">
			<span class="flex_charge_left">身份证号码:</span>
			<span class="flex_charge_right" v-if="cardInfo.idCard">{{cardInfo.idCard}}</span>
			<span class="flex_charge_right" v-if="!cardInfo.idCard">无</span>
		</p>
		<p v-if="cardInfo.cardType != 2">
			<span class="flex_charge_left">折扣:</span>
			<span class="flex_charge_right" v-if="cardInfo.discountName">{{cardInfo.discountName}}</span>
			<span class="flex_charge_right" v-if="!cardInfo.discountName">无</span>
		</p>
		<p>
			<span class="flex_charge_left">账户余额:</span>
			<span class="flex_charge_right">{{cardInfo.remain}}元</span>
		</p>
		<p>
			<span class="flex_charge_left">充值金额:</span>
			<%--<input onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"--%>
               <%--onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"--%>
                <%--type="number" class="form-control text" placeholder="请输入充值金额" v-model="chargeInfo.chargeMoney">--%>
			<input
				   type="text" class="form-control text" placeholder="请输入充值金额" v-model="chargeInfo.chargeMoney">
		</p>
		<p v-if="cardInfo.cardType != 2">
			<span class="flex_charge_left">充值活动:</span>
			<span class="flex_charge_right" style="vertical-align: top;">
        		<label v-for="activity in activitys" style="display: block;" @click="selectCharge(activity)">
                    <input type="radio" name="activityId" value="{{activity.id}}">{{activity.labelText}}
                </label>
        	</span>
		</p>
		<div class="form-group text-center">
			<input class="btn green" type="submit" @click="confirmCharge" value="确定" />&nbsp;&nbsp;&nbsp;
			<a class="btn default" @click="closeChargePage">取消</a>
		</div>
	</div>
	<weui-dialog :show.sync="wMessage.show" :msg="wMessage.message" :type="wMessage.type"></weui-dialog>
</div>

<script>
	(function() {
		var $table = $(".table-body>table");
		var tb = $table.DataTable({
			ajax: {
				url: "card/rechargeData"
			}
		});
		var C = new Controller(null, tb);
		var vueObj = new Vue({
			el: "#control",
			mixins: [C.formVueMix],
			data: {
				showform: false,
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
				activitys: [],
				cardInfo: {},
				chargeInfo: {
					chargeMoney: null,
					chargeSettingId: null
				},
				parameter: {
					cardId: ''
				},
				addCard: {
					money: null,
					actualMoney: null,
					zsMoney: null
				},
				chargePageShow: true,
				showTypeDialog: false,
				authCode: null, //支付条码信息
				wMessage: {
					show: 0,
					message: "",
					type: null
				}, //加载中弹窗
			},
			events: {
				"successMessage": function(msg, time) {
					this.showMessage(msg, 1, time);
				},
				"errorMessage": function(msg, time) {
					this.showMessage(msg, 2, time);
				},
				"closeMessage": function() {
					this.closeMessage();
				},
				"loadingMessage": function(msg, time) {
					this.showMessage(msg, 3, time);
				}
			},
			methods: {
				showMessage: function(msg, type, time) {
					this.wMessage.show = true;
					this.wMessage.type = type;
					this.wMessage.message = msg;
					var that = this;
					setTimeout(function() {
						that.wMessage.show = 0;
					}, time || 1000);
				},
				closeMessage: function() {
					this.wMessage.show = false;
				},
				initialization: function() {
					this.parameter = {
						cardId: ''
					};
					this.chargeInfo = {
						chargeMoney: null,
						chargeSettingId: null
					};
					this.addCard = {
						money: null,
						zsMoney: null,
						actualMoney: null
					};
				},
				selectCharge: function(activity) {
					this.chargeInfo.chargeSettingId = activity.id;
					console.log(this.chargeInfo.chargeSettingId);
				},
				closeChargePage: function() {
					this.chargePageShow = true;
				},
				andTop: function() {
					var that = this;
					getCardNum(function(num) {
						if(num == null || num == '') {
							C.systemButtonNo('error', "连接读卡服务失败");
							return;
						} else {
							that.parameter.cardId = num;
							$.get("card/" + num, function(data) {
								if(data.data == '' || data.data == null) {
									C.systemButtonNo('error', '该卡号或手机号不存在');
								} else {
									that.chargePageShow = false;
									that.cardInfo = data.data;
									console.log(JSON.stringify(that.cardInfo));
								}
							});
						}
					})
				},
				confirmCharge: function() {
					var that = this;
					if(that.chargeInfo.chargeMoney <= 0 && !that.chargeInfo.chargeSettingId && that.chargeInfo.chargeMoney != null) {
						C.systemButtonNo('error', "充值金额必须大于0");
						return;
					}
					if(that.chargeInfo.chargeMoney && that.chargeInfo.chargeSettingId) {
						C.systemButtonNo('error', "充值金额跟活动只能选择一个");
						return;
					} else if(!that.chargeInfo.chargeMoney && !that.chargeInfo.chargeSettingId) {
						C.systemButtonNo('error', "充值金额或活动必选一项");
						return;
					}

					if(this.chargeInfo.chargeMoney && !that.chargeInfo.chargeSettingId) {
						this.addCard.money = parseInt(this.chargeInfo.chargeMoney);
						console.log(this.addCard.money);
						this.addCard.actualMoney = parseInt(this.cardInfo.remain) + this.addCard.money;
					} else if(!this.chargeInfo.chargeMoney && that.chargeInfo.chargeSettingId) {
						that.activitys.forEach(function(f) {
							if(that.chargeInfo.chargeSettingId == f.id) {
								that.addCard.money = f.chargeMoney;
								that.addCard.zsMoney = f.rewardMoney;
								that.addCard.actualMoney = f.chargeMoney + f.rewardMoney + parseInt(that.cardInfo.remain);
							}
						})
					}
					this.showTypeDialog = true;
				},
				payMoney: function() {
					var that = this;
					if(this.currentState == 3 || this.currentState == 4) {
						this.authCode = null;
						that.$dispatch("errorMessage", "请选择微信支付或支付宝支付", 3000);
						return;
					}
					if($('#customerCode').val().length != 18) {
						this.authCode = null;
						that.$dispatch("errorMessage", "请扫描正确的付款码", 3000);
						return;
					}
					var scanParam = {
						payType: this.currentState,
						authCode: this.authCode,
						paymentAmount: 0.01 //充值的总金额不包括工本费
					};
					//发起支付请求						
					that.scanCodePayment(scanParam, function(result) {
						if(result.isPolling) {
							that.$dispatch("loadingMessage", "正在支付中，请稍候", 30000);
							//进入轮循
							var payaram = {
								payType: that.currentState,
								outTradeNo: result.outTradeNo
							};
							//表示当前轮循次数
							var number = 0;
							var time = setInterval(function() {
								that.confirmPayment(payaram, function(conResult) {
									number += 1;
									console.log(number);
									if(number > 6) {
										//轮循超时，退出轮循                                            
										clearInterval(time);
										that.$dispatch("errorMessage", "支付超时，正在撤销支付请求", 3000);
										//调用撤销订单接口
										that.revocationOfOrder(payaram, function(revokeResult) {
											if(revokeResult.success) {
												that.$dispatch("successMessage", "撤销订单成功", 3000);
											} else {
												that.$dispatch("errorMessage", revokeResult.message, 3000);
											}
										});
										return;
									}

									if(!conResult.isPolling && !conResult.success) {
										//支付出错，退出轮循
										clearInterval(time);
										that.$dispatch("errorMessage", conResult.message, 3000);
									} else if(!conResult.isPolling && conResult.success) {
										//支付成功，退出轮循
										clearInterval(time);
										//调用充值接口插入充值记录
										that.$dispatch("closeMessage");
										var options = {
											chargeMoney: that.addCard.money,
											rewardMoney: that.addCard.zsMoney,
											chargeBalance: that.addCard.money,
											rewardBalance: that.addCard.zsMoney,
											payType: that.currentState,
											companyId: that.cardInfo.companyId,
											chargeSettingId: that.chargeInfo.chargeSettingId,
											cardId: that.cardInfo.cardId,
											payData: conResult.payData,
											cardType: parseInt(that.cardInfo.cardType)
										};
										console.log(JSON.stringify(options));
										//将充值明细传给三哥
										$.ajax({
											type: "post",
											url: 'card/addCardRecharge',
											data: JSON.stringify(options),
											contentType: "application/json",
											success: function(data) { //成功后返回
												if(data.success) {
													that.showTypeDialog = false;
													that.initialization(); //清空信息
													that.$dispatch("successMessage", "充值成功", 3000);
												} else {
													that.$dispatch("errorMessage", "充值失败", 3000);
												}
											},
											error: function() { //失败后执行
												that.$dispatch("errorMessage", "充值失败", 3000);
											}
										});
										console.log(conResult.payData);
									}
								})
							}, 5000);

						} else {
							that.$dispatch("errorMessage", result.message);
						}
					});
				},
				scanCodePayment: function(param, cbk) {
					$.post("rechargePay/scanCodePayment", param, function(result) {
						cbk && cbk(result);
					});
				},
				confirmPayment: function(param, cbk) {
					$.post("rechargePay/confirmPayment", param, function(result) {
						cbk && cbk(result);
					});
				},
				revocationOfOrder: function(param, cbk) {
					$.post("rechargePay/revocationOfOrder", param, function(result) {
						cbk && cbk(result);
					});
				},
				confirmGetMoney: function() {
					var that = this;
					if(this.currentState == 1 || this.currentState == 2) {
						that.$dispatch("errorMessage", '请扫描手机二维码支付');
						return;
					};
					var options = {
						chargeMoney: this.addCard.money,
						rewardMoney: this.addCard.zsMoney,
						chargeBalance: this.addCard.money,
						rewardBalance: this.addCard.zsMoney,
						payType: this.currentState,
						companyId: this.cardInfo.companyId,
						chargeSettingId: this.chargeInfo.chargeSettingId,
						cardId: this.cardInfo.cardId,
						cardType: parseInt(that.cardInfo.cardType)
					};
					console.log(JSON.stringify(options));
					that.$dispatch("loadingMessage", "正在支付中，请稍候", 30000);
					$.ajax({
						type: "post",
						url: 'card/addCardRecharge',
						data: JSON.stringify(options),
						contentType: "application/json",
						success: function(data) { //成功后返回
							if(data.success) {
								that.initialization(); //清空信息
								that.showTypeDialog = false;
								that.$dispatch("successMessage", "充值成功", 3000);
							} else {
								that.$dispatch("errorMessage", "充值失败", 3000);
							}
						},
						error: function() { //失败后执行
							that.$dispatch("errorMessage", "充值失败", 3000);
						}
					});
				},
				payByMode: function(f) {
					$('#customerCode').focus();
					this.currentState = f.state;
				},
				closePayDialog: function() {
					this.showTypeDialog = false;
				}
			},
			components: {
				"weui-dialog": {
					props: ['show', 'msg', 'type'],
					template: '<div class="weui_loading_toast" v-if="show">' +
						'<div class="weui_mask_transparent"></div>' +
						'<div class="weui_toast msg-dialog">' +
						'<img src="../assets/pages/img/correctImg.png" class="msgImg" v-if="type && type == 1" />' +
						'<img src="../assets/pages/img/errorMsg.png" class="msgImg" v-if="type && type == 2" />' +
						'<img src="../assets/pages/img/loading.gif" class="msgImg" v-if="type && type == 3" />' +
						'<p style="top:0px;">{{msg}}</p>' +
						'</div>' +
						'</div>',
					methods: {
						close: function() {
							this.show = false;
						}
					}
				}
			},
			ready: function() {
				var that = this;
				$.get("card/activity/list", function(data) {
					that.activitys = data.data;
					console.log(JSON.stringify(that.activitys));
				});
			},
			created: function() {}
		});
		C.vue = vueObj;

	}());
</script>