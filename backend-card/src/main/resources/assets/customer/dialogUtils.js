/**
 * dialog util
 * 封装了 artDialog ，提供了一些常用的confirm提示框
 */
var DialogUtil = {
	confirmDialog: function(text,title,successcbk,cancelcbk){

		var width = text.length*16;
		width = width>200?width:200;
		var cDialog = new dialog({
			title:title||"",
			content:text||"",
			width:width,
			ok:function(){
				if(typeof successcbk=="function"){
					successcbk();
				}
			},
			cancel:function(){
				if(typeof cancelcbk=="function"){
					cancelcbk();
				}	
			}
		});
		cDialog.showModal();
	},
	
	deleteConfirm: function(successCbk){
		DialogUtil.confirmDialog("你确定要删除吗?","警告",successCbk);
	},
	
	cancelConfirm: function(successCbk){
		DialogUtil.confirmDialog("你确定要取消吗?","提醒",successCbk);
	}
	
};