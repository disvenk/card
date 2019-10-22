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
	p {
		margin: 12px 0;
	}
	.weui_dialog_order {
	    position: fixed;
	    font-size: 18px;
	    z-index:3;
	    width:35%;
	    top: 15%;
	    left: 0;
	    right: 0;
	    margin: auto;
	    text-align: center;
	    background: #fff;
	    border-radius: 5px;
	    font-family: "微软雅黑";
	}
	.pay_header {
	 	position: relative; 
	    width: 100%;
	    text-align: center;
	    display: table;
	    font-size: 20px;
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
	.pay_footer {
	    position: relative;
	    display: -webkit-flex;
	    display: flex;
	    -webkit-justify-content: space-around;
	    justify-content: space-around;
	    padding: 10px 10%;
	    font-weight: bold;
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
    <!-- 模态框（Modal） -->
	<div class="weui_dialog_alert" v-show="pinDialogShow">
        <div class="weui_mask" style="z-index:2;"></div>
        <div class="weui_dialog_order">
	        <div class="full-height">
	        <div class="pay_header">
		        <span class="order_middle">销卡退款明细</span>
	        </div>
	        <div class="paymode_body">	        	
		        <p class="flex_pin_item" v-for="f in refundDetails">
		        	<span v-if="f.payType == 1">微信退款: {{f.balanceMoney}}元</span>
		        	<span v-if="f.payType == 2">支付宝退款: {{f.balanceMoney}}元</span>
		        	<span v-if="f.payType == 3">现金退款: {{f.balanceMoney}}元</span>
		        	<span v-if="f.payType == 4">支票退款: {{f.balanceMoney}}元</span>
                    <span v-if="f.payType == 5">天子星退款: {{f.balanceMoney}}元</span>
		        </p>
	        </div>
	        <div class="pay_footer">
	        	<button class="btn btn-default" style="background: #199ed8;width: 40%;color: #fff;font-size:18px;" @click="pinCard">确定销卡</button>
	        	<button class="btn btn-default" style="background: #199ed8;width: 40%;color: #fff;font-size:18px;" @click="close">取消</button>
	        </div>
	        </div>
        </div>
    </div>


    <div class="portlet light bordered" v-if="pinPageShow">
        <div class="portlet-title">
            <div class="caption">
                <span class="caption-subject bold font-blue-hoki">读卡</span>
            </div>
        </div>
        <div class="portlet-body">
            <div class="form-body">
                <div class="form-group row">
                    <label class="col-md-1 control-label">卡号<span style="color:#FF0000;">*</span></label>
                    <div class="col-md-3">
                        <input type="text" class="form-control" name="cardId" v-model="parameter.cardId"
                               required="required" placeholder="请刷卡" v-bind:readonly="true" @keyup.enter.prevent="save">
                    </div>
                    <div>
                        <input class="btn green" type="submit" @click="save" value="开始销卡"/>
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
        <p v-if="cardInfo.cardType != 2">
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
        <p v-if="cardInfo.cardType == 1">
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
        <p style="color: #0000FF;">
        	<span class="flex_charge_left">卡工本费:</span>
        	<span class="flex_charge_right">10元</span>
        </p>
        <p style="color: red;">
        	<span class="flex_charge_left">参与优惠不可退金额:</span>
			<span class="flex_charge_right">{{cardInfo.notReturnAmount}}元</span>
        </p>
        <p style="color: #0000FF;">
        	<span class="flex_charge_left">可退金额:</span>
        	<span class="flex_charge_right">{{cardInfo.returnAmount + 10}}元</span>
        </p>
        <div class="form-group text-center">
            <input class="btn green" type="submit" @click="confirmPinCard" value="销卡"/>&nbsp;&nbsp;&nbsp;
            <a class="btn default" @click="closeChargePage">取消</a>
        </div>
    </div>
</div>

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
            cardInfo:{},
            pinPageShow:true,
            parameter:{
            	cardId:''
            },
            pinDialogShow:false,
            refundDetails:[],		//退款明细
            refundMoney:{
            	wechatMoney:null,
				aliMoney:null,
				cashMoney:null,
				chequeMoney:null,
                starMoney:null
            }
        },
        methods: {
            create:function () {
                this.parameter={
                    cardId:''
                };
            },
            save:function(){
                var that=this;
				getCardNum(function(num){
	                if(num == null || num == ''){
						C.systemButtonNo('error',"连接读卡服务失败");
	                    return;
	                }else{					
			            that.parameter.cardId = num;;
			            $.get("card/pinCardInfo/" + num, function (data) {
			                if (data.data == '' || data.data == null){
			                    C.systemButtonNo('error','该卡号或手机号不存在');
			                }else {                               
			                    that.cardInfo = data.data;  
			                    that.pinPageShow=false;
			                    console.log(JSON.stringify(that.cardInfo));
			                }
			            })
			        }
	            })
            },
            confirmPinCard:function(){  
            	var that = this;
            	//如果不可退金额大于0，则不能销卡
            	if(this.cardInfo.notReturnAmount > 0){
            		C.systemButtonNo('error','该卡号存在充值活动金额，不能销卡！');
            		return;
            	}
                $.ajax({
                    type:"get",
                    url:'card/pinCardMoney',	                    
                    data:{
                    	cardId:that.parameter.cardId                    	
                    },
                    success:function(data){ //成功后返回
                        if (data.success){
                        	that.refundDetails = data.data;
                        	that.refundDetails.forEach(function(f){
			                	if(f.payType == 1){
			                		that.refundMoney.wechatMoney = f.balanceMoney;
			                	}else if(f.payType == 2){
			                		that.refundMoney.aliMoney = f.balanceMoney;
			                	}else if(f.payType == 3){
			                		that.refundMoney.cashMoney = f.balanceMoney;
			                	}else if(f.payType == 4){
			                		that.refundMoney.chequeMoney = f.balanceMoney;
			                	}else if(f.payType == 5){
                                    that.refundMoney.starMoney = f.balanceMoney;
                                }
			                })
                        	console.log(JSON.stringify(that.refundMoney));
                        	C.systemButtonNo('success','查询成功');	
                        	that.pinDialogShow = true;
                        }else {
                        	C.systemButtonNo('error','查询失败');	
                        }
                    },
                    error: function(){ //失败后执行
                        that.$dispatch("errorMessage","查询失败",3000);
                    }
                });                  
                
            },
            close:function(){
            	this.pinDialogShow = false;
            },
            closeChargePage:function(){
            	this.pinPageShow = true;
            },
            pinCard:function(){
            	var that = this;
            	
            	$.ajax({
					type: "get",
					url: 'card/pinCard',
					data: {
						cardId: that.parameter.cardId,							
						wechatMoney: that.refundMoney.wechatMoney,
						aliMoney: that.refundMoney.aliMoney,
						cashMoney: that.refundMoney.cashMoney,
						chequeMoney: that.refundMoney.chequeMoney,
                        starMoney: that.refundMoney.starMoney,
						cardType: parseInt(that.cardInfo.cardType)
					},
					contentType: "application/json",
					success: function(data) { //成功后返回
						if(data.success) {
							C.systemButtonNo('success', '销卡成功');
							that.pinDialogShow = false;
							that.pinPageShow = true;
						} else {
							C.systemButtonNo('error', data.message);
						}
					},
					error: function() { //失败后执行
						C.systemButtonNo('error', '销卡失败');
					}
				});
            }
        },
        ready:function () {
            
        },
        created: function () {
        }
    })
    
    C.vue = vueObj;
}());
</script>
