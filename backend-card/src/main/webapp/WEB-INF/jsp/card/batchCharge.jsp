<%@ page language="java" pageEncoding="utf-8" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
				margin: 8px auto;

			}

			.modal-body ul li .title {
				display: inline-block;
				width: 40%;
				text-align: right;
			}

			.modal-body ul li .value {
				width: 60%;
				text-align: left;
				display: inline-block;
			}
		</style>
		<div id="control">
			<!-- 公司列表 -->
			<div v-show="!showDetailFlag">
				<div class="form-group row">
					<label class="col-md-1 control-label" style="top: 10px;">公司
						<span style="color:#FF0000;">*</span>
					</label>
					<div class="col-md-3">
						<select name="companyId" v-model="companDetailId" class="bs-select form-control" @change="changeCompanyId">
							<option selected value="">请选择</option>
							<option v-for="company in companys" value="{{company.id}}">
								{{company.companyName}}
							</option>
						</select>
					</div>
				</div>
				<!-- 公司列表 -->
				<div role="tabpanel" class="tab-pane">
					<div class="panel panel-primary" style="border-color:write;">
						<div class="panel panel-info">
							<div class="panel-heading text-center">
								<strong style="margin-right:100px;font-size:20px">公司列表</strong>
							</div>
							<div class="panel-body">
								<table id="cardCustomerTable" class="table table-striped table-bordered table-hover" width="100%"></table>
							</div>
						</div>
					</div>
				</div>
				<!-- 批量充值form -->
				<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false" aria-labelledby="exampleModalLabel">
					<div class="modal-dialog" role="document">
						<div class="modal-content" style="margin-top: 170px;">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-label="Close">
									<span aria-hidden="true">×</span>
								</button>
								<h4 class="modal-title" id="exampleModalLabel" style="text-align:center">批量充值</h4>
							</div>
							<div class="modal-body" align="center">
								<ul>
									<li class="row">
										<span class="title col-md-6">企业：</span>
										<span class="value col-md-6">{{formData.companyName}}</span>
									</li>
									<li class="row">
										<span class="title col-md-6">充值人数：</span>
										<span class="value col-md-6">{{formData.employeesNum}}</span>
									</li>
									<li class="row">
										<span class="title col-md-6">每人充值金额：</span>
										<span class="value col-md-6"><input name="chargeMoney" type="number" v-model="formData.chargeMoney" ></span>
									</li>
									<li class="row">
										<span class="title col-md-6">总计：</span>
										<span class="value col-md-6">{{formData.employeesNum * formData.chargeMoney}}</span>
									</li>
									<li class="row">
										<span class="title col-md-6">充值方式：</span>
										<span class="value col-md-6">
											<!-- <label><input type="radio" v-model="formData.payType" name="payType" value="1">微信</label>
											<label><input type="radio" v-model="formData.payType" name="payType" value="2">支付宝</label> -->
											<label><input type="radio" v-model="formData.payType" name="payType" value="3" >支票</label>
											<label><input type="radio" v-model="formData.payType" name="payType" value="4">现金</label>
										</span>
									</li>
									
								</ul>
							</div>
							<div class="modal-footer text-center" style="text-align:center;">
								<a class="btn default" data-dismiss="modal">取消</a>
								<input class="btn btn-primary" style="margin-left: 40px;" type="submit" value="确定" @click="confirmCharge" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- 某个公司下 详情页面 -->
			<div v-show="showDetailFlag">
				<div class="form-group row">
					<!-- <label class="col-md-1 control-label" style="top: 10px;">公司
						<span style="color:#FF0000;">*</span>
					</label> -->
					<div class="col-md-12">
						<form class="form-inline">
							<button type="button" class="btn btn-primary" @click="goBack()">返回</button>&nbsp;						
							<div class="form-group" style="margin-right: 50px;">
								<label for="endDate">手机号：</label>
								<input type="text" class="form-control" id="telephone" v-model="telephone"  placeholder="请输入手机号码">
							</div>
							<button type="button" class="btn btn-primary" @click="searchInfo">查询</button>&nbsp;
							<button type="button" class="btn btn-primary" @click="checkboxCharge()">充值</button>&nbsp;
						</form>
					</div>
				</div>
				<!-- 某公司下 详情列表 -->
				<div role="tabpanel" class="tab-pane">
					<div class="panel panel-primary" style="border-color:write;">
						<div class="panel panel-info">
							<div class="panel-heading text-center">
								<strong style="margin-right:100px;font-size:20px">{{companyDetailName}}员工列表</strong>
							</div>
							<div class="panel-body">
								<table id="cardEmployeeTable" class="table table-striped table-bordered table-hover" width="100%"></table>
							</div>
						</div>
					</div>
				</div>

				<!-- checkbox 批量充值form -->
				<div class="modal fade" id="exampleModal2" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false" aria-labelledby="exampleModalLabel">
					<div class="modal-dialog" role="document">
						<div class="modal-content" style="margin-top: 170px;">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-label="Close">
									<span aria-hidden="true">×</span>
								</button>
								<h4 class="modal-title" id="exampleModal2Label" style="text-align:center">批量充值</h4>
							</div>
							<div class="modal-body" align="center">
								<ul>
									<li class="row">
										<span class="title col-md-6">企业：</span>
										<span class="value col-md-6">{{companyDetailName}}</span>
									</li>
									<li class="row">
										<span class="title col-md-6">充值人数：</span>
										<span class="value col-md-6">{{selectedEmployee.length}}</span>
									</li>
									<li class="row">
										<span class="title col-md-6">每人充值金额：</span>
										<span class="value col-md-6"><input name="chargeMoney" type="text" v-model="formData2.chargeMoney" ></span>
									</li>
									<li class="row">
										<span class="title col-md-6">总计：</span>
										<span class="value col-md-6">{{selectedEmployee.length * formData2.chargeMoney}}</span>
									</li>
									<li class="row">
										<span class="title col-md-6">充值方式：</span>
										<span class="value col-md-6">
											<!-- <label><input type="radio" v-model="formData2.payType" name="payType" value="1">微信</label>
											<label><input type="radio" v-model="formData2.payType" name="payType" value="2">支付宝</label> -->
											<label><input type="radio" v-model="formData2.payType" name="payType" value="3" >支票</label>
											<label><input type="radio" v-model="formData2.payType" name="payType" value="4">现金</label>
										</span>
									</li>
									
								</ul>
							</div>
							<div class="modal-footer text-center" style="text-align:center;">
								<a class="btn default" data-dismiss="modal">取消</a>
								<input class="btn btn-primary" style="margin-left: 40px;" type="submit" value="确定" @click="confirmCharge2" />
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
					//          ajax : {
					//              url : "card/rechargeData"
					//          }
				});
				var C = new Controller(null, tb);
				var vueObj = new Vue({
					el: "#control",
					mixins: [C.formVueMix],
					data: {
						companys: [],
						activitys: [],
						companyId: null,
						telephone: null,		   // 某公司的手机号
						companDetailId: null,
						companyDetailName: '',		// 详情页公司名字
						formData: {},				// 公司全部批量充值
						cardEmployeeTable: null, // 某公司下员工列表【查看详情】
						showDetailFlag: false,     // 是否显示详情页
						selectedEmployee: [], 		// 被选中的要充值的checkbox 卡号
						formData2: {chargeMoney: 0},				// checkbox 的批量充值
					},
					created: function () {
						this.createAppriseTable();
					},
					computed: {
						// companDetailId: function () {
						// for (var i = 0; i < this.companys.length; i++) {
						//     var c = this.companys[i];
						//     if (c.id == this.companyId) {
						//         return c.id;
						//     }
						// }
						// return 1;
						// }
					},
					methods: {
						changeCompanyId: function () {
							this.cardCustomerTable.ajax.reload();
						},
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
								serverSide: true,
								bFilter: false, //过滤功能
								bSort: false, //排序功能
								ajax: {
									"url": 'cardCompany/listCompanyDatas',
									"type": 'post',
									"data": function (d) {
										//添加额外的参数传给服务器
										if (that.companDetailId) {
											d.companyId = that.companDetailId;
										}
									}
								},
								columns: [{
										title: "序号",
										data: null,
										render: function (data, type, full, meta) {
											return meta.row + 1 + meta.settings._iDisplayStart;
										}
									},
									{
										title: "公司名称",
										data: "companyName"
									},
									{
										title: "员工数量",
										data: "employeesNum"
									},
									{
										title: "操作",
										data: "companyId",
										orderable: false,
										createdCell: function (td, tdData, rowData) {
											// console.log(tdData);
											// console.log(rowData);
											var buttonDetail = $("<button type='button' class='btn blue'>查看详情</button>");
											buttonDetail.click(function () {
												that.showDetail(rowData);
											});
											var buttonForm = $(
												"<button type='button' class='btn blue' style='margin-left: 10px;' >批量充值</button>");
											buttonForm.click(function () {
												that.showForm(rowData);
											})
											$(td).html(buttonDetail);
											$(td).append(buttonForm);
										}
									}
								]
							});
						},
						createEmployeeTable: function () {
							var that = this;
							that.cardEmployeeTable = $("#cardEmployeeTable").DataTable({
								lengthMenu: [
									[10, 25, 50, 100],
									[10, 25, 50, 100]
								],
								order: [
									[1, "desc"]
								],
								serverSide: true,
								bFilter: false, //过滤功能
								bSort: false, //排序功能
								ajax: {
									"url": 'cardCompany/listCompanyDetailDatas',
									"type": 'post',
									"data": function (d) {
										//添加额外的参数传给服务器
										d.companyId = that.companDetailId;
										if (that.telephone) {
											d.telephone = that.telephone;
										}
									}
								},
								columns: [
									{
										title: "",
										data: "cardId",
										createdCell: function (td, tdData, rowData) {
											
											var checkbox = $("<input type='checkbox' name='selectEmployee'>");
											checkbox.click(function () {
												// console.log($(this).prop('checked'));
												// tdData	
												var checked = $(this).prop('checked');
												that.toggleCheckbox($(this).prop('checked'), rowData);
											});
					
											$(td).html(checkbox);
										}
									},
									{
										title: "序号",
										data: null,
										render: function (data, type, full, meta) {
											return meta.row + 1 + meta.settings._iDisplayStart;
										}
									},
									{
										title: "卡号",
										data: "cardId"
									},
									{
										title: "折扣",
										data: "discountName"
									},
									{
										title: "姓名",
										data: "customerName"
									},
									{
										title: "手机号码",
										data: "telephone"
									},
									{
										title: "身份证号",
										data: "idCard"
									},
									{
										title: "账户余额(元)",
										data: "remain"
									},
									{
										title: "卡状态",
										data: "cardState",
										createdCell: function(td,tdData) {
											var state = "未知";
											if (tdData == 1) {
												state = "正常";
											} else if(tdData == 2) {
												state = "挂失";
											}
											$(td).html(state);
										}
									}
								]
							});
						},
						// 查看详情
						showDetail(rowData) {
							this.showDetailFlag = true;
							this.companDetailId = rowData.companyId;
							this.companyDetailName = rowData.companyName;
							if (this.cardEmployeeTable) {
								this.cardEmployeeTable.ajax.reload();	
							} else {
								// 没有初始化过，则初始化
								this.createEmployeeTable();
							}
						},
						// 批量充值
						showForm(rowData) {
							rowData.chargeMoney = 0;
							this.formData = rowData;
							if (this.formData.employeesNum == 0) {
								return C.systemButtonNo('error', "企业员工数是为0");
							} else {
								$("#exampleModal").modal('show');
							}
						},
						// 批量充值 确定
						confirmCharge() {
							var that = this;
							if ( ! /^[0-9.]+$/.test(that.formData.chargeMoney) || that.formData.chargeMoney<=0) {
								return C.systemButtonNo('error', "请输入准确的充值金额");
							}
							if ( ! /^[1-4]$/.test(that.formData.payType)) {
								return C.systemButtonNo('error', "请选择充值方式");
							}
							$.ajax({
								type: 'post',
								url: 'card/allChargeCard',
								data: this.formData,
								success: function(data) {
									// consoloe.log(data)
									if (data.success) {
										that.formData.chargeMoney = 0;
										C.systemButtonNo('success', "充值成功");
										$("#exampleModal").modal('hide');
									}
								}
							})
						},
						// 返回按钮
						goBack: function () {
							this.showDetailFlag = false;
						},
						// 手机号查询
						searchInfo: function() {
							this.cardEmployeeTable.ajax.reload();	
						},
						// 勾选详情页面的checkbox
						toggleCheckbox: function(flag, rowData) {
							if (flag) {
								this.selectedEmployee.push(rowData.cardCustomerId);
							} else {
								var index = this.selectedEmployee.indexOf(rowData.cardCustomerId);
								if (index > -1) {
									this.selectedEmployee.splice(index, 1)
								}
							}
							// console.log(this.selectedEmployee)
						},
						// 详情页的充值
						checkboxCharge: function() {
							// console.log(this.selectedEmployee)
							if (this.selectedEmployee.length === 0) {
								return C.systemButtonNo('error', "请勾选要充值的员工");
							} else {
								$("#exampleModal2").modal('show');
							}
						},
						// 确定充值
						confirmCharge2: function() {
							var that = this;
							if ( ! /^[0-9.]+$/.test(that.formData2.chargeMoney) || that.formData2.chargeMoney<=0) {
								return C.systemButtonNo('error', "请输入准确的充值金额");
							}
							if ( ! /^[1-4]$/.test(that.formData2.payType)) {
								return C.systemButtonNo('error', "请选择充值方式");
							}
							var params = that.selectedEmployee.map((item, index) => {
								return {
									chargeMoney: that.formData2.chargeMoney,
									companyId: that.companDetailId,
									cardCustomerId: item,
									payType: that.formData2.payType
								}
							})
							// console.log(params)
							// debugger
							$.ajax({
								type: 'post',
								dataType: 'json',
								url: 'card/allOneChargeCard',
								data: JSON.stringify(params),
								contentType: "application/json;charset=utf-8",
								success: function(data) {
									// consoloe.log(data)
									if (data.success) {
										// 刷新页面
										that.cardEmployeeTable.ajax.reload();
										// 清空checkbox
										that.selectedEmployee = [];
										that.formData2.chargeMoney = 0;
										C.systemButtonNo('success', "充值成功");
										$("#exampleModal2").modal('hide');
									}
								}
							})
						}
					},
					ready: function () {
						var that = this;
						$.get("card/activity/list", function (data) {
							that.activitys = data.data;
						});
						$.get("cardCompany/list", function (data) {
							that.companys = data.data;
						});
					}
				});
				C.vue = vueObj;

			}());
		</script>