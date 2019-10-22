<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
    .form-horizontal .checkbox, .form-horizontal .radio {
        min-height: 34px;
        margin-left: 16px;
    }
    table thead tr th,table tbody tr td {
        text-align: center;
        vertical-align: middle;
    }
    .table>tbody>tr>td, .table>tbody>tr>th, .table>thead>tr>td, .table>thead>tr>th {
        vertical-align: middle;
        max-width: 60%;
    }
</style>
<div id="control">
    <div class="row form-div" v-if="showform">
        <div class="col-md-offset-3 col-md-6" >
            <div class="portlet light bordered" style="width:750px;height:320px;">
                <div class="portlet-title">
                    <div class="caption">
                        <span class="caption-subject bold font-blue-hoki">新增公司</span>
                    </div>
                </div>
                <div class="portlet-body">
                    <form class="form-horizontal" role="form" action="{{m.id?'cardCompany/modify':'cardCompany/create'}}" @submit.prevent="save">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">新增公司:</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" style="margin-left: 15px;" name="companyName" required v-model="m.companyName">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">联系人:</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" style="margin-left: 15px;" name="contactName" required v-model="m.contactName">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">联系电话:</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" style="margin-left: 15px;" name="contactMobile" required v-model="m.contactMobile">
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
            <button class="btn blue pull-right" @click="create">新增公司</button>
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
                "url": 'cardCompany/datas',
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
                    title : "公司名称",
                    data : "companyName",
                },
                {
                    title : "操作",
                    data : "id",
                    createdCell:function(td,tdData,rowData,row){
                        console.log(rowData);
                        var operator=[
                            C.createDelBtn(tdData,"cardCompany/delete"),
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

            },
            methods:{
                edit:function(model){
                    this.m= model;
                    this.openForm();
                },
                create:function(){
                    this.openForm();
                }
            }
        });
        C.vue=vueObj;
    }());
</script>
