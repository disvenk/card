<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="control">
    <div class="portlet light bordered">
        <div class="portlet-title">
            <div class="caption">
                <span class="caption-subject bold font-blue-hoki">消费查询</span>
            </div>
        </div>
        <div class="portlet-body">
            <div class="form-body">
                <div class="form-group row">
                    <label class="col-md-1 control-label">卡号<span style="color:#FF0000;">*</span></label>
                    <div class="col-md-2">
                        <input type="text" class="form-control" name="cardId" v-model="parameterId"
                               required="required" placeholder="请刷卡" v-bind:readonly="true">
                    </div>
                    <div>
                        <a href='cardCustomer/accountLog?cardId={{parameterId}}' id="id-checkbox" class='btn green ajaxify'
                           @click="queryCard">查询</a>
                        <%--<input class="btn green"  type="submit" @click="queryCard"  value="查询"/>&nbsp;&nbsp;&nbsp;--%>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<script>
    (function () {
        var $table = $(".table-body>table");
        var tb = $table.DataTable({
            ajax: {
                url: "card/rechargeData"
            }
        });
        var C = new Controller(null, tb);
        var vueObj = new Vue({
            el: "#control",
            mixins: [C.formVueMix],
            data: {
                parameterId: null,
                flag:false
            },
            created: function () {

            },
            ready:function () {

                $("#id-checkbox").bind("click", function(event) {
                    event.preventDefault();
                }, false);
            },
            methods: {
                queryCard: function () {
                     var that = this;
                     getCardNum(function(num){
                        that.parameterId = num;
                        if(that.parameterId == null || that.parameterId == ''){
                            that.flag = false;
                            alert('连接读卡服务失败');
                            }else{
                                $("#id-checkbox").unbind( "click" )
                        }
                      });
                },
            }
        });
        C.vue = vueObj;

    }());
</script>
