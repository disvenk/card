package com.resto.msgc.backend.card.controller;

import com.alibaba.fastjson.JSONObject;
import com.resto.msgc.backend.card.dto.*;
import com.resto.msgc.backend.card.entity.TbCardCompany;
import com.resto.msgc.backend.card.responseEntity.RestResponseEntity;
import com.resto.msgc.backend.card.service.PinCardDataService;
import com.resto.msgc.backend.card.util.ExcelUtil;
import com.resto.msgc.backend.card.util.FileUtils;
import io.swagger.annotations.Api;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by disvenk.dai on 2018-04-14 10:59
 */

@Api
@Controller
@RequestMapping("pincardData")
public class PincardDataContrller {

    @Resource
    PinCardDataService pinCardDataService;

    @RequestMapping("pincardData")
    public String index() {
        return "PincardData/list";
    }

    /**
    *@Description:查询指定起始时间所有的数据
    *@Author:disvenk.dai
    *@Date:11:45 2018/4/19 0019
    */
    @RequestMapping("selectCardRefundByStartAndEnd")
    public ResponseEntity selectCardRefundByStartAndEnd(String beginDate, String endDate){
        JSONObject jsonObject = pinCardDataService.selectCardRefundByStartAndEnd(beginDate, endDate);
        return new ResponseEntity(new RestResponseEntity(100,"成功",jsonObject,null), HttpStatus.OK);
    }

    /**
     *@Description:导出指定起始时间的实体销卡数据报表
     *@Author:disvenk.dai
     *@Date:18:45 2018/4/17 0017
     */
    @RequestMapping("exprotEntityExcel")
    public ResponseEntity exprotEntityExcel(@RequestParam("beginDate") String beginDate,
                                            @RequestParam("endDate") String endDate,
                                            HttpServletResponse response,
                                            HttpServletRequest request){
        BrandEntityPinCardDataDto brandEntityPinCardDataDto = pinCardDataService.selectJson1(beginDate, endDate);

        List<Object> json1 = new ArrayList<>();
        if(brandEntityPinCardDataDto!=null){
            json1.add(brandEntityPinCardDataDto);
        }

        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("beginDate", beginDate);
        map.put("reportType", "实体卡销卡数据报表");// 表的头，第一行内容
        map.put("endDate", endDate);
        map.put("num", "16");// 显示的位置
        map.put("reportTitle", "实体卡销卡数据报表");// 表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] header1 = {{"销卡总数", "20"},
                {"退款总额(元)", "20"},
                {"现金退款金额(元)", "25"},
                {"支票退款金额(元)", "25"},
                {"微信退款金额(元)", "25"},
                {"支付宝退款金额(元)", "25"},
                {"天子星退款金额(元)","25"}
        };
        String[] column1 = {"pinCardCount", "count", "cashMoney", "chequeMoney", "wechatMoney","aliMoney","starMoney"};


        HSSFWorkbook workbook1 = new HSSFWorkbook();

        ExcelUtil<BrandEntityPinCardDataDto> excelUtil = new ExcelUtil<>();


        String str1 = "实体卡销卡数据报表" + beginDate + "至" + endDate + ".xls";

        try {
            excelUtil.ExportCardExcel(str1,request,response,workbook1, header1, column1, json1, map);

            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new ResponseEntity(new RestResponseEntity(200,"失败",null,null), HttpStatus.OK);
    }

    /**
     *@Description:导出指定起始时间的普通销卡数据报表
     *@Author:disvenk.dai
     *@Date:18:45 2018/4/17 0017
     */
    @RequestMapping("exprotNormalExcel")
    public ResponseEntity exprotNormalExcel(@RequestParam("beginDate") String beginDate,
                                            @RequestParam("endDate") String endDate,
                                            HttpServletResponse response,
                                            HttpServletRequest request){
        BrandEntityPinCardDataDto brandEntityPinCardDataDto = pinCardDataService.selectJson2(beginDate, endDate);

        List<Object> json2 = new ArrayList<>();
        if(brandEntityPinCardDataDto!=null){
            json2.add(brandEntityPinCardDataDto);
        }


        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("beginDate", beginDate);
        map.put("reportType", "普通卡销卡数据报表");// 表的头，第一行内容
        map.put("endDate", endDate);
        map.put("num", "16");// 显示的位置
        map.put("reportTitle", "普通卡销卡数据报表");// 表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] header2 = {{"销卡总数(元)", "20"},
                {"退款总额(元)", "20"},
                {"现金退款金额(元)", "25"},
                {"支票退款金额(元)", "25"},
                {"微信退款金额(元)", "25"},
                {"支付宝退款金额(元)", "25"},
                {"天子星退款金额(元)","25"}
        };
        String[] column2 = {"pinCardCount", "count", "cashMoney", "chequeMoney", "wechatMoney","aliMoney","starMoney"};


