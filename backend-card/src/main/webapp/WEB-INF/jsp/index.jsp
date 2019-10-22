<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="tag-head.jsp" %>

<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <base href="<%=basePath%>" />
    <meta charset="utf-8" />
    <title>美食广场</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1" name="viewport" />
    <meta content="" name="description" />
    <meta content="" name="author" />
    <!-- BEGIN GLOBAL MANDATORY STYLES -->
    <link href="<%=resourcePath%>/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <link href="<%=resourcePath%>/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
    <link href="<%=resourcePath%>/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <link href="<%=resourcePath%>/assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css" />
    <link href="<%=resourcePath%>/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.css" rel="stylesheet" type="text/css" />
    <!-- END GLOBAL MANDATORY STYLES -->
    <!-- BEGIN PAGE LEVEL PLUGINS -->
    <link href="<%=resourcePath%>/assets/global/plugins/datatables/datatables.min.css" rel="stylesheet" type="text/css" />
    <link href="<%=resourcePath%>/assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="<%=resourcePath%>/assets/global/plugins/jstree/dist/themes/default/style.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=resourcePath%>/assets/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=resourcePath%>/assets/global/plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css"/>

    <link rel="stylesheet" type="text/css" href="<%=resourcePath%>/assets/global/plugins/bootstrap-toastr/toastr.min.css">
    <link href="<%=resourcePath%>/assets/global/plugins/bootstrap-colorpicker/css/colorpicker.css" rel="stylesheet" type="text/css" />
    <link href="<%=resourcePath%>/assets/global/plugins/jquery-minicolors/jquery.minicolors.css" rel="stylesheet" type="text/css" />

    <link href="<%=resourcePath%>/assets/global/plugins/select2/css/select2.min.css" rel="stylesheet" type="text/css" />
    <link href="<%=resourcePath%>/assets/global/plugins/select2/css/select2-bootstrap.min.css" rel="stylesheet" type="text/css" />
    <link href="<%=resourcePath%>/assets/global/plugins/typeahead/typeahead.css" rel="stylesheet" type="text/css" />
    <link href="<%=resourcePath%>/assets/global/plugins/bootstrap-toastr/toastr.min.css" rel="stylesheet" type="text/css" />

    <!-- END PAGE LEVEL PLUGINS -->
    <!-- BEGIN THEME GLOBAL STYLES -->
    <link href="<%=resourcePath%>/assets/global/css/components.min.css" rel="stylesheet" id="style_components" type="text/css" />
    <link href="<%=resourcePath%>/assets/global/css/plugins.min.css" rel="stylesheet" type="text/css" />
    <!-- END THEME GLOBAL STYLES -->
    <!-- BEGIN THEME LAYOUT STYLES -->
    <link href="<%=resourcePath%>/assets/layouts/layout/css/layout.min.css" rel="stylesheet" type="text/css" />
    <link href="<%=resourcePath%>/assets/layouts/layout/css/themes/darkblue.min.css" rel="stylesheet" type="text/css" id="style_color" />
    <link href="<%=resourcePath%>/assets/layouts/layout/css/custom.min.css" rel="stylesheet" type="text/css" />

    <link href="<%=resourcePath %>/assets/customer/css/custom.css" rel="stylesheet"s type="text/css" />
    <!-- END THEME LAYOUT STYLES -->
    <link rel="shortcut icon" href="assets/pages/img/favicon.ico" />
    <link href="<%=resourcePath%>/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css" />
	<link href="<%=resourcePath%>/assets/global/css/common.css" rel="stylesheet" type="text/css" />
    <%--    wangEditor  富文本编辑器  --%>
    <link href="<%=resourcePath%>/assets/global/plugins/wangEditor-2.1.22/css/wangEditor.css" rel="stylesheet">

    <!-- END HEAD -->

