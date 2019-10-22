<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="controlShop">
    <a class="btn btn-info ajaxify" href="cardCustomer/accountLogView">
        <span class="glyphicon glyphicon-circle-arrow-left"></span>
        返回
    </a>
    <h2 class="text-center">
        <strong>消费详情</strong>
    </h2>
    <br />
    <div class="row">
        <div class="col-md-12">
            <table>
                <tr>
                    <th><lable>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;卡号：</lable></th>
                    <td>${cardDto.cardId}</td>
                <tr>
                <tr>
                    <th><lable>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;姓名：</lable></th>
                    <td>${cardDto.customerName}</td>
                <tr>
                <tr>
                    <th><lable>&nbsp;&nbsp;&nbsp;&nbsp;手机号：</lable></th>
                    <td>${cardDto.telephone}</td>
                <tr>
                <tr>
                    <th><lable>开卡类型：</lable></th>
                    <c:choose>
                        <c:when test="${cardDto.cardType == 0}">
                            <td>普通卡</td>
                        </c:when>
                        <c:when test="${cardDto.cardType == 1}">
                            <td>员工卡</td>
                        </c:when>
                        <c:otherwise>
                            <td></td>
                        </c:otherwise>
                    </c:choose>
                <tr>
                <tr>
                    <th><lable>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;折扣：</lable></th>
                    <td>${cardDto.discountName}</td>
                <tr>
                <tr>
                    <th><lable>账户余额：</lable></th>
                    <td>${cardDto.remain}</td>
                <tr>
            </table>
        </div>
    </div>
    <br /> <br />
    <div class="panel panel-info">
        <div class="panel-heading text-center" style="font-size: 22px;">
            <strong>消费详情</strong>
        </div>
        <div class="panel-body">
            <table class="table table-striped table-bordered table-hover" id="shopAppraise">
            </table>
        </div>
    </div>
</div>
<script src="assets/customer/date.js" type="text/javascript"></script>
<script>
    var shopId = "${shopId}";
    var vueObjShop = new Vue({
        el : "#controlShop",
        data : {
            shopAppraiseTable : {},
        },
        created : function() {
            this.initDataTables();
            this.shopAppraiseTable.clear();
            this.shopAppraiseTable.rows.add(${purchaseHistoryDto}).draw();
        },
        methods : {
            initDataTables:function () {
                //that代表 vue对象
                var that = this;
                that.shopAppraiseTable = $("#shopAppraise").DataTable({
                    lengthMenu: [ [10,25, 50, 100], [10,25, 50, 100] ],
                    order: [[ 0, "desc" ]],
                   //serverSide: true,
                    bFilter: false, //过滤功能
                    bSort: false, //排序功能
                    /*ajax: {
                        "url": 'cardCustomer/cardRechargeLogDatas',
                        "type": 'post',
                    },*/
                    columns : [
                        {
                            title:'日期',
                            data:'createTime'
                        },
                        {
                            title : "订单流水",
                            data : "serialNumber",
                        },
                        {
                            title : "订单金额(元)",
                            data : "originalAmount",
                        },
                        {
                            title : "储值卡折扣金额(元)",
                            data : "discountPayValue",
                        },
                        {
                            title : "充值账户支付(元)",
                            data : "accountPayValue",
                        }
                        ,
                        {
                            title : "充值赠送支付(元)",
                            data : "rechargeGiftPayValue",
                        },
                        {
                            title : "微信支付(元)",
                            data : "wechatPayValue",
                        },
                        {
                            title : "支付宝支付(元)",
                            data : "aliPayValue",
                        },
                        {
                            title : "优惠券支付(元)",
                            data : "couponPayValue",
                        }
                        ,
                        {
                            title : "红包支付(元)",
                            data : "redEnvelopePayValue",
                        },
                        {
                            title : "等位红包支付(元)",
                            data : "waitRedEnvelopePayValue",
                        },
                        {
                            title : "刷卡支付(元)",
                            data : "swingCardPayValue",
                        },
                        {
                            title : "现金实收(元)",
                            data : "cashPayValue",
                        }
                        ,
                        {
                            title : "闪惠支付(元)",
                            data : "shanHuiPayValue",
                        },
                        {
                            title : "会员支付(元)",
                            data : "memberPayValue",
                        },
                        {
                            title : "退菜返还红包(元)",
                            data : "returnOrderPayValue",
                        },
                        {
                            title : "其他支付方式(元)",
                            data : "otherPayValue",
                        }
                    ]
                });
            }
        }
    });
</script>

