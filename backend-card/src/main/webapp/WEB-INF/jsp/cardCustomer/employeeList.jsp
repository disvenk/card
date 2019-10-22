<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
    ul li {
        list-style: none;
    }
    .list-style {
        font-size: 18px;
    }
    .list-style li {
        margin-left: 20%;
        margin-bottom: 10px;
    }
</style>

<div id="control">
    <h2 class="text-center"><strong>用户列表</strong></h2><br/>
    <div class="row" id="searchTools">
        <div class="col-md-12">
            <form class="form-inline">
                <div class="form-group" style="margin-right: 50px;">
                    <label for="beginDate">开始时间：</label>
                    <input type="text" class="form-control form_datetime" id="beginDate" v-model="searchDate.beginDate"   readonly="readonly">
                </div>
                <div class="form-group" style="margin-right: 50px;">
                    <label for="endDate">结束时间：</label>
                    <input type="text" class="form-control form_datetime" id="endDate" v-model="searchDate.endDate"   readonly="readonly">
                </div>
                <div class="form-group" style="margin-right: 50px;">
                    <label for="endDate">手机号：</label>
                    <input type="text" class="form-control" id="telephone" v-model="searchDate.telephone"  placeholder="请输入手机号码">
                </div>
                <button type="button" class="btn btn-primary" @click="searchInfo">查询</button>&nbsp;
            </form>

        </div>
    </div>
    <br/>
    <br/>

    <div role="tabpanel" class="tab-pane">
        <div class="panel panel-primary" style="border-color:write;">
            <div class="panel panel-info">
                <div class="panel-heading text-center">
                    <strong style="margin-right:100px;font-size:22px">用户列表</strong>
                </div>
                <div class="panel-body">
                    <table id="cardCustomerTable" class="table table-striped table-bordered table-hover" width="100%">
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false" aria-labelledby="exampleModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content" style="margin-top: 170px;">
                <%--<div class="modal-header" style="background-color:#E0EBF9;">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">×</span></button>
                    <h4 class="modal-title" id="exampleModalLabel" style="text-align:center">变更卡类型和折扣项</h4>
                </div>--%>
                <div class="modal-body">
                    <ul class="list-style">
                        <li>卡号：{{platformOrderk.cardId}}</li>
                        <li>姓名：{{platformOrderk.customerName}}</li>
                        <li>手机号：{{platformOrderk.telephone}}</li>
                        <li>当前类型:
                            <span v-if="platformOrderk.type == 0">普通卡</span>
                            <span v-if="platformOrderk.type == 1">员工卡</span>
                            <button type="button" class="btn btn-info" style="margin-left: 10px;" @click="changeType">切换类型</button>
                        </li>
                        <%--<li>开卡类型：--%>
                        <%--<label>--%>
                        <%--<input type="radio" name="type" v-model="platformOrderk.type" value="0">普通卡--%>
                        <%--</label>--%>
                        <%--<label>--%>
                        <%--<input type="radio" name="type" v-model="platformOrderk.type" value="1">员工卡--%>
                        <%--</label>--%>
                        <%--</li>--%>
                        <li v-show="platformOrderk.type == 1">身份证号：<input type="text" name="idCard" value="{{platformOrderk.idCard}}" v-model="platformOrderk.idCard"></li>
                        <li v-show="platformOrderk.type == 1">公司：
                            <select name="companyId" v-model="platformOrderk.companyId">
                                <option readonly selected value>请选择</option>
                                <option  v-for="company in companys" value="{{company.id}}">
                                    {{company.companyName}}
                                </option>
                            </select>
                        </li>
                        <li>折扣：
                            <select name="discountId" v-model="platformOrderk.discountId">
                                <option readonly selected value>请选择</option>
                                <option  v-for="discount in discounts" value="{{discount.id}}">
                                    {{discount.discountName}}
                                </option>
                            </select>
                        </li>
                        <li><input type="hidden" name="id" value="{{platformOrderk.id}}"/></li>
                    </ul>
                    <div class="form-group">
                    </div>
                </div>
                <div class="modal-footer">
                    <input class="btn green" type="submit" value="保存" @click="add"/>
                    <a class="btn default" data-dismiss="modal" style="margin-right:235px;" @click="canelEdit">取消</a>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<script src="assets/customer/date.js" type="text/javascript"></script>

