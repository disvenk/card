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
	.table.table-bordered tr th{
		text-align: center;
		line-height: 2.3;
	}
	p {
		margin: 8px 0;
	}
	.weui_dialog_order {
	    position: fixed;
	    font-size: 20px;
	    z-index:3;
	    width:30%;
	    top: 15%;
	    left: 0;
	    right: 0;
	    margin: auto;
	    text-align: center;
	    background: #fff;
	    border-radius: 5px;
	    font-family: "微软雅黑";
	}
</style>
<div id="control">
    
    <div class="portlet light bordered">
        <div class="portlet-title">
            <div class="caption">
                <span class="caption-subject bold font-blue-hoki">挂失</span>
            </div>
        </div>
        <div class="portlet-body">
            <div class="form-body">
                <div class="form-group row">
                    <label class="col-md-1 control-label">手机号<span style="color:#FF0000;">*</span></label>
                    <div class="col-md-3">
                        <input type="text" class="form-control" name="cardId" v-model="telephone"
                                placeholder="请输入手机号" @keyup.enter.prevent="searchCardInfo">
                    </div>
                    <div>
                        <input class="btn green" type="submit" @click="searchCardInfo" value="搜索"/>
                    </div>
                </div>
            </div>
            
            <table class="table table-bordered" v-if="lossPageShow">
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
			            	<button type="button" class="btn btn-info" v-if="cardInfo.cardState == 1" @click="repairCard">挂失</button>
			            	<button type="button" class="btn btn-success" v-if="cardInfo.cardState == 2" @click="repairCard">取消挂失</button>
			            </th>
			        </tr>
			    </tbody>
			</table>
			
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
                cardInfo:null,
            	telephone:null,               	
                lossPageShow:false
            },
            methods: {
                repairCard:function(){
                	var that = this;
                	var nowCardState;
                	if(that.cardInfo.cardState == 1){
	              		nowCardState = 2;
	              	}else if(that.cardInfo.cardState == 2){
	              		nowCardState = 1;
	              	}
                	$.ajax({
	                    type:"get",
	                    url:'cardCustomer/modifyCard',
	                    data:{
	                    	id:this.cardInfo.id,
	                    	cardId:this.cardInfo.cardId,
							cardState:nowCardState
	                    },
	                    contentType:"application/json",
	                    success:function(data){ //成功后返回
	                        if (data.success){	                        		                            
	                            if(that.cardInfo.cardState == 1){
	                            	that.cardInfo.cardState = 2;
				              		C.systemButtonNo('success','挂失成功');
				              	}else if(that.cardInfo.cardState == 2){
				              		that.cardInfo.cardState = 1;
				              		C.systemButtonNo('success','取消挂失成功');
				              	}
	                            console.log(JSON.stringify(that.cardInfo.cardState));
	                        }else {
	                            C.systemButtonNo('error',data.message);
	                        }
	                    },
	                    error: function(){ //失败后执行
	                        C.systemButtonNo('error','返回失败');
	                    }
	                });  
                },
                searchCardInfo:function(){
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
	                    type:"get",
	                    url:'cardCustomer/byTelephone',
	                    data:{
	                    	telephone:that.telephone
	                    },
	                    contentType:"application/json",
	                    success:function(data){ //成功后返回
	                        if (data.success){	                        	
	                            C.systemButtonNo('success','查询成功');
	                            that.cardInfo = data.data;
	                            that.lossPageShow = true;
	                            console.log(JSON.stringify(that.cardInfo));
	                        }else {
	                            C.systemButtonNo('error',data.message);
	                        }
	                    },
	                    error: function(){ //失败后执行
	                        C.systemButtonNo('error','查询失败');
	                    }
	                });                	
                }
                
            },
            ready:function () {
                var that=this;
            },
            created: function () {
            	
            }
        })
    C.vue = vueObj;
}());

</script>
