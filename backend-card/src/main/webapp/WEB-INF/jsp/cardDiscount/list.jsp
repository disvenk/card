<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="control">
    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6" >
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">新建折扣项</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form class="form-horizontal" role="form" action="{{m.id?'cardDiscount/modify':'cardDiscount/create'}}" @submit.prevent="save">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">名&nbsp;称：</label>
                                <div class="col-sm-8" style="margin-left: -16px;">
                                    <input type="text" class="form-control" name="discountName" required v-model="m.discountName">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">是否启用:</label>
                                <div class="col-sm-8">
                                    <label><input type="radio"  style="cursor:pointer"  name="isOpen" required v-model="m.isOpen" value="1">启用</label>
                                    <label><input type="radio"  style="cursor:pointer"  name="isOpen" required v-model="m.isOpen" value="0">不启用</label>
                                </div>
                                <button type="button" class="btn btn-primary" @click="addUnit">添加时间段</button>
                            </div>
                            <div class="form-group" v-for="(index,unit) in tbCardDiscountDetailList">
                                <input type="hidden" name="weekIndex" value="{{index+1}}"/>
                                <%--折扣日期--%>
                                <div>
                                    <label class="col-sm-2 control-label">折扣日期：</label>
                                    <div>
                                        <label class="checkbox-inline" v-for="(o,f) in weekList">
                                            <input type="checkbox" id="inline{{index+1}}Checkbox{{o+1}}" :value="f.id" name="discountWeeks{{index+1}}">
                                            <span>{{f.name}}</span>
                                        </label>
                                    </div>
                                </div>
                                <%--折扣时间--%>
                                <div style="padding: 10px 0;margin-top: 20px;">
                                    <label class="col-sm-2 control-label">折扣时间：</label>
                                    <div class="input-group">
                                        <div class="input-group">
                                            <input type="text" class="form-control timepicker timepicker-no-seconds" name="startDiscountTimes{{index+1}}" id="startDiscountTimes{{index+1}}"  @focus="initTime" readonly="readonly">
                                            <%--<span class="input-group-btn">
                                                <button class="btn default" type="button">
                                                    <i class="fa fa-clock-o"></i>
                                              </button>
                                            </span>--%>
                                        </div>
                                        <div class="input-group" style="margin-left: 212px;margin-top: -24px;">至</div>
                                        <div class="input-group" style="margin-left:260px;margin-top: -29px;">
                                            <input type="text" class="form-control timepicker timepicker-no-seconds" name="endDiscountTimes{{index+1}}" id="endDiscountTimes{{index+1}}"  @focus="initTime" readonly="readonly">
                                            <%--<span class="input-group-btn">
                                                <button class="btn default" type="button">
                                                    <i class="fa fa-clock-o"></i>
                                              </button>
                                            </span>--%>
                                        </div>
                                    </div>
                                </div>
                                <%--折扣率--%>
                                <div style="padding: 10px 0;margin-top: 10px;">
                                    <label class="col-sm-2 control-label">折扣率：</label>
                                    <div class="col-sm-8">
                                        <input onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                               				onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                                        	type="text" class="form-control" maxlength="2" style="margin-left: -15px;" name="discounts" required value="{{unit.discount}}">
                                    </div>
                                </div>
                                <div><b style="margin-left: -26px;">%</b></div>
                                <div class="col-sm-1" style="margin-top: -29px;">
                                    <a class="btn red" @click="removeUnit(unit)">移除</a>
                                </div>
                            </div>
                            <div class="form-group text-center">
                                <input type="hidden" name="id" v-model="m.id" />
                                <input class="btn green" type="submit" value="保存"/>
                                <a class="btn default" @click="cancel">取消</a>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>

    <div class="table-div">
        <div class="table-operator">
            <button class="btn green pull-right" @click="create">新建折扣项</button>
        </div>
        <div class="clearfix"></div>
        <div class="table-filter"></div>
        <div class="table-body">
            <table class="table table-striped table-hover table-bordered "></table>
        </div>
    </div>
</div>


<script>
    (function(){
        var $table = $(".table-body>table");
        var tb = $table.DataTable({
            bFilter: false, //过滤功能
            bSort: false, //排序功能
            serverSide: true,
            ajax : {
                "url": 'cardDiscount/datas',
                "type": 'post',
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
                    title : "名称",
                    data : "discountName",
                },
                {
                    title : "是否开启",
                    data : "isOpen",
                    createdCell:function(td,tdData){
                        var state = "未知";
                        if (tdData == 0){
                            state = "未启用";
                        }else {
                            state = "启用";
                        }
                        $(td).html(state);
                    }
                },
                {
                    title : "操作",
                    data : "id",
                    createdCell:function(td,tdData,rowData,row){
                        console.log(rowData);
                        var operator=[
                            C.createDelBtn(tdData,"cardDiscount/delete"),
                            C.createEditBtn(rowData),
                        ];
                        $(td).html(operator);
                    }
                }],
        });


        var C = new Controller(null,tb);
        var vueObj = new Vue({
            el:"#control",
            mixins:[C.formVueMix],
            data:{
                tbCardDiscountDetailList:[],
                weekList:[
                    {
                        name:'星期一',
                        id:1
                    },
                    {
                        name:'星期二',
                        id:2
                    },
                    {
                        name:'星期三',
                        id:3
                    },
                    {
                        name:'星期四',
                        id:4
                    },
                    {
                        name:'星期五',
                        id:5
                    },
                    {
                        name:'星期六',
                        id:6
                    },
                    {
                        name:'星期日',
                        id:7
                    },
                ]
            },
            methods:{
                addUnit:function(){
                    this.tbCardDiscountDetailList.push({startDiscountTimes:"",endDiscountTimes:"",discount:""});
                },
                removeUnit:function(unit){
                    this.tbCardDiscountDetailList.$remove(unit);
                },
                edit:function(model){
                    this.tbCardDiscountDetailList = model.tbCardDiscountDetailList == null ? [] : model.tbCardDiscountDetailList;
                    this.m= model;
                    this.openForm();
                    setTimeout(function () {
                        for(var l=0;l<model.tbCardDiscountDetailList.length;l++){
                            var weekObj = model.tbCardDiscountDetailList[l].discountWeek.split(",");
                            var boxes = document.getElementsByName("discountWeeks"+(l+1));
                            for (i = 0; i < boxes.length; i++) {
                                for (j = 0; j < weekObj.length; j++) {
                                    if (boxes[i].value == weekObj[j]) {
                                        $("#uniform-inline"+(l+1)+"Checkbox"+(i+1)).addClass("checker focus");
                                        $("#uniform-inline"+(l+1)+"Checkbox"+(i+1)+" span").addClass("checked");
                                        boxes[i].checked=true;
                                        break
                                    }
                                }
                            }
                            var timeObj = model.tbCardDiscountDetailList[l].discountTime.split(",");
                            for (p = 0; p < timeObj.length; p++) {
                                $("#startDiscountTimes"+(l+1)).val(timeObj[0]);
                                $("#endDiscountTimes"+(l+1)).val(timeObj[1]);
                                break
                            }
                        }
                    },1);
                },
                create:function(){
                    this.tbCardDiscountDetailList = [];
                    this.m={};
                    this.openForm();
                    this.addUnit();
                },
                initTime : function() {
                    $(".timepicker-no-seconds").timepicker({
                        autoclose : true,
                        showMeridian : false,
                        minuteStep : 5
                    });
                }
            }
        });
        C.vue=vueObj;
    }());
</script>
