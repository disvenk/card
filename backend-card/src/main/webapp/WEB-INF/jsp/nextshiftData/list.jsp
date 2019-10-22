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

            .modal-body ul li {
                width: 80%;
                margin: 4px auto;
                
            }

            .modal-body ul li .title {
                display: inline-block;
                width: 30%;
                text-align: right;
            }
            .modal-body ul li .value {
                width: 60%;
                text-align: left;
                padding-left: 20px;
                display: inline-block;
            }
            .modal-body ul li .value input {
                padding-left: 2px;
            }
        </style>

        <div id="control">
            <h2 class="text-center">
                <strong>营业数据报表</strong>
            </h2>
            <br/>
            <div class="row" id="searchTools">
                <div class="col-md-12">
                    <form class="form-inline">
                        <div class="form-group" style="margin-right: 50px;">
                            <label for="beginDate">选择日期：</label>
                            <input type="text" class="form-control form_datetime" id="beginDate" v-model="searchDate.date" readonly="readonly">
                        </div>
                        <!-- <div class="form-group" style="margin-right: 50px;">
                    <label for="endDate">结束时间：</label>
                    <input type="text" class="form-control form_datetime" id="endDate" v-model="searchDate.endDate"   readonly="readonly">
                </div> -->
                        <!-- <div class="form-group" style="margin-right: 50px;">
                    <label for="endDate">手机号：</label>
                    <input type="text" class="form-control" id="telephone" v-model="searchDate.telephone"  placeholder="请输入手机号码">
                </div> -->
                        <button type="button" class="btn btn-primary" @click="searchInfo">查询</button>&nbsp;
                        <button type="button" class="btn btn-primary" @click="logOut()" v-if="isShowLogout">交班</button>&nbsp;
                    </form>

                </div>
            </div>
            <br/>
            <br/>

            <div role="tabpanel" class="tab-pane">
                <div class="panel panel-primary" style="border-color:write;">
                    <div class="panel panel-info">
                        <div class="panel-heading text-center">
                            <strong style="font-size:22px;">收银台营业情况</strong>
                        </div>
                        <div class="panel-body">
                            <table id="cardSummaryTable" class="table table-striped table-bordered table-hover dataTable no-footer" width="100%" role="grid"
                                aria-describedby="cardSummaryTable_info" style="width: 100%;">
                                <thead>
                                    <tr role="row">
                                        <th class="sorting_disabled" rowspan="1" colspan="1">日期</th>
                                        <th class="sorting_disabled" rowspan="1" colspan="1">总收(元)</th>
                                        <th class="sorting_disabled" rowspan="1" colspan="1">应收现金(元)</th>
                                        <th class="sorting_disabled" rowspan="1" colspan="1">支付宝(元)</th>
                                        <th class="sorting_disabled" rowspan="1" colspan="1">支票(元)</th>
                                        <th class="sorting_disabled" rowspan="1" colspan="1">微信(元)</th>
                                        <th class="sorting_disabled" rowspan="1" colspan="1">天子星支付金额(元)</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr role="row" class="odd">
                                        <td>{{cardSummaryTable.created}}</td>
                                        <td>{{cardSummaryTable.chareCount}}</td>
                                        <td>{{cardSummaryTable.cashChargeCount}}</td>
                                        <td>{{cardSummaryTable.alipayCardChargeCount}}</td>
                                        <td>{{cardSummaryTable.chequeCardChargeCount}}</td>
                                        <td>{{cardSummaryTable.wechatCardChargeCount}}</td>
                                        <td>{{cardSummaryTable.starCardChargeCount}}</td>
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
                            <strong style="font-size:22px">值班营业数据</strong>
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
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">×</span>
                            </button>
                            <h4 class="modal-title" id="exampleModalLabel" style="text-align:center">交接班</h4>
                        </div>
                        <div class="modal-body" align="center">
                            <ul>
                                <li><span class="title">手机号：</span><span class="value">{{formData.tel}}</span></li>
                                <li><span class="title">当班时间：</span><span class="value">{{formData.onCallDate}}</span></li>
                                <li><span class="title">应收现金：</span><span class="value">{{formData.cashChargeCount}}</span></li>
                                <li><span class="title">微信：</span><span class="value">{{formData.wechatCardChargeCount}}</span></li>
                                <li><span class="title">支付宝：</span><span class="value">{{formData.alipayCardChargeCount}}</span></li>
                                <li><span class="title">支票：</span><span class="value">{{formData.chequeCardChargeCount}}</span></li>
                                <li><span class="title">实收现金：</span><span class="value"><input type="text" v-model="formData.actualMoney"/></span></li>
                            </ul>
                        </div>
                        <div class="modal-footer text-center" align="center" style="text-align:center;">
                            <a class="btn default" data-dismiss="modal">取消</a>
                            <input class="btn btn-primary" style="margin-left: 40px;" type="submit" value="确定交班" @click="confirmLogout" />
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </div>
        <script src="../assets/customer/date.js" type="text/javascript"></script>
        <script src="../assets/customer/dialogUtils.js" type="text/javascript"></script>

        <script>
            //时间插件
            $('.form_datetime').datetimepicker({
                endDate: new Date(),
                minView: "month",
                maxView: "month",
                autoclose: true, //选择后自动关闭时间选择器
                todayBtn: true, //在底部显示 当天日期
                todayHighlight: true, //高亮当前日期
                format: "yyyy-mm-dd",
                startView: "month",
                language: "zh-CN"
            });

            //创建vue对象
            var vueObj = new Vue({
                el: "#control",
                data: {
                    searchDate: {
                        date: "",
                        endDate: "",
                    },
                    cardSummaryTable: {
                        test: '测试文字'
                    }, //总报表
                    cardCustomerTable: {}, //各档口风报表
                    formData: {actualMoney: '0'}
                },
                computed: {
                    // 是否显示交班按钮
                    isShowLogout: function() {
                        return new Date().format("yyyy-MM-dd") === this.searchDate.date ? true : false;
                    }
                },
                created: function () {
                    var date = new Date().format("yyyy-MM-dd");
                    this.searchDate.date = date;
                    this.searchDate.endDate = date;
                    this.createAppriseTable();
                    // this.getCardSummaryTable(); // 获取总的报表数据
                },
                methods: {
                    createAppriseTable: function () {
                        var that = this;
                        that.cardCustomerTable = $("#cardCustomerTable").DataTable({
                            lengthMenu: [
                                [10, 25, 50, 100],
                                [10, 25, 50, 100]
                            ],
                            order: [
                                [1, "desc"]
                            ],
                            // serverSide: true,
                            bFilter: false, //过滤功能
                            bSort: false, //排序功能
                            ajax: {
                                "url": 'nextshiftData/selectBusinessCountByDate',
                                "type": 'post',
                                "data": function (d) {
                                    //添加额外的参数传给服务器
                                    d.date = that.searchDate.date;
                                    d.endDate = that.searchDate.endDate;
                                },
                                "dataSrc": function(response) {
                                    // 统计数据
                                    that.cardSummaryTable = response.data.json ? response.data.json : {}; 

                                    // 表格数据
                                    return response.data.jsonList;
                                }
                            },
                            columns: [{
                                    title: "日期",
                                    data: "date"
                                },
                                {
                                    title: "应收现金",
                                    data: "cashMoney"
                                },
                                {
                                    title: "实收现金",
                                    data: "actualCash"
                                },
                                {
                                    title: "支付宝",
                                    data: "aliMoney",
                                },
                                {
                                    title: "微信",
                                    data: "wechatMoney"
                                },
                                {
                                    title: "支票",
                                    data: "chequeMoney"
                                },
                                {
                                    title: "天子星支付(元)",
                                    data: "starMoney"
                                },
                                {
                                    title: "营业员",
                                    data: "tel"
                                },
                            ]
                        });
                    },
                    searchInfo: function () {
                        this.cardCustomerTable.ajax.reload();
                    },
                    // 显示交班模态框
                    logOut: function () {
                        var _this = this;
                        $.getJSON(`nextshiftData/selectToDayAmount`, function(response) {
                            _this.formData = response.data.json;
                            // console.log(data);
                            $("#exampleModal").modal();
                        })
                    },
                    confirmLogout: function() {
                        var _this = this;
                        if ( ! /^[0-9.]+$/.test(_this.formData.actualMoney)) {
                            toastr.clear();
                            toastr.error('请输入格式正确的实收金额');
                            return;
                        }
                        DialogUtil.confirmDialog('确定交班吗?', '确认', function() {

                            $.getJSON(`nextshiftData/confirmNextShift`, _this.formData, function(data) {
                                if (data.stateCode == 100) {
                                    $("#exampleModal").modal('hide');
                                    // 返回操作成功过， 再退出登录
                                    location.assign('/logout');
                                } else {
                                    toastr.clear();
                                    toastr.error(data.message);
                                }
                            })
                        }, function() {})
                    }
                    
                }
            })
        </script>