<script>

    //时间插件
    $('.form_datetime').datetimepicker({
        endDate:new Date(),
        minView:"month",
        maxView:"month",
        autoclose:true,//选择后自动关闭时间选择器
        todayBtn:true,//在底部显示 当天日期
        todayHighlight:true,//高亮当前日期
        format:"yyyy-mm-dd",
        startView:"month",
        language:"zh-CN"
    });

    //创建vue对象
    var vueObj =  new Vue({
        el:"#control",
        data:{
            searchDate : {
                beginDate : "",
                endDate : "",
            },
            cardCustomerTable : {},
            platformOrderk:{},
            companys:[],
            discounts:[],
            nowType:null
        },
        created : function() {
            var date = new Date().format("yyyy-MM-dd");
            this.searchDate.beginDate = date;
            this.searchDate.endDate = date;
            this.createAppriseTable();
            this.readyCard();
            //this.searchInfo();
        },
        methods:{
            createAppriseTable : function () {
                var that = this;
                that.cardCustomerTable=$("#cardCustomerTable").DataTable({
                    lengthMenu: [ [10,25, 50, 100], [10,25, 50, 100] ],
                    order: [[ 1, "desc" ]],
                    serverSide: true,
                    bFilter: false, //过滤功能
                    bSort: false, //排序功能
                    ajax: {
                        "url": 'cardCustomer/datas',
                        "type": 'post',
                        "data":function ( d ) {
                            //添加额外的参数传给服务器
                            d.beginDate=that.searchDate.beginDate;
                            d.endDate=that.searchDate.endDate;
                            d.telephone=that.searchDate.telephone;
                            d.type=1;
                        }
                    },
                    columns : [
                        {
                            title: "序号",
                            data: null,
                            render: function (data, type, full, meta) {
                                return meta.row + 1 + meta.settings._iDisplayStart;
                            }
                        },
                        {
                            title : "卡号",
                            data : "cardId"
                        },
                        {
                            title : "姓名",
                            data : "customerName"
                        },
                        {
                            title : "手机号码",
                            data : "telephone"
                        },
                        {
                            title : "身份证号",
                            data : "idCard",
                            render: function (data) {
                                return data == '' || data == null ?  "---" : data.substring(0,8) + '******' + data.substring(14,18);
                            }
                        },
                        {
                            title : "单位名称",
                            data : "companyName"
                        },
                        {
                            title : "账户余额",
                            data : "remain"
                        },
                        {
                            title : "卡类型",
                            data : "type",
                            createdCell:function(td,tdData){
                                var state = "未知";
                                if (tdData == 0){
                                    state = "普通卡";
                                }else if(tdData == 1){
                                    state = "员工卡";
                                }
                                $(td).html(state);
                            }
                        },
                        {
                            title : "开卡时间",
                            data : "createdAt"
                        },
                        {
                            title : "最后一次消费",
                            data : "updatedAt"
                        },
                        {
                            title : "操作",
                            data: "id",
                            orderable : false,
                            createdCell: function (td, tdData, rowData) {
                                var button = $("<button type='button' class='btn blue'>编辑 </button> ");
                                button.click(function(){
                                    that.cardCustomerModal(rowData);
                                });
                                $(td).html(button);
                            }
                        },
                        {
                            title: "消费",
                            data: "cardId",
                            orderable : false,
                            createdCell: function (td, tdData, rowData) {
                                var button = $("<a href='cardCustomer/accountLogp?cardId="+tdData+"' class='btn green ajaxify '>消费详情</a>");
                                $(td).html(button);
                            }
                        },
                        {
                            title: "充值",
                            data: "cardId",
                            orderable : false,
                            createdCell: function (td, tdData, rowData) {
                                var button = $("<a href='cardCustomer/cardRechargeLog?cardId="+tdData+"&accountId="+rowData.accountId+"' class='btn green ajaxify '>充值详情</a>");
                                $(td).html(button);
                            }
                        }
                    ]
                });
            },
            searchInfo : function() {
                this.cardCustomerTable.ajax.reload();
                /*var that = this;
                 var timeCha = new Date(that.searchDate.endDate).getTime() - new Date(that.searchDate.beginDate).getTime();
                 if(timeCha < 0){
                 toastr.clear();
                 toastr.error("开始时间应该少于结束时间！");
                 return false;
                 }/!*else if(timeCha > 604800000){
                 toastr.clear();
                 toastr.error("暂时未开放大于一周以内的查询！");
                 return false;
                 }*!/
                 toastr.clear();
                 toastr.success("查询中...");
                 try {
                 $.post("cardCustomer/datas", this.getDate(null), function (result) {
                 if (result.success) {
                 that.cardCustomerTable.clear();
                 that.cardCustomerTable.rows.add(result.data.tbCardCustomerList).draw();
                 toastr.clear();
                 toastr.success("查询成功");
                 } else {
                 toastr.clear();
                 toastr.error("查询出错");
                 }
                 });
                 }catch (e){
                 toastr.clear();
                 toastr.error("系统异常，请刷新重试");
                 }*/
            },
            cardCustomerModal : function (data) {
                this.platformOrderk = data;
                this.nowType = data.type;
                console.log(JSON.stringify(this.platformOrderk.type));
                $("#exampleModal").modal();
            },
            getDate : function(shopId){
                var data = {
                    beginDate : this.searchDate.beginDate,
                    endDate : this.searchDate.endDate,
                };
                return data;
            },
            today : function(){
                date = new Date().format("yyyy-MM-dd");
                this.searchDate.beginDate = date
                this.searchDate.endDate = date
                this.searchInfo();
            },
            yesterDay : function(){
                this.searchDate.beginDate = GetDateStr(-1);
                this.searchDate.endDate  = GetDateStr(-1);
                this.searchInfo();
            },
            week : function(){
                this.searchDate.beginDate  = getWeekStartDate();
                this.searchDate.endDate  = new Date().format("yyyy-MM-dd")
                this.searchInfo();
            },
            month : function(){
                this.searchDate.beginDate  = getMonthStartDate();
                this.searchDate.endDate  = new Date().format("yyyy-MM-dd")
                this.searchInfo();
            },
            readyCard:function () {
                var that=this;
                $.get("cardCompany/list", function (data) {
                    that.companys = data.data;
                });
                $.get("cardDiscount/list", function (data) {
                    that.discounts = data.data;
                });
            },
            add:function () {
                $.ajax({
                    data:{
                        id:this.platformOrderk.id,
                        type:this.platformOrderk.type,
                        idCard:this.platformOrderk.idCard,
                        discountId:this.platformOrderk.discountId,
                        companyId:this.platformOrderk.companyId
                    },
                    url : "cardCustomer/modifyDiscount",
                    type:"post",
                    success : function(result) {
                        if (result.success){
                            toastr.success("修改成功");
                            $('#exampleModal').modal('hide');
                            $('#cardCustomerTable').DataTable().ajax.reload();
                        }else {
                            toastr.error(result.message);
                        }
                    }
                });
            },
            changeType:function () {
                if(this.platformOrderk.type == 0){
                    this.platformOrderk.type = 1;
                }else{
                    this.platformOrderk.type = 0;
                }
            },
            canelEdit:function () {
                this.platformOrderk.type = this.nowType;
            }
        }
    })
</script>