        HSSFWorkbook workbook2 = new HSSFWorkbook();

        ExcelUtil<BrandEntityPinCardDataDto> excelUtil2 = new ExcelUtil<>();

        String str2 = "普通卡销卡数据报表" + beginDate + "至" + endDate + ".xls";

        try {

            map.put("reportType", "普通卡销卡数据报表");
            excelUtil2.ExportCardExcel(str2,request,response,workbook2, header2, column2, json2, map);
            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(new RestResponseEntity(200,"失败",null,null), HttpStatus.OK);
    }

    /**
     *@Description:导出指定起始时间的员工卡销卡数据报表
     *@Author:disvenk.dai
     *@Date:18:45 2018/4/17 0017
     */
    @RequestMapping("exprotWorkerExcel")
    public ResponseEntity exprotWorkerExcel(@RequestParam("beginDate") String beginDate,
                                            @RequestParam("endDate") String endDate,
                                            HttpServletResponse response,
                                            HttpServletRequest request){

        List<BrandEntityPinCardDataDto> brandEntityPinCardDataDto = pinCardDataService.selectJsonList1(beginDate, endDate);
        List<Object> list = new ArrayList<>();
        if(brandEntityPinCardDataDto!=null && !brandEntityPinCardDataDto.isEmpty()){
            for(BrandEntityPinCardDataDto brandEntityPinCardDataDto1 : brandEntityPinCardDataDto){
                if(brandEntityPinCardDataDto1!=null){}
                list.add(brandEntityPinCardDataDto1);
            }
        }


        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("beginDate", beginDate);
        map.put("reportType", "员工卡销卡数据报表");// 表的头，第一行内容
        map.put("endDate", endDate);
        map.put("num", "16");// 显示的位置
        map.put("reportTitle", "员工卡销卡数据报表");// 表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] header3 = {
                {"公司名称", "20"},
                {"退卡总数", "20"},
                {"退款总额(元)", "20"},
                {"现金退款金额(元)", "25"},
                {"支票退款金额(元)", "25"},
                {"微信退款金额(元)", "25"},
                {"支付宝退款金额(元)", "25"},
                {"天子星退款金额(元)","25"}
        };
        String[] column3 = {"pinCardCount", "count", "cashMoney", "chequeMoney", "wechatMoney","aliMoney","starMoney"};

        HSSFWorkbook workbook3 = new HSSFWorkbook();

        ExcelUtil<BrandEntityPinCardDataDto> excelUtil3 = new ExcelUtil<>();

        String str3 = "员工卡销卡数据报表" + beginDate + "至" + endDate + ".xls";
        try {
            map.put("reportType", "员工卡销卡数据报表");
            excelUtil3.ExportCardExcel(str3,request,response,workbook3, header3, column3, list, map);

            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(new RestResponseEntity(200,"失败",null,null), HttpStatus.OK);
    }

