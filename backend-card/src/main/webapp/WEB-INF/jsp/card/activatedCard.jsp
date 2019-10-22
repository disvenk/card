<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style type="text/css">
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
</style>

<div id="control">

    <div class="portlet light bordered" v-if="activatedPageShow">
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
                        <input type="text" class="form-control" name="cardId" v-model="parameter.cardId" placeholder="请输入卡号" @keyup.enter.prevent="save">
                    </div>
                    <div>
                        <input class="btn green" type="submit" @click="save" value="开始激活"/>
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
        <p v-if="cardInfo.cardType != null">
        	<span class="flex_charge_left">储值卡类型:</span>
        	<span class="flex_charge_right" v-if="cardInfo.cardType == 0">普通卡</span>
        	<span class="flex_charge_right" v-if="cardInfo.cardType == 1">员工卡</span>
        	<span class="flex_charge_right" v-if="cardInfo.cardType == 2">临时卡</span>
        </p>
        <p v-if="cardInfo.cardType != 2 && cardInfo.companyName != null">
        	<span class="flex_charge_left">单位名称:</span>
        	<span class="flex_charge_right">{{cardInfo.companyName}}</span>
        </p>
        <p v-if="cardInfo.cardType != 2 && cardInfo.customerName != null">
        	<span class="flex_charge_left">姓名:</span>
        	<span class="flex_charge_right">{{cardInfo.customerName}}</span>
        </p>
        <p v-if="cardInfo.cardType != 2 && cardInfo.telephone != null">
        	<span class="flex_charge_left">手机号:</span>
        	<span class="flex_charge_right">{{cardInfo.telephone}}</span>
        </p>
        <p v-if="cardInfo.cardType == 1 && cardInfo.idCard != null">
        	<span class="flex_charge_left">身份证号码:</span>
        	<span class="flex_charge_right">{{cardInfo.idCard}}</span>
        </p>
        <p v-if="cardInfo.cardType != 2 && cardInfo.discountName != null">
        	<span class="flex_charge_left">折扣:</span>
        	<span class="flex_charge_right">{{cardInfo.discountName}}</span>
        </p>
        <p>
        	<span class="flex_charge_left">获取新卡号:</span>
        	<input type="text" class="form-control text" v-model="newCardId" placeholder="请刷卡" readonly="">
        	<button type="button" style="margin-top: -3px;margin-left: -5px;" class="btn btn-primary" @click="getCardInfo">读卡</button>
        </p>
        <p>
        	<span class="flex_charge_left">账户余额:</span>
        	<input type="text" class="form-control text" v-model="cardInfo.remain">
        </p>
        <p>
        	<span class="flex_charge_left">充值本金余额:</span>
        	<input type="text" class="form-control text" v-model="cardInfo.rewardMoney">
        </p>
        <p>
        	<span class="flex_charge_left">充值赠送余额:</span>
        	<input type="text" class="form-control text" v-model="cardInfo.chargeMoney">
        </p>
        <p>
        	<span class="flex_charge_left">工本费:</span>
			<span class="flex_charge_right">10元</span>	
        </p>
        <div class="form-group text-center">
            <input class="btn green" type="submit" @click="confirmActivated" value="激活"/>&nbsp;&nbsp;&nbsp;
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
            activatedPageShow:true,
            newCardId:'',		//替代天子星新卡号
            parameter:{
            	cardId:''
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
                if(that.parameter.cardId == ''){
                	C.systemButtonNo('error', '请输入卡号');
                	return;
                }
	            $.ajax({
					type: "get",
					url: "card/activatedCardInfo/" + that.parameter.cardId, 
					contentType: "application/json",
					success: function(data) { //成功后返回
						if(data.success) {
							C.systemButtonNo('success', '查询成功');
							that.cardInfo = data.data;
							that.activatedPageShow=false;
						} else {
							C.systemButtonNo('error', data.message);
						}
					},
					error: function() { //失败后执行
						C.systemButtonNo('error', '查询失败');
					}
				});		   
            },
            confirmActivated:function(){            	  
            	var that=this;
            	var options = {
            		remain:	this.cardInfo.remain,
            		chargeMoney: this.cardInfo.chargeMoney,
            		rewardMoney: this.cardInfo.rewardMoney,
            		cardCustomerId: this.cardInfo.cardCustomerId,
            		cardId:this.newCardId
            	}
	            $.ajax({
					type: "post",
					url: "card/activatedCard", 					
					contentType: "application/json",
					data:JSON.stringify(options),
					success: function(data) { //成功后返回
						if(data.success) {
							C.systemButtonNo('success', '激活成功');
							that.parameter.cardId = '';
							this.newCardId = '';
							that.activatedPageShow=true;
						} else {
							C.systemButtonNo('error', data.message);
						}
					},
					error: function() { //失败后执行
						C.systemButtonNo('error', '激活失败');
					}
				});	
            },
            closeChargePage:function(){
            	this.activatedPageShow = true;
            	this.parameter.cardId = '';
            	this.newCardId = '';
            },
            getCardInfo:function(){
            	var that = this;
            	getCardNum(function(num){
	                if(num == null || num == ''){
						C.systemButtonNo('error',"连接读卡服务失败");
	                    return;
	                }else{
	                	that.newCardId = num;
	                	console.log(num);
	                }
	            })
            	
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
