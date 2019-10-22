<%@ page language="java" pageEncoding="utf-8" %>
<%@include file="tag-head.jsp" %>
<div class="page-header navbar navbar-fixed-top">
    <!-- BEGIN HEADER INNER -->
    <div class="page-header-inner ">
        <!-- BEGIN LOGO -->
        <div class="page-logo">
            <a href="javascript:(0)"> <img src="<%=resourcePath%>/assets/pages/img/Resto+.png" width="110px"
                                           height="20px" alt="logo" class="logo-default" style="margin-top: 15px;"/>
            </a>
            <div class="menu-toggler sidebar-toggler">
            </div>
        </div>
        <a href="javascript:;" class="menu-toggler responsive-toggler"
           data-toggle="collapse" data-target=".navbar-collapse">
        </a>
        <div class="top-menu">
            <ul class="nav navbar-nav pull-right">
                <li class="dropdown dropdown-user">
                    <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown"
                       data-close-others="true">
                        <span class="username username-hide-on-mobile">
                            <span>美食广场</span>
							<span class="namespan">${USER_INFO.username } </span>
                        </span>
                        <i class="fa fa-angle-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-default">
                        <li class="divider"></li>
                        <li><a href="logout">
                            <i class="icon-users">
                            </i>
                            切换用户
                            </a>
                        </li>
                        <li><a href="logout">
                            <i class="icon-power">
                            </i>
                            注销登录
                        </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>