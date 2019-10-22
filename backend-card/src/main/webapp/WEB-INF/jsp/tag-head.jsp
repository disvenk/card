<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String resourcePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
    String basePath = resourcePath + "/";
%>
<body>

<script type="text/javascript">
            <%--var socket;--%>
            <%--if(typeof(socket) == "undefined") {--%>
                <%--console.log("您的浏览器不支持WebSocket");--%>
                <%--&lt;%&ndash;return;&ndash;%&gt;--%>

                <%--//实现化WebSocket对象，指定要连接的服务器地址与端口--%>
                <%--socket = new WebSocket("ws://127.0.0.1:8000");--%>
                <%--//打开事件--%>
                <%--socket.onopen = function() {--%>
                    <%--console.log("Socket 已打开");--%>
                <%--};--%>
                <%--//获得消息事件--%>
                <%--socket.onmessage = function(msg) {--%>
                    <%--var data = msg.data;--%>
                    <%--console.log(data);--%>
                <%--};--%>
                <%--//关闭事件--%>
                <%--socket.onclose = function() {--%>
                    <%--console.log("Socket已关闭");--%>
                <%--};--%>
                <%--//发生了错误事件--%>
                <%--socket.onerror = function() {--%>
                    <%--console.log("发生了错误");--%>
                <%--}--%>
            <%--}--%>

            <%--function getCardNo(){--%>
                <%--&lt;%&ndash;socket.send('getCardNo');&ndash;%&gt;--%>
            <%--}--%>

            	function getCardNum(cb){
                        $.ajax({
                            url:'http://192.168.1.132:8888/newPosApi/getCardNum',
                            //url:'newPosApi/getCardNum',
                            async:false,
                            success:function(data){
                                    var msg = data.message;
                                    if (data.success){
                                        cb(msg);
                                    }else {
                                        alert(msg);
                                    }
                            },error: function(){ //失败后执行
                                    cb(null);
                                }
                            });

                  }


</script>
</body>