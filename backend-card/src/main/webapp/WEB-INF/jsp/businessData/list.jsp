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
    <h2 class="text-center"><strong>营业数据报表</strong></h2><br/>
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
                <!-- <div class="form-group" style="margin-right: 50px;">
                    <label for="endDate">手机号：</label>
                    <input type="text" class="form-control" id="telephone" v-model="searchDate.telephone"  placeholder="请输入手机号码">
                </div> -->
                <button type="button" class="btn btn-primary" @click="searchInfo">查询报表</button>&nbsp;
                <button type="button" class="btn btn-primary" @click="downLoad(0)">下载报表</button>&nbsp;
            </form>

        </div>
    </div>
    <br/>
    <br/>

    <div role="tabpanel" class="tab-pane">
        <div class="panel panel-primary" style="border-color:write;">
            <div class="panel panel-info">
                <div class="panel-heading text-center">
                    <strong style="font-size:22px;">营业报表</strong>
                    <button type="button" class="btn btn-primary pull-right" @click="downLoad(1)">月报表下载</button>
                </div>
                <div class="panel-body">
                    <table id="cardSummaryTable" class="table table-striped table-bordered table-hover dataTable no-footer" width="100%" role="grid"
                        aria-describedby="cardSummaryTable_info" style="width: 100%;">
                        <thead>
                            <tr role="row">
                                <th class="sorting_disabled" rowspan="1" colspan="1" >品牌名称</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >订单数</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >订单总额</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >实收金额</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >折扣金额</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >退款金额</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr role="row" class="odd">
                                <td>{{cardSummaryTable.brandName}}</td>
                                <td>{{cardSummaryTable.orderCount}}</td>
                                <td>{{cardSummaryTable.originalAmount}}</td>
                                <td>{{cardSummaryTable.paymentAmount}}</td>
                                <td>{{cardSummaryTable.reductionAmount}}</td>
                                <td>{{cardSummaryTable.refundMoney}}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div role="tabpanel" class="tab-pane">
        <div class="panel panel-primary" style="border-color:write;">
            <div class="panel panel-info">
                <div class="panel-heading text-center">
                    <strong style="font-size:22px">各档口营业报表</strong>
                    <button type="button" class="btn btn-primary pull-right" @click="downLoad(2)">月报表下载</button>
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
                <div class="modal-header" >
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">×</span></button>
                    <h4 class="modal-title" id="exampleModalLabel" style="text-align:center">下载月报表</h4>
                </div>
                <div class="modal-body" align="center">
                        <select style="padding: 5px 12px;" v-model="excel.year">
                            <option v-for="year in yearList" :value="year">{{year}}</option>
                        </select>
                        <span style="font-size: 16px;margin-left: 15px;font-weight: bold;">年</span>
                        <select style="padding: 5px 12px;" v-model="excel.month">
                            <option value="01">01</option>
                            <option value="02">02</option>
                            <option value="03">03</option>
                            <option value="04">04</option>
                            <option value="05">05</option>
                            <option value="06">06</option>
                            <option value="07">07</option>
                            <option value="08">08</option>
                            <option value="09">09</option>
                            <option value="10">10</option>
                            <option value="11">11</option>
                            <option value="12">12</option>
                        </select>
                        <span style="font-size: 16px;margin-left: 15px;font-weight: bold;">月</span>
                    </div>
                <div class="modal-footer text-center"  style="text-align:center;">
                    <a class="btn default" data-dismiss="modal"  >取消</a>
                    <input class="btn green" style="margin-left: 40px;" type="submit" value="生成并下载" @click="downloadFile"/>
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
    Vue.config.debug = true;
    var vueObj =  new Vue({
        el:"#control",
        data:{
            searchDate : {
                beginDate : "",
                endDate : "",
            },
            cardSummaryTable: {test: '测试文字'},   //总报表
            cardCustomerTable : {}, //各档口风报表
            excel: {year: 2018, month: '01'}, // 月报表 年 月
            yearList: [],
            downLoadType: 0,// 0-2
        },
        created : function() {
            var date = new Date().format("yyyy-MM-dd");
            this.searchDate.beginDate = date;
            this.searchDate.endDate = date;
            this.createAppriseTable();
            this.generateYears();
            // this.getCardSummaryTable(); // 获取总的报表数据
            // this.cardSummaryTable = {brandName: 'askfdj;l'}
        },
        methods:{
            createAppriseTable : function () {
                var that = this;
                that.cardCustomerTable=$("#cardCustomerTable").DataTable({
                    lengthMenu: [ [10,25, 50, 100], [10,25, 50, 100] ],
                    order: [[ 1, "desc" ]],
                    // serverSide: true,
                    bFilter: false, //过滤功能
                    bSort: false, //排序功能
                    ajax: {
                        "url": 'businessData/selectBrandByDateFromStartAndEnd',
                        "type": 'post',
                        "data":function ( d ) {
                            //添加额外的参数传给服务器
                            d.beginDate=that.searchDate.beginDate;
                            d.endDate=that.searchDate.endDate;
                        },
                        "dataSrc": function(response) {
                            // 统计数据
                            that.cardSummaryTable = response.data.brand ? response.data.brand : {};
                            // 表格数据
                            return response.data.shop;
                        }
                    },
                    columns : [
                        {
                            title : "档口名称",
                            data : "shopName",
                            defaultContent: '--'
                        },
                        {
                            title : "订单数",
                            data : "orderCount"
                        },
                        {
                            title : "订单总额",
                            data : "originalAmount",
                        },
                        {
                            title : "实收金额",
                            data : "paymentAmount"
                        },
                        {
                            title : "折扣金额",
                            data : "reductionAmount"
                        },
                        {
                            title: "退款金额",
                            data: "refundMoney"
                        }
                    ]
                });
            },
            searchInfo : function() {
               this.cardCustomerTable.ajax.reload();
            },
            // 下载月报表，选择月份
            cardSelectMonthModal : function (data) { 
                $("#exampleModal").modal();
            },
            // 下载报表
            downLoad: function(type) {
                var that = this;
                that.downLoadType = type;
                // console.log(that.searchDate)
                // 1:总；2：各档口
                if (type === 0) {
                    return window.location.href = 'businessData/exprotBrandExcel?beginDate='+that.searchDate.beginDate+'&endDate='+that.searchDate.endDate;
                } else if (type === 1) {
                    this.cardSelectMonthModal()
                } else if (type === 2) {
                    this.cardSelectMonthModal()
                }
            },
            downloadFile: function() {
                // console.log(this.excel)
                var that = this;
                if (this.downLoadType === 1) {
                    return window.location.href = 'businessData/exprotBrandMothExcel?mothDate='+that.excel.year+'-'+that.excel.month;
                } else if (this.downLoadType === 2) {
                    return window.location.href = 'businessData/exprotShopMothExcel?mothDate='+that.excel.year+'-'+that.excel.month;  
                }
            },
            // 生成可以选择的年份
            generateYears: function() {
                var startYear = 2016;
                var endYear = new Date().format("yyyy");
                for (var i = startYear; i<=endYear; ++i) {
                    this.yearList.push(i);
                }
            }
        }
    })
</script>