    /**
     *@Description:导出指定月份实体卡销卡数据报表
     *@Author:disvenk.dai
     *@Date:18:45 2018/4/17 0017
     */
    @RequestMapping("exprotMothEntityExcel")
    public ResponseEntity exprotMothEntityExcel(@RequestParam("mothDate") String mothDate,
                                                HttpServletResponse response,
                                                HttpServletRequest request){
        BrandEntityPinCardDataDto brandEntityPinCardDataDto = pinCardDataService.selectJson11(mothDate);

        List<Object> json1 = new ArrayList<>();
        if(brandEntityPinCardDataDto!=null){
            json1.add(brandEntityPinCardDataDto);
        }


        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("mothDate", mothDate);
        map.put("reportType", "实体卡销卡数据报表");// 表的头，第一行内容
        map.put("num", "16");// 显示的位置
        map.put("reportTitle", "实体卡销卡数据报表");// 表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] header1 = {{"销卡总数", "20"},
                {"退款总额(元)", "20"},
                {"现金退款金额(元)", "25"},
                {"支票退款金额(元)", "25"},
                {"微信退款金额(元)", "25"},
                {"支付宝退款金额(元)", "25"},
                {"天子星退款金额(元)","25"}
        };
        String[] column1 = {"pinCardCount", "count", "cashMoney", "chequeMoney", "wechatMoney","aliMoney","starMoney"};


        HSSFWorkbook workbook1 = new HSSFWorkbook();

        ExcelUtil<BrandEntityCardChargeDto> excelUtil = new ExcelUtil<>();


        String str1 = "实体卡销卡数据"+mothDate+"月份报表.xls";

        try {
            excelUtil.ExportCardExcel(str1,request,response,workbook1, header1, column1, json1, map);

            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new ResponseEntity(new RestResponseEntity(200,"失败",null,null), HttpStatus.OK);
    }

    /**
     *@Description:导出指定月份的普通卡销卡数据报表
     *@Author:disvenk.dai
     *@Date:18:45 2018/4/17 0017
     */
    @RequestMapping("exprotMothNormalExcel")
    public ResponseEntity exprotMothNormalExcel(@RequestParam("mothDate") String mothDate,
                                                HttpServletResponse response,
                                                HttpServletRequest request){
        BrandEntityPinCardDataDto brandEntityPinCardDataDto = pinCardDataService.selectJson22(mothDate );

        List<Object> json2 = new ArrayList<>();
        if(brandEntityPinCardDataDto!=null){
            json2.add(brandEntityPinCardDataDto);
        }

        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("mothDate", mothDate);
        map.put("reportType", "普通卡销卡数据报表");// 表的头，第一行内容
        map.put("num", "16");// 显示的位置
        map.put("reportTitle", "普通卡销卡数据报表");// 表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] header2 = {{"销卡总数", "20"},
                {"退款总额(元)", "20"},
                {"现金退款金额(元)", "25"},
                {"支票退款金额(元)", "25"},
                {"微信退款金额(元)", "25"},
                {"支付宝退款金额(元)", "25"},
                {"天子星退款金额(元)","25"}
        };
        String[] column2 = {"pinCardCount", "count", "cashMoney", "chequeMoney", "wechatMoney","aliMoney","starMoney"};


        HSSFWorkbook workbook2 = new HSSFWorkbook();

        ExcelUtil<BrandEntityPinCardDataDto> excelUtil2 = new ExcelUtil<>();

        String str2 = "普通卡销卡数据"+mothDate+"月份报表.xls";

        try {

            map.put("reportType", "普通卡销卡数据报表");
            excelUtil2.ExportCardExcel(str2,request,response,workbook2, header2, column2, json2, map);
            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(new RestResponseEntity(200,"失败",null,null), HttpStatus.OK);
    }

