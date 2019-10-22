<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
	.weui_mask {
	  	position: fixed;
	  	z-index: 1;
	  	width: 100%;
	  	height: 100%;
	  	top: 0;
	  	left: 0;
	  	background: rgba(0, 0, 0, 0.6);
	}
	p {
		margin: 8px 0;
	}
	.weui_dialog_order {
	    position: fixed;
	    font-size: 20px;
	    z-index:3;
	    width:45%;
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
		margin: 8px 8px 8px 0px;;
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
</style>
<div id="control">

	<div class="weui_dialog_alert" v-show="showTypeDialog">
        <div class="weui_mask" style="z-index:2;"></div>
        <div class="weui_dialog_order">
	        <div class="full-height">
	        <div class="pay_header">
		        <span class="order_middle">支付方式</span>
		        <!--<img src="../assets/pages/img/close.png" class="closeImg" alt="关闭" @click="closePayDialog"/>-->
	        </div>
	        <div class="paymode_body">		        
		        <div class="weui_pay">
			        <div class="weui_flex_item" @click="payByMode(f)" v-for="f in payModeList" :class="{active:f.state == currentState}">
			            <img v-if="f.state == 1" src="../assets/pages/img/wxpay.png" class="payImg" alt="微信支付" />
			            <img v-if="f.state == 2" src="../assets/pages/img/alipay.png" class="payImg" alt="支付宝"/ >
			            <img v-if="f.state == 3" src="../assets/pages/img/cashpay.png" class="payImg" style="border-radius: 50%;" alt="现金支付" />
			            <img v-if="f.state == 4" src="../assets/pages/img/bank.png" class="payImg" style="border-radius: 50%;" alt="支票支付" />
			            <span class="headerText">{{f.name}}</span>
			        </div>		        
		        </div>
		        <p v-if="!parameter.remain && !parameter.chargeSettingId" style="margin: 15px 0;">
			        <span>卡工本费</span>
			        <span style="margin-left:15%;">10元</span>
		        </p>
		        <div v-if="parameter.remain">
		        	<p>
				        <span class="flex_money_left">卡押金:</span>
				        <span class="flex_money_right" style="color:#199ed8;margin-left: 20px;">0.01元</span>
			        </p>
		        	<p>
				        <span class="flex_money_left">充值金额:</span>
				        <span  class="flex_money_right">{{addCard.money}}元</span>
			        </p>
			        <p>
				        <span class="flex_money_left">卡内实存:</span>
				        <span class="flex_money_right">{{addCard.money}}元</span>
			        </p>
			        <p>
				        <span class="flex_money_left">实收金额:</span>
				        <span  class="flex_money_right">{{addCard.money+10}}元</span>
			        </p>
		        </div>
		        <div v-if="parameter.chargeSettingId">
		        	<p>
				        <span class="flex_money_left">卡押金:</span>
				        <span class="flex_money_right" style="color:#199ed8;margin-left: 20px;">10元</span>
			        </p>
		        	<p>
				        <span class="flex_money_left">充值金额:</span>
				        <span class="flex_money_right">{{addCard.money}}元</span>
			        </p>
			        <p>
				        <span class="flex_money_left">卡内金额:</span>
				        <span class="flex_money_right">{{addCard.actualMoney}}元</span>
			        </p>
			        <p>
				        <span class="flex_money_left">实收金额:</span>
				        <span  class="flex_money_right">{{addCard.money+10}}元</span>
			        </p>
		        </div>
		        
		        <input type="text" autofocus="autofocus" class="form-control" @change="payMoney" id="customerCode" v-model="authCode"
		        	 placeholder="请扫描顾客出示的二维码" style="height:45px;font-size:20px;width: 100%;" maxlength="18">
	        </div>
	        <div class="pay_footer">
	        	<button class="btn btn-default" @click="confirmCharge" style="background: #199ed8;width: 80%;color: #fff;font-size:22px;">确认收款</button>
	        </div>
	        </div>
        </div>
    </div>
	
    <div class="portlet light bordered" style="max-height: 80%;overflow-y: scroll;">           
        
        <div class="portlet-body">
            <div class="form-body">
                <div class="form-group row">
                    <label class="col-md-1 control-label">卡号<span style="color:#FF0000;">*</span></label>
                    <div class="col-md-4" style="width: 330px;">
                        <input type="text" class="form-control" name="cardId" v-model="parameter.cardId"
                               required="required" maxlength="16" v-bind:readonly="true" placeholder="请刷卡">
                        <button type="button" id="verifyCode" class="btn btn-primary" @click="getCardId">读卡</button>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-md-1 control-label">开卡类型<span style="color:#FF0000;">*</span></label>
                    <div class="col-md-8">
                        <label class="radio-inline">
                            <input type="radio" name="cardType" checked value="0" v-model="parameter.cardType">普通卡
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="cardType" value="1" v-model="parameter.cardType">员工卡
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="cardType" value="2" v-model="parameter.cardType">临时卡
                        </label>
                    </div>
                </div>               
                <div v-if="parameter.cardType == 1">
                    <div class="form-group row" >
                        <label class="col-md-1 control-label">公司<span style="color:#FF0000;">*</span></label>
                        <div class="col-md-3">
                            <select name="companyId" v-model="parameter.companyId" class="bs-select form-control">
                                <option readonly selected value>请选择</option>
                                <option  v-for="company in companys" value="{{company.id}}">
                                    {{company.companyName}}
                                </option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="form-group row" v-if="parameter.cardType != 2">
                    <label class="col-md-1 control-label">姓名<span style="color:#FF0000;">*</span></label>
                    <div class="col-md-3">
                        <input type="text" class="form-control" name="customerName" v-model="parameter.customerName"
                               required="required">
                    </div>
                </div>     
                <div class="form-group row" v-if="parameter.cardType != 2">
                    <label class="col-md-1 control-label">手机号<span style="color:#FF0000;">*</span></label>
                    <div class="col-md-4" style="width: 330px;">
                        <input type="text" class="form-control" name="telephone" v-model="parameter.telephone"
                               required="required" >
                        <button v-if="parameter.cardType == 0" type="button" id="verifyCode" class="btn btn-primary"
                        	 v-bind="{disabled:remainTime>0}" @click="getCode">{{remainTime||"获取验证码"}}</button>
                    </div>
                </div>
                <div v-if="parameter.cardType == 1">
                    <div class="form-group row">
                        <label class="col-md-1 control-label">身份证号<span style="color:#FF0000;">*</span></label>
                        <div class="col-md-3">
                            <input type="text" class="form-control" name="idCard" v-model="parameter.idCard"
                                   required="required">
                        </div>
                    </div>
                </div>     
                <div v-if="parameter.cardType == 0">
	                <div class="form-group row">
	                    <label class="col-md-1 control-label">验证码<span style="color:#FF0000;">*</span></label>
	                    <div class="col-md-3">
	                        <input type="text" class="form-control" name="code" v-model="parameter.code"
	                               required="required">
	                    </div>
	                </div>                             
				</div>
                <div class="form-group row" v-if="parameter.cardType != 2">
                    <label class="col-md-1 control-label">折扣<span style="color:#FF0000;">*</span></label>
                    <div class="col-md-3">
                        <select name="discountId" v-model="parameter.disCountId" class="bs-select form-control" @change="selectDiscount">
                            <option readonly selected value>请选择</option>
                            <option v-for="discount in discounts" value="{{discount.id}}">
                                {{discount.discountName}}
                            </option>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-md-1 control-label" >充值金额</label>
                    <div class="col-md-3">
                        <input onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                               onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                               type="text" maxlength="7"  class="form-control" v-model="parameter.remain" required="required">
                    </div>
                </div>
                <div class="form-group row" v-if="parameter.cardType != 2">
                    <label class="col-md-1 control-label">充值活动</label>
                    <div class="col-md-3">
                        <label v-for="activity in activitys" style="display: block;" @click="selectCharge(activity)">
                            <input type="radio" name="activityId" value="{{activity.id}}">{{activity.labelText}}
                        </label>
                    </div>
                </div>
                <div class="form-group text-center">
                    <input class="btn green" type="submit" @click="save" value="确定开卡"/>&nbsp;&nbsp;&nbsp;
                    <!--<a class="btn default" @click="detailsCl">取消</a>-->
                </div>
            </div>
        </div>             	    
    </div>
	<weui-dialog :show.sync="wMessage.show" :msg="wMessage.message" :type="wMessage.type" ></weui-dialog>
</div>

<style>
	.form-control {
		width: 300px;
	}
    #verifyCode {
        height: 34px;
        display: inline-block;
        float: right;
        margin-top: -34px;
        width: 100px;
    }
