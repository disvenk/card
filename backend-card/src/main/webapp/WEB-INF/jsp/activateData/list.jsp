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
    <h2 class="text-center"><strong>开卡数据报表</strong></h2><br/>
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
                    <strong style="font-size:22px">实体卡开卡数据报表</strong>
                    <button type="button" class="btn btn-primary pull-right" @click="downLoad(1)">月报表下载</button>
                </div>
                <div class="panel-body">
                    <table id="cardEntityTable" class="table table-striped table-bordered table-hover dataTable no-footer" width="100%" role="grid"
                        aria-describedby="cardEntityTable_info" style="width: 100%;">
                        <thead>
                            <tr role="row">
                                <th class="sorting_disabled" rowspan="1" colspan="1" >开卡总数量(张)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >员工卡开卡数量(张)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >普通卡开卡数量(张)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >临时卡开卡数量(张)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >支票开卡金额(元)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >现金开卡金额(元)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >微信开卡金额(元)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >支付宝金额开卡(元)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >天子星开卡金额(元)</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr role="row" class="odd">
                                <td>{{cardEntityTable.cardCount}}</td>
                                <td>{{cardEntityTable.workerCardCount}}</td>
                                <td>{{cardEntityTable.normalCardCount}}</td>
                                <td>{{cardEntityTable.tempCardCount}}</td>
                                <td>{{cardEntityTable.chequeCardMoney}}</td>
                                <td>{{cardEntityTable.cashCardMoney}}</td>
                                <td>{{cardEntityTable.wechatCardMoney}}</td>
                                <td>{{cardEntityTable.aliPayCardMoney}}</td>
                                <td>{{cardEntityTable.starCardMoney}}</td>
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
                    <strong style="font-size:22px;">普通卡开卡数据报表</strong>
                    <button type="button" class="btn btn-primary pull-right" @click="downLoad(3)">月报表下载</button>
                </div>
                <div class="panel-body">
                    <table id="cardCommonTable" class="table table-striped table-bordered table-hover dataTable no-footer" width="100%" role="grid"
                        aria-describedby="cardCommonTable_info" style="width: 100%;">
                        <thead>
                            <tr role="row">
                                <th class="sorting_disabled" rowspan="1" colspan="1" >普通卡开卡数量(张)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >现金开卡金额(元)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >微信开卡金额(张)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >支付宝金额开卡(元)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >支票开卡总额(元)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >天子星开卡金额(元)</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr role="row" class="odd">
                                <td>{{cardCommonTable.cardCount}}</td>
                                <td>{{cardCommonTable.cashCardMoney}}</td>
                                <td>{{cardCommonTable.wechatCardMoney}}</td>
                                <td>{{cardCommonTable.aliPayCardMoney}}</td>
                                <td>{{cardCommonTable.chequeCardMoney}}</td>
                                <td>{{cardCommonTable.starCardMoney}}</td>
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
                    <strong style="font-size:22px;margin-right:90px;">临时卡开卡数据报表</strong>
                    <!-- <button type="button" class="btn btn-primary pull-right" @click="downLoad(4)">月报表下载</button> -->
                </div>
                <div class="panel-body">
                    <table id="cardTempTable" class="table table-striped table-bordered table-hover dataTable no-footer" width="100%" role="grid"
                        aria-describedby="cardTempTable_info" style="width: 100%;">
                        <thead>
                            <tr role="row">
                                <th class="sorting_disabled" rowspan="1" colspan="1" >临时卡开卡数量(张)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >现金开卡金额(元)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >微信开卡金额(张)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >支付宝金额开卡(元)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >支票开卡总额(元)</th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" >天子星开卡金额(元)</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr role="row" class="odd">
                                <td>{{cardTempTable.cardCount}}</td>
                                <td>{{cardTempTable.cashCardMoney}}</td>
                                <td>{{cardTempTable.wechatCardMoney}}</td>
                                <td>{{cardTempTable.aliPayCardMoney}}</td>
                                <td>{{cardTempTable.chequeCardMoney}}</td>
                                <td>{{cardTempTable.starCardMoney}}</td>
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
                    <strong style="font-size:22px">员工卡开卡数据报表</strong>
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
                    <h4 class="modal-title" id="exampleModalLabel" style="text-align:center" >下载月报表</h4>
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
    var vueObj =  new Vue({
        el:"#control",
        data:{
            searchDate : {
                beginDate : "",
                endDate : "",
            },
            cardEntityTable: {test: '实体卡测试'},   
            cardCommonTable: {test: '普通卡测试'},
            cardTempTable: {test: '临时卡测试'},   
            cardSummaryTable: {test: '测试文字'},   //总报表
            cardCustomerTable : {}, //各档口风报表
            excel: {year: new Date().format("yyyy"), month: '01'}, // 月报表 年 月
            yearList: [],
            downLoadType: 0,
        },
        created : function() {
            var date = new Date().format("yyyy-MM-dd");
            this.searchDate.beginDate = date;
            this.searchDate.endDate = date;
            this.createAppriseTable();
            this.generateYears();
            // this.getCardEntityTable();// 实体卡数据
            // this.getCardCommonTable();// 普通卡数据
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
                        "url": 'activateData/selectBrandByDateFromStartAndEnd',
                        "type": 'post',
                        "data":function ( d ) {
                            //添加额外的参数传给服务器
                            d.beginDate=that.searchDate.beginDate;
                            d.endDate=that.searchDate.endDate;
                            // d.telephone=that.searchDate.telephone;
                        },
                        "dataSrc": function(response) {
                            // 统计数据
                            that.cardEntityTable = response.data.json1 ? response.data.json1 : {}; 
                            that.cardCommonTable = response.data.json2 ? response.data.json2 : {};
                            that.cardTempTable = response.data.json3 ? response.data.json3 : {};

                            // 表格数据
                            return response.data.jsonList;
                        }
                    },
                    columns : [
                        {
                            title : "公司名称",
                            data : "companyName"
                        },
                        {
                            title : "开卡数量",
                            data : "cardCount"
                        },
                        {
                            title : "支票开卡金额",
                            data : "chequeCardMoney",
                        },
                        {
                            title : "现金开卡金额",
                            data : "cashCardMoney"
                        },
                        {
                            title : "微信开卡金额",
                            data : "wechatCardMoney"
                        },
                        {
                            title : "天子星开卡金额(元)",
                            data : "starCardMoney"
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
                    setTimeout(function() {
                        window.open('activateData/exprotEntityExcel?beginDate='+that.searchDate.beginDate+'&endDate='+that.searchDate.endDate);
                    }, 0);
                    setTimeout(function() {
                        window.open('activateData/exprotNormalExcel?beginDate='+that.searchDate.beginDate+'&endDate='+that.searchDate.endDate);
                    }, 10);
                    setTimeout(function() {
                        window.open('activateData/exprotWorkerExcel?beginDate='+that.searchDate.beginDate+'&endDate='+that.searchDate.endDate);
                    }, 20);
                } else {
                    this.cardSelectMonthModal()
                }
            },
            downloadFile: function() {
                // console.log(this.excel)
                var that = this;
                if (this.downLoadType === 1) {
                    setTimeout(function() {
                        window.open('activateData/exprotMothEntityExcel?mothDate='+that.excel.year+'-'+that.excel.month);
                    }, 0);
                    setTimeout(function() {
                        window.open('activateData/exprotMothNormalExcel?mothDate='+that.excel.year+'-'+that.excel.month);
                    }, 10);
                    setTimeout(function() {
                        window.open('activateData/exprotMothWorkerExcel?mothDate='+that.excel.year+'-'+that.excel.month);
                    }, 20);
                } else if (this.downLoadType === 2) {
                    return window.location.href = 'activateData/selectOneCompanyAll?mothDate='+that.excel.year+'-'+that.excel.month;  
                } else if (this.downLoadType === 3) {
                    return window.location.href = 'activateData/exprotMothNormalAllExcel?mothDate='+that.excel.year+'-'+that.excel.month;  
                } else if (this.downLoadType === 4) {// 临时卡报表
                    return window.location.href = 'activateData/exprotMothNormalAllExcel?mothDate='+that.excel.year+'-'+that.excel.month;                      
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