<body class="page-header-fixed page-sidebar-closed-hide-logo page-content-white">
<!-- BEGIN HEADER -->
<jsp:include page="head.jsp"></jsp:include>
<!-- END HEADER -->
<!-- BEGIN HEADER & CONTENT DIVIDER -->
<div class="clearfix"> </div>
<div class="page-container">
    <div class="page-sidebar-wrapper">
        <div class="page-sidebar navbar-collapse collapse">
            <ul class="page-sidebar-menu  page-header-fixed " data-keep-expanded="false" data-auto-scroll="true" data-slide-speed="200" style="padding-top: 20px">
                <li class="nav-item">
                    <a href="javascript:;" class="nav-link nav-toggle">
                        <i class="fa"></i>
                        <span class="title">储值卡管理</span>
                        <span class="arrow "></span>
                    </a>
                    <ul class="sub-menu">
                        <li class="nav-item">
                            <a href="card/create" class="ajaxify ">
                                <i class="fa"></i>开卡</a>
                        </li>
                        <li class="nav-item">
                            <a href="javascript:;" class="nav-link nav-toggle" style="margin-left: -5px;">
                                <i class="fa"></i>
                                <span class="title">充值</span>
                                <span class="arrow "></span>
                            </a>
                            <ul class="sub-menu">
                                <li class="nav-item">
                                    <a href="card/recharge" class="ajaxify ">
                                    <i class="fa"></i>单卡充值</a>
                                </li>
                                <li class="nav-item">
                                    <a href="card/batch" class="ajaxify ">
                                    <i class="fa"></i>批量充值</a>
                                </li>
                            </ul>
                        </li>
                        <li class="nav-item">
                            <a href="card/pin" class="ajaxify "><i class="fa"></i>销卡</a>
                        </li>
                        <li class="nav-item">
                            <a href="card/loss" class="ajaxify "><i class="fa"></i>挂失</a>
                        </li>
                        <li class="nav-item">
                            <a href="card/fill" class="ajaxify "><i class="fa"></i>补卡</a>
                        </li>
                        <li class="nav-item">
                            <a href="card/activated" class="ajaxify "><i class="fa"></i>激活</a>
                        </li>
                        <!--<li class="nav-item">
                            <a href="cardCustomer/accountLogView" class="ajaxify "><i class="fa"></i>消费查询</a>
                        </li>-->
                    </ul>
                </li>
                <li class="nav-item">
                    <a href="javascript:;" class="nav-link nav-toggle">
                        <i class="fa"></i>
                        <span class="title">用户中心</span>
                        <span class="arrow "></span>
                    </a>
                    <ul class="sub-menu">
                        <li class="nav-item">
                            <a href="cardCustomer/employeeView" class="ajaxify ">
                                <i class="fa"></i>员工卡</a>
                        </li>
                        <li class="nav-item">
                            <a href="cardCustomer/normalView" class="ajaxify ">
                                <i class="fa"></i>普通卡</a>
                        </li>
                        <li class="nav-item">
                            <a href="cardCustomer/temporaryView" class="ajaxify ">
                                <i class="fa"></i>临时卡</a>
                        </li>
                    </ul>
                </li>
                <li class="nav-item">
                    <a href="javascript:;" class="nav-link nav-toggle">
                        <i class="fa"></i>
                        <span class="title">运营数据</span>
                        <span class="arrow "></span>
                    </a>
                    <ul class="sub-menu">
                        <li class="nav-item">
                            <a href="businessData/businessData" class="ajaxify ">
                                <i class="fa"></i>营业数据</a>
                        </li>
                        <li class="nav-item">
                            <a href="activateData/activateData" class="ajaxify ">
                                <i class="fa"></i>开卡数据</a>
                        </li>
                        <li class="nav-item">
                            <a href="rechargeData/rechargeData" class="ajaxify ">
                                <i class="fa"></i>充值数据</a>
                        </li>
                        <li class="nav-item">
                            <a href="pincardData/pincardData" class="ajaxify ">
                                <i class="fa"></i>销卡数据</a>
                        </li>
                        <li class="nav-item">
                            <a href="nextshiftData/nextshiftData" class="ajaxify ">
                                <i class="fa"></i>交班数据</a>
                        </li>
                    </ul>
                </li>
                <li class="nav-item">
                    <a href="javascript:;" class="nav-link nav-toggle">
                        <i class="fa"></i>
                        <span class="title">系统设置</span>
                        <span class="arrow "></span>
                    </a>
                    <ul class="sub-menu">
                        <li class="nav-item">
                            <a href="cardDiscount/view" class="ajaxify ">
                                <i class="fa"></i>时间折扣设置</a>
                        </li>
                        <li class="nav-item">
                            <a href="cardCompany/view" class="ajaxify ">
                                <i class="fa"></i>公司管理</a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
    <div class="page-content-wrapper">
        <div class="page-content">
            <div class="page-content-body">

            </div>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"></jsp:include>
<!-- END FOOTER -->
<!--[if lt IE 9]>
<script src="<%=resourcePath%>/assets/global/plugins/respond.min.js"></script>
<script src="<%=resourcePath%>/assets/global/plugins/excanvas.min.js"></script>
<![endif]-->
<!-- BEGIN CORE PLUGINS -->
<script src="<%=resourcePath%>/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/js.cookie.min.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="<%=resourcePath%>/assets/pages/scripts/components-date-time-pickers.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="<%=resourcePath%>/assets/global/scripts/datatable.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/scripts/moment.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/scripts/moment-with-locales.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/datatables/datatables.min.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/jstree/dist/jstree.js"></script>
<script src="<%=resourcePath%>/assets/global/plugins/select2/js/select2.full.min.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/typeahead/handlebars.min.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/typeahead/typeahead.bundle.min.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/customer/vuejs/vue.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/bootstrap-toastr/toastr.min.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/jquery-validation/js/localization/messages_zh.js" type="text/javascript" charset="utf-8"></script>
<script src="<%=resourcePath%>/assets/global/plugins/bootstrap-colorpicker/js/bootstrap-colorpicker.js" type="text/javascript"></script>
<script src="<%=resourcePath%>/assets/global/plugins/jquery-minicolors/jquery.minicolors.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->

<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="<%=resourcePath%>/assets/global/scripts/app.min.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->

<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="<%=resourcePath%>/assets/layouts/layout/scripts/layout.js" type="text/javascript"></script>
<!--	CUSTOMER SCRIPTS -->

<script src="<%=resourcePath%>/assets/customer/datatable_lang_ch.js" type="text/javascript" charset="utf-8"></script>
<script src="<%=resourcePath%>/assets/customer/dialog/customer_dialog.js" type="text/javascript" charset="utf-8"></script>
<script src="<%=resourcePath%>/assets/customer/utils.js" type="text/javascript" charset="utf-8"></script>
<script src="<%=resourcePath%>/assets/customer/vue-components.js?v=1" type="text/javascript" charset="utf-8"></script>
<script src="<%=resourcePath%>/assets/customer/controller.js" type="text/javascript" charset="utf-8"></script>

<script src="<%=resourcePath%>/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.js" type="text/javascript"></script>
</body>

<script type="text/javascript">
    function format(str) {
        Date.prototype.format(str)
    }
</script>
</html>