</style>
<script>
	
(function () {
    var $table = $(".table-body>table");
    var tb = $table.DataTable({
        ajax : {
            url : "card/create"
        }
    });
    var C = new Controller(null, tb);	

    var vueObj = new Vue({
        el: "#control",
        data: {
            showTypeDialog:false,
            sendCodeBtn:true,
            remainTime:0,
            currentState: 1,		//支付方式
            payModeList: [
                {
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
            companys:[],		//公司列表
            discounts:[],		//折扣列表
            activitys:[],		//充值列表
            parameter:{
                cardId:'',	//卡号
                idCard:'',					//身份号号码
                customerName:'',			//用户名称
                telephone:'',				//手机号	                       		                        
                cardType:'',				//开卡类型	                        
                disCountId:null,			//折扣Id
                chargeSettingId:null,		//充值活动ID
                companyId:'',				//公司ID
                remain:null,				//充值金额
                code:''						//验证码
            },
            addCard:{
            	money:0,
            	actualMoney:0,
            	zsMoney:0
            },
            authCode:null,			//支付条码信息
            wMessage: {show: 0, message: "",type:null}, //加载中弹窗
        },
        computed:{
        	countId: function () {
	            for (var i = 0; i < this.discounts.length; i++) {
	                var c = this.discounts[i];
	                if (c.id == this.parameter.disCountId) {
	                    return c.id;
	                }
	            }
	        }
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
        	getCardId:function(){
        		var that = this;
        		getCardNum(function(num){
	                if(num == null || num == ''){
						C.systemButtonNo('error',"连接读卡服务失败");
	                    return;
	                }else{
	                	that.parameter.cardId = num;
	                	console.log(num);
	                }
	            })
        	},
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
        	initialization:function(){
        		this.parameter = {
                    cardId:'',					//卡号
                    idCard:'',					//身份号号码
                    customerName:'',			//用户名称
                    telephone:'',				//手机号	                       		                        
                    cardType:'',				//开卡类型	                        
                    disCountId:'',				//折扣Id
                    chargeSettingId:null,		//充值活动ID
                    companyId:'',				//公司ID
                    remain:null,				//充值金额
                    code:''						//验证码
                }
        	},
            selectDiscount:function() {
				console.log(this.parameter.disCountId);				
            },
            selectCharge:function(activity){
            	this.parameter.chargeSettingId = activity.id;
            	console.log(this.parameter.chargeSettingId);
            },
            save:function(){
                var that=this; 
                var myreg = /^(((13[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;

                //开卡各种验证
                if(that.parameter.cardType != 2){
                	if(!that.parameter.cardId){
	                	C.systemButtonNo('error',"请输入卡号");
	                	return;
	                }else if(that.parameter.cardId.length != 16){
	                	C.systemButtonNo('error',"请录入正确的卡号");
	                	return;
	                }else if(!that.parameter.customerName){
	                	C.systemButtonNo('error',"请输入姓名");
	                	return;	                        	
	                }else if(!that.parameter.companyId && that.parameter.cardType == 1){
	                	C.systemButtonNo('error',"请选择公司");
	                	return;
	                }else if(!that.parameter.telephone){
	                	C.systemButtonNo('error',"请输入手机号");                	
	                	return;
	                }else if(!myreg.test(this.parameter.telephone)){
	                	C.systemButtonNo('error',"请输入正确的手机号");
	                	return;
	                }else if(!that.parameter.idCard && that.parameter.cardType == 1){
	                	C.systemButtonNo('error',"请输入身份证号");
	                	return;
	                }else if(that.parameter.idCard.length != 18 && that.parameter.cardType == 1){
	                	C.systemButtonNo('error',"身份证号长度不符");
	                	return;
	                }else if(!that.parameter.code && that.parameter.cardType == 0){
	                	C.systemButtonNo('error',"请输入验证码");
	                	return;
	                }else if(!that.parameter.disCountId && that.discounts.length>0){
	                	C.systemButtonNo('error',"请选择折扣活动");
	                	return;
	                }else if(that.parameter.remain && that.parameter.chargeSettingId != null){
	                	C.systemButtonNo('error',"金额跟活动只能选择一个");
	                	return;
	                }else if(!this.sendCodeBtn && that.parameter.cardType == 0){
	                	C.systemButtonNo('error',"请发送验证码");
	                	return;
	                }	                
                }          	

                if(this.parameter.remain && !that.parameter.chargeSettingId){
        			this.addCard.money = parseInt(this.parameter.remain);
        			this.addCard.actualMoney = parseInt(this.parameter.remain);
        		}else if(!this.parameter.remain && that.parameter.chargeSettingId){
        			that.activitys.forEach(function(f){
        				if(that.parameter.chargeSettingId == f.id){
        					that.addCard.money = f.chargeMoney;   
        					that.addCard.zsMoney = f.rewardMoney;
        					that.addCard.actualMoney = f.chargeMoney + f.rewardMoney;
        				}
        			})
        		}                       
                //插入对象
                var options = {
                	cardId:	this.parameter.cardId, 
					idCard:	this.parameter.idCard,
					customerName: this.parameter.customerName,
					telephone: this.parameter.telephone,
					code:this.parameter.code,
					cardType: this.parameter.cardType,
					discountId: this.countId,
					companyId: this.parameter.companyId
                }                        
                
                console.log(this.countId);
                console.log(JSON.stringify(options));
                
                $.ajax({
                    type:"POST",
                    url:'card/addCard',
                    contentType:"application/json",
                    datatype: "json",
                    data:JSON.stringify(options),
                    success:function(data){ //成功后返回
                        if (data.success){
                            C.systemButtonNo('success','开卡成功');   
                            that.showTypeDialog = true;
                            //that.initialization();
                    	}else{
                    		C.systemButtonNo('success',data.message);
                    	}
                    },
                    error: function(){ //失败后执行
                        C.systemButtonNo('error','开卡失败');
                    }
                });
            },
            cutRemainTime: function () {
	            if (this.remainTime > 0) {
	                this.remainTime--;
	                var that = this;
	                setTimeout(function () {
	                    that.cutRemainTime();
	                }, 1000);
	            }
	        },
            getCode:function(){
                var that=this;
				if(!this.sendCodeBtn){
	                return;
	            };
                var myreg = /^(((13[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
                if(!this.parameter.telephone){
                	C.systemButtonNo('error','请填写手机号');
                	return;
                }else if(!myreg.test(this.parameter.telephone)){
                	C.systemButtonNo('error','请输入有效的手机号码');
                	return;
                }
                this.sendCodeBtn = false;

                $.ajax({
                    type:"GET",
                    url:'sendCode/2/' + this.parameter.telephone,
                    contentType:"application/json",
                    success:function(data){ //成功后返回
                        if (data.success){
                            C.systemButtonNo('success','发送成功');
                            that.sendCodeBtn = true;
		                    that.remainTime = 60;
		                    that.cutRemainTime();
                        }else {
                            C.systemButtonNo('error',data.message);
                        }
                    },
                    error: function(){ //失败后执行
                        C.systemButtonNo('error','发送失败');
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

            	var actualCount = parseInt(this.addCard.money+10);
            	var scanParam = {
            		payType:this.currentState,
					authCode:this.authCode,
					paymentAmount: 0.01		//充值的总金额包括工本费
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
					            		chargeMoney:that.addCard.money,
					            		rewardMoney:that.addCard.zsMoney,
					            		chargeBalance:that.addCard.money,
					            		rewardBalance:that.addCard.zsMoney,
					            		cost:0.01,
					            		payType:that.currentState,
					            		companyId:that.parameter.companyId,
					            		chargeSettingId:that.parameter.chargeSettingId,
					            		cardId:that.parameter.cardId,
					            		payData:conResult.payData,
					            		cardType: parseInt(that.parameter.cardType)
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
					                        	that.showTypeDialog = false;
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
            confirmCharge:function(){
            	var that = this;
				if(this.currentState == 1 || this.currentState == 2){
					that.$dispatch("errorMessage", '请扫描手机二维码支付');
            		return;
            	};
            	var options = {
            		chargeMoney:this.addCard.money,
            		rewardMoney:this.addCard.zsMoney,
            		chargeBalance:this.addCard.money,
            		rewardBalance:this.addCard.zsMoney,
            		cost:0.01,
            		payType:this.currentState,
            		companyId:this.parameter.companyId,
            		chargeSettingId:this.parameter.chargeSettingId,
            		cardId:this.parameter.cardId,
            		cardType: parseInt(this.parameter.cardType),
            	}
            	
            	//当支付方式选择支票或现金时
            	that.$dispatch("loadingMessage","正在支付中，请稍候",30000);
            	if(this.currentState == 3 || this.currentState == 4){
            		$.ajax({
	                    type:"post",
	                    url:'card/addCardRecharge',
	                    data:JSON.stringify(options),
	                    contentType:"application/json",
	                    success:function(data){ //成功后返回
	                        if (data.success){	       
	                        	that.initialization();
	                            that.showTypeDialog = false;
	                            that.$dispatch("successMessage","充值成功",3000);
	                        }else {
	                            that.$dispatch("errorMessage","充值失败",3000);
	                        }
	                    },
	                    error: function(){ //失败后执行
	                        that.$dispatch("errorMessage","充值失败",3000);
	                    }
	                });
            	}
            },
            closePayDialog:function () {
                this.showTypeDialog=false;
            },
            payByMode: function (f) {
                $('#customerCode').focus();
                this.currentState = f.state;
            }
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
        ready:function () {
            var that=this;
            $.get("cardCompany/list", function (data) {
                that.companys = data.data;
                console.log(JSON.stringify(that.companys));
            });
            $.get("cardDiscount/list", function (data) {
                that.discounts = data.data;
                console.log(JSON.stringify(that.discounts));
            });
            $.get("card/activity/list", function (data) {
                that.activitys = data.data;
                console.log(JSON.stringify(that.activitys));
            });
        }
    })
    
    C.vue = vueObj;
}());

</script>