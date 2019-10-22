<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="controlShop">
    <c:choose>
        <c:when test="${cardDto.cardType == 0}">
            <a class="btn btn-info ajaxify" href="cardCustomer/normalView">
                <span class="glyphicon glyphicon-circle-arrow-left"></span>
                返回
            </a>
        </c:when>
        <c:when test="${cardDto.cardType == 1}">
            <a class="btn btn-info ajaxify" href="cardCustomer/employeeView">
                <span class="glyphicon glyphicon-circle-arrow-left"></span>
                返回
            </a>
        </c:when>
        <c:when test="${cardDto.cardType == 2}">
            <a class="btn btn-info ajaxify" href="cardCustomer/temporaryView">
                <span class="glyphicon glyphicon-circle-arrow-left"></span>
                返回
            </a>
        </c:when>
    </c:choose>
    <h2 class="text-center">
        <strong>充值详情</strong>
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
                        <c:when test="${cardDto.cardType == 2}">
                            <td>临时卡</td>
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
            </table>
        </div>
    </div>
    <br /> <br />
    <div class="panel panel-info">
        <div class="panel-heading text-center" style="font-size: 22px;">
            <strong>充值详情</strong>
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
            this.shopAppraiseTable.rows.add(${tbAccountLogList}).draw();
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
                            title:'交易流水号',
                            data:'tradeNo'
                        },
                        {
                            title : "充值日期",
                            data : "createTime",
                        },
                        {
                            title : "充值类型",
                            data : "type",
                            createdCell:function(td,tdData){
                                var str = "未知"
                                if(tdData == "0"){
                                    str = "普通充值"
                                }else if(tdData == "1"){
                                    str = "活动充值"
                                }
                                $(td).html(str);
                            }
                        },
                        {
                            title : "充值金额(元)",
                            data : "chargeMoney",
                        },
                        {
                            title : "赠送金额(元)",
                            data : "rewardMoney",
                        },
                        {
                            title : "卡内余额(元)",
                            data : "remain",
                        },
                        {
                            title : "操作人",
                            data : "loginTelephone",
                        }
                    ]
                });
            }
        }
    });
</script>