    /**
     *@Description:导出指定月份的员工卡销卡据报表
     *@Author:disvenk.dai
     *@Date:18:45 2018/4/17 0017
     */
    @RequestMapping("exprotMothWorkerExcel")
    public ResponseEntity exprotMothWorkerExcel(@RequestParam("mothDate") String mothDate,
                                                HttpServletResponse response,
                                                HttpServletRequest request){

        List<BrandEntityPinCardDataDto> brandEntityPinCardDataDto = pinCardDataService.selectJsonList2(mothDate );
        List<Object> list = new ArrayList<>();
        if(brandEntityPinCardDataDto!=null && !brandEntityPinCardDataDto.isEmpty()){
            for(BrandEntityPinCardDataDto brandEntityPinCardDataDto1 : brandEntityPinCardDataDto){
                if(brandEntityPinCardDataDto1!=null){}
                list.add(brandEntityPinCardDataDto1);
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("mothDate", mothDate);
        map.put("reportType", "员工卡销卡数据报表");// 表的头，第一行内容
        map.put("num", "16");// 显示的位置
        map.put("reportTitle", "员工卡销卡数据报表");// 表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] header3 = {
                {"公司名称", "20"},
                {"销卡总数", "20"},
                {"退款总额(元)", "20"},
                {"现金退款金额(元)", "25"},
                {"支票退款金额(元)", "25"},
                {"微信退款金额(元)", "25"},
                {"支付宝退款金额(元)", "25"},
                {"天子星退款金额(元)","25"}
        };
        String[] column3 = {"companyName","pinCardCount", "count", "cashMoney", "chequeMoney", "wechatMoney","aliMoney","starMoney"};

        HSSFWorkbook workbook3 = new HSSFWorkbook();

        ExcelUtil<BrandWorkerChargeAllDto> excelUtil3 = new ExcelUtil<>();

        String str3 = "员工卡销卡数据"+mothDate+"月份报表.xls";
        try {
            map.put("reportType", "员工卡销卡数据报表");
            excelUtil3.ExportCardExcel(str3,request,response,workbook3, header3, column3, list, map);

            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(new RestResponseEntity(200,"失败",null,null), HttpStatus.OK);
    }

    /**
     *@Description:导出每个公司指定某个月的销卡明细
     *@Author:disvenk.dai
     *@Date:20:26 2018/4/18 0018
     */
    @RequestMapping("exportRefundCardCompanyAll")
    public ResponseEntity selectOneCompanyAll(@RequestParam("mothDate") String mothDate,
                                              HttpServletResponse response,
                                              HttpServletRequest request){


        String str4 = "各公司员工卡销卡数据"+mothDate+"月份报表.xls";


        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("mothDate", mothDate);
        map.put("num", "16");// 显示的位置
        map.put("timeType", "yyyy-MM-dd");

        String[][] header4 = {
                {"日期", "20"},
                {"销卡时间", "23"},
                {"公司名称", "23"},
                {"姓名", "23"},
                {"手机号码", "23"},
                {"身份证号", "23"},
                {"支票退款金额", "23"},
                {"现金退款金额", "23"},
                {"微信退款金额", "23"},
                {"支付宝退款金额", "23"},
                {"天子星退款总额","23"},
                {"退款总额", "23"}
        };
        String[] column4 = {"date","time", "companyName", "customerName","tel","idCard","chequeMoney","cashMoney","wechatMoney","aliMoney","starMoney","count"};

        JSONObject jsonObject = pinCardDataService.selectRefundCardWorkerAllDto(mothDate);
        List<TbCardCompany> companys = (List<TbCardCompany>) jsonObject.get("companyName");
        List<List<BrandRefundCardWorkerAllDto>> lists = (List<List<BrandRefundCardWorkerAllDto>>) jsonObject.get("companyEntity");
        HSSFWorkbook workbook = new HSSFWorkbook();
        ExcelUtil<BrandRefundCardWorkerAllDto> excelUtil = new ExcelUtil<>();


        try {
            for( int i=0;i<companys.size();i++){
                String companyName = companys.get(i).getCompanyName();
                map.put("companyName",companyName);
                map.put("reportType",companyName+"员工卡销卡明细");// 表的头，第一行内容
                map.put("reportTitle", companyName);// 表的名字
                excelUtil.ExportShopMothExcel(workbook, header4, column4, lists.get(i), map);
            }


            response.setContentType(
                    "application/vnd.ms-excel");

            String agent = request
                    .getHeader("user-agent");//获取所使用的浏览器类型
            str4 = FileUtils.encodeDownloadFilename(str4, agent);//按照指定的浏览器进行编码
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + str4);//设置下载的文件名称

            ServletOutputStream outputStream =response
                    .getOutputStream();//获取输入流
            workbook.write(outputStream);//写入导出

            // 关闭
            outputStream.close();
            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new ResponseEntity(new RestResponseEntity(200,"失败",null,null), HttpStatus.OK);
    }

}
