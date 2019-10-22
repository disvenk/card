package com.resto.msgc.backend.card.controller;

import com.alibaba.fastjson.JSONObject;
import com.resto.msgc.backend.card.dto.*;
import com.resto.msgc.backend.card.entity.TbCardCompany;
import com.resto.msgc.backend.card.responseEntity.RestResponseEntity;
import com.resto.msgc.backend.card.service.ActivateDataService;
import com.resto.msgc.backend.card.service.RechargeDataService;
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
import javax.xml.ws.soap.Addressing;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by disvenk.dai on 2018-04-14 10:56
 */

@Api
@Controller
@RequestMapping("activateData")
public class ActivateDataController {

    @Resource
    ActivateDataService activateDataService;

    @RequestMapping("activateData")
    public String index() {
        return "activateData/list";
    }

    /**
     *@Description:查询指定起始时间的开卡数据
     *@Author:disvenk.dai
     *@Date:18:45 2018/4/17 0017
     */
    @RequestMapping("selectBrandByDateFromStartAndEnd")
    public ResponseEntity selectBrandByDateFromStartAndEnd(String beginDate, String endDate){
        JSONObject jsonObject = activateDataService.selectBrandRechargeDateByBeginAndEnd(beginDate,endDate);
        return new ResponseEntity(new RestResponseEntity(100,"成功",jsonObject,null), HttpStatus.OK);
    }

    /**
     *@Description:导出指定起始时间的实体开卡数据报表
     *@Author:disvenk.dai
     *@Date:18:45 2018/4/17 0017
     */
    @RequestMapping("exprotEntityExcel")
    public ResponseEntity exprotEntityExcel(@RequestParam("beginDate") String beginDate,
                                            @RequestParam("endDate") String endDate,
                                            HttpServletResponse response,
                                            HttpServletRequest request){
        JSONObject jsonObject = activateDataService.selectJson1(beginDate,endDate);
        List<Object> json1 = new ArrayList<>();
        BrandCardDataDto brandCardDataDto= new BrandCardDataDto();
        brandCardDataDto.setCardCount(jsonObject.get("cardCount").toString());
        brandCardDataDto.setWorkerCardCount(jsonObject.get("workerCardCount").toString());
        brandCardDataDto.setNormalCardCount(jsonObject.get("normalCardCount").toString());
        brandCardDataDto.setTempCardCount(jsonObject.get("tempCardCount").toString());
        brandCardDataDto.setChequeCardMoney(jsonObject.get("chequeCardMoney").toString());
        brandCardDataDto.setCashCardMoney(jsonObject.get("cashCardMoney").toString());
        brandCardDataDto.setWechatCardMoney(jsonObject.get("wechatCardMoney").toString());
        brandCardDataDto.setAliPayCardMoney(jsonObject.get("aliPayCardMoney").toString());
        brandCardDataDto.setStarPayCardMoney(jsonObject.get("starCardMoney").toString());
        json1.add(brandCardDataDto);

        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("beginDate", beginDate);
        map.put("reportType", "实体卡开卡数据报表");// 表的头，第一行内容
        map.put("endDate", endDate);
        map.put("num", "16");// 显示的位置
        map.put("reportTitle", "实体卡开卡数据报表");// 表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] header1 = {{"开卡总数量(张)", "20"},
                {"员工卡开发数量(张)", "20"},
                {"普通卡开卡数量(张)", "25"},
                {"临时卡开卡数量(张)", "25"},
                {"支票开卡金额(元)", "25"},
                {"现金开卡金额(元)", "25"},
                {"微信开卡金额(元)", "25"},
                {"支付宝开卡金额(元)", "25"},
                {"天子星开卡金额(元)", "25"}
        };
        String[] column1 = {"cardCount", "workerCardCount", "normalCardCount","tempCardCount", "chequeCardMoney", "cashCardMoney","wechatCardMoney","aliPayCardMoney","starPayCardMoney"};


        HSSFWorkbook workbook1 = new HSSFWorkbook();

        ExcelUtil<BrandCardDataDto> excelUtil = new ExcelUtil<>();


        String str1 = "实体开卡数据报表" + beginDate + "至" + endDate + ".xls";

        try {
            excelUtil.ExportCardExcel(str1,request,response,workbook1, header1, column1, json1, map);

            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new ResponseEntity(new RestResponseEntity(200,"失败",null,null), HttpStatus.OK);
    }

    /**
     *@Description:导出指定起始时间的普通开卡数据报表
     *@Author:disvenk.dai
     *@Date:18:45 2018/4/17 0017
     */
    @RequestMapping("exprotNormalExcel")
    public ResponseEntity exprotNormalExcel(@RequestParam("beginDate") String beginDate,
                                            @RequestParam("endDate") String endDate,
                                            HttpServletResponse response,
                                            HttpServletRequest request){
        JSONObject jsonObject = activateDataService.selectJson2(beginDate,endDate);

        List<Object> json2 = new ArrayList<>();

        BrandNormalDateDto brandNormalDateDto= new BrandNormalDateDto();
        brandNormalDateDto.setCardCount(jsonObject.get("cardCount").toString());
        brandNormalDateDto.setChequeCardMoney(jsonObject.get("chequeCardMoney").toString());
        brandNormalDateDto.setCashCardMoney(jsonObject.get("cashCardMoney").toString());
        brandNormalDateDto.setWechatCardMoney(jsonObject.get("wechatCardMoney").toString());
        brandNormalDateDto.setAliPayCardMoney(jsonObject.get("aliPayCardMoney").toString());
        brandNormalDateDto.setStarPayCardMoney(jsonObject.get("starCardMoney").toString());
        json2.add(brandNormalDateDto);

        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("beginDate", beginDate);
        map.put("reportType", "实体卡开卡数据报表");// 表的头，第一行内容
        map.put("endDate", endDate);
        map.put("num", "16");// 显示的位置
        map.put("reportTitle", "实体卡开卡数据报表");// 表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] header2 = {{"开卡总数量(张)", "20"},
                {"支票开卡金额(元)", "23"},
                {"现金开卡金额(元)", "23"},
                {"微信开卡金额(元)", "23"},
                {"支付宝开卡金额(元)", "23"},
                {"天子星开卡金额(元)", "23"}
        };
        String[] column2 = {"cardCount", "chequeCardMoney", "cashCardMoney","wechatCardMoney","aliPayCardMoney","starPayCardMoney"};


        HSSFWorkbook workbook2 = new HSSFWorkbook();

        ExcelUtil<BrandNormalDateDto> excelUtil2 = new ExcelUtil<>();

        String str2 = "普通卡开卡数据报表" + beginDate + "至" + endDate + ".xls";

        try {

            map.put("reportType", "普通卡开卡数据报表");
            excelUtil2.ExportCardExcel(str2,request,response,workbook2, header2, column2, json2, map);
            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(new RestResponseEntity(200,"失败",null,null), HttpStatus.OK);
    }

    /**
     *@Description:导出指定起始时间的员工开卡数据报表
     *@Author:disvenk.dai
     *@Date:18:45 2018/4/17 0017
     */
    @RequestMapping("exprotWorkerExcel")
    public ResponseEntity exprotWorkerExcel(@RequestParam("beginDate") String beginDate,
                                            @RequestParam("endDate") String endDate,
                                            HttpServletResponse response,
                                            HttpServletRequest request){

        List<JSONObject> jsonObjects = activateDataService.selectJsonList(beginDate, endDate);

        List<Object> json3 = new ArrayList<>();

        for(JSONObject json : jsonObjects){
            BrandWorkCardDataDto brandWorkCardDataDto = new BrandWorkCardDataDto();
            brandWorkCardDataDto.setCompanyName(json.get("companyName").toString());
            brandWorkCardDataDto.setCardCount(json.get("cardCount").toString());
            brandWorkCardDataDto.setChequeCardMoney(json.get("chequeCardMoney").toString());
            brandWorkCardDataDto.setCashCardMoney(json.get("cashCardMoney").toString());
            brandWorkCardDataDto.setWechatCardMoney(json.get("wechatCardMoney").toString());
            brandWorkCardDataDto.setAliPayCardMoney(json.get("aliPayCardMoney").toString());
            brandWorkCardDataDto.setStarPayCardMoney(json.get("starCardMoney").toString());
            json3.add(brandWorkCardDataDto);
        }

        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("beginDate", beginDate);
        map.put("reportType", "实体卡开卡数据报表");// 表的头，第一行内容
        map.put("endDate", endDate);
        map.put("num", "16");// 显示的位置
        map.put("reportTitle", "实体卡开卡数据报表");// 表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] header3 = {
                {"公司名称", "20"},
                {"开卡总数量(张)", "23"},
                {"支票开卡金额(元)", "23"},
                {"现金开卡金额(元)", "23"},
                {"微信开卡金额(元)", "23"},
                {"支付宝开卡金额(元)", "23"},
                {"天子星开卡金额(元)", "23"}
        };
        String[] column3 = {"companyName","cardCount", "chequeCardMoney", "cashCardMoney","wechatCardMoney","aliPayCardMoney","starPayCardMoney"};

        HSSFWorkbook workbook3 = new HSSFWorkbook();

        ExcelUtil<BrandWorkCardDataDto> excelUtil3 = new ExcelUtil<>();

        String str3 = "员工卡开卡数据报表" + beginDate + "至" + endDate + ".xls";
        try {
            map.put("reportType", "员工卡开卡数据报表");
            excelUtil3.ExportCardExcel(str3,request,response,workbook3, header3, column3, json3, map);

            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(new RestResponseEntity(200,"失败",null,null), HttpStatus.OK);
    }

    /**
     *@Description:导出指定月份实体开卡数据报表
     *@Author:disvenk.dai
     *@Date:18:45 2018/4/17 0017
     */
    @RequestMapping("exprotMothEntityExcel")
    public ResponseEntity exprotMothEntityExcel(@RequestParam("mothDate") String mothDate,
                                                HttpServletResponse response,
                                                HttpServletRequest request){
        JSONObject jsonObject = activateDataService.selectJson11(mothDate);
        List<Object> json1 = new ArrayList<>();
        BrandCardDataDto brandCardDataDto= new BrandCardDataDto();
        brandCardDataDto.setCardCount(jsonObject.get("cardCount").toString());
        brandCardDataDto.setWorkerCardCount(jsonObject.get("workerCardCount").toString());
        brandCardDataDto.setNormalCardCount(jsonObject.get("normalCardCount").toString());
        brandCardDataDto.setTempCardCount(jsonObject.get("tempCardCount").toString());
        brandCardDataDto.setChequeCardMoney(jsonObject.get("chequeCardMoney").toString());
        brandCardDataDto.setCashCardMoney(jsonObject.get("cashCardMoney").toString());
        brandCardDataDto.setWechatCardMoney(jsonObject.get("wechatCardMoney").toString());
        brandCardDataDto.setAliPayCardMoney(jsonObject.get("aliPayCardMoney").toString());
        brandCardDataDto.setStarPayCardMoney(jsonObject.get("starCardMoney").toString());
        json1.add(brandCardDataDto);

        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("mothDate", mothDate);
        map.put("reportType", "实体卡开卡数据报表");// 表的头，第一行内容
        map.put("num", "16");// 显示的位置
        map.put("reportTitle", "实体卡开卡数据报表");// 表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] header1 = {{"开卡总数量(张)", "20"},
                {"员工卡开发数量(张)", "20"},
                {"普通卡开卡数量(张)", "25"},
                {"临时卡开卡数量(张)", "25"},
                {"支票开卡金额(元)", "25"},
                {"现金开卡金额(元)", "25"},
                {"微信开卡金额(元)", "25"},
                {"支付宝开卡金额(元)", "25"},
                {"天子星开卡金额(元)", "25"}
        };
        String[] column1 = {"cardCount", "workerCardCount", "normalCardCount","tempCardCount", "chequeCardMoney", "cashCardMoney","wechatCardMoney","aliPayCardMoney","starPayCardMoney"};


        HSSFWorkbook workbook1 = new HSSFWorkbook();

        ExcelUtil<BrandCardDataDto> excelUtil = new ExcelUtil<>();


        String str1 = "实体开卡数据"+mothDate+"月份报表.xls";

        try {
            excelUtil.ExportCardExcel(str1,request,response,workbook1, header1, column1, json1, map);

            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new ResponseEntity(new RestResponseEntity(200,"失败",null,null), HttpStatus.OK);
    }

    /**
     *@Description:导出指月份的普通开卡数据报表
     *@Author:disvenk.dai
     *@Date:18:45 2018/4/17 0017
     */
    @RequestMapping("exprotMothNormalExcel")
    public ResponseEntity exprotMothNormalExcel(@RequestParam("mothDate") String mothDate,
                                                HttpServletResponse response,
                                                HttpServletRequest request){
        JSONObject jsonObject = activateDataService.selectJson22(mothDate);

        List<Object> json2 = new ArrayList<>();

        BrandNormalDateDto brandNormalDateDto= new BrandNormalDateDto();
        brandNormalDateDto.setCardCount(jsonObject.get("cardCount").toString());
        brandNormalDateDto.setChequeCardMoney(jsonObject.get("chequeCardMoney").toString());
        brandNormalDateDto.setCashCardMoney(jsonObject.get("cashCardMoney").toString());
        brandNormalDateDto.setWechatCardMoney(jsonObject.get("wechatCardMoney").toString());
        brandNormalDateDto.setAliPayCardMoney(jsonObject.get("aliPayCardMoney").toString());
        brandNormalDateDto.setStarPayCardMoney(jsonObject.get("starCardMoney").toString());
        json2.add(brandNormalDateDto);

        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("mothDate", mothDate);
        map.put("reportType", "实体卡开卡数据报表");// 表的头，第一行内容
        map.put("num", "16");// 显示的位置
        map.put("reportTitle", "实体卡开卡数据报表");// 表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] header2 = {{"开卡总数量(张)", "20"},
                {"支票开卡金额(元)", "23"},
                {"现金开卡金额(元)", "23"},
                {"微信开卡金额(元)", "23"},
                {"支付宝开卡金额(元)", "23"},
                {"天子星开卡金额(元)", "23"}
        };
        String[] column2 = {"cardCount", "chequeCardMoney", "cashCardMoney","wechatCardMoney","aliPayCardMoney","starPayCardMoney"};


        HSSFWorkbook workbook2 = new HSSFWorkbook();

        ExcelUtil<BrandNormalDateDto> excelUtil2 = new ExcelUtil<>();

        String str2 = "普通卡开卡数据"+mothDate+"月份报表.xls";

        try {

            map.put("reportType", "普通卡开卡数据报表");
            excelUtil2.ExportCardExcel(str2,request,response,workbook2, header2, column2, json2, map);
            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(new RestResponseEntity(200,"失败",null,null), HttpStatus.OK);
    }

    /**
     *@Description:导出指定月份的员工开卡数据报表
     *@Author:disvenk.dai
     *@Date:18:45 2018/4/17 0017
     */
    @RequestMapping("exprotMothWorkerExcel")
    public ResponseEntity exprotMothWorkerExcel(@RequestParam("mothDate") String mothDate,
                                                HttpServletResponse response,
                                                HttpServletRequest request){

        List<JSONObject> jsonObjects = activateDataService.selectJsonList2(mothDate);

        List<Object> json3 = new ArrayList<>();

        for(JSONObject json : jsonObjects){
            BrandWorkCardDataDto brandWorkCardDataDto = new BrandWorkCardDataDto();
            brandWorkCardDataDto.setCompanyName(json.get("companyName").toString());
            brandWorkCardDataDto.setCardCount(json.get("cardCount").toString());
            brandWorkCardDataDto.setChequeCardMoney(json.get("chequeCardMoney").toString());
            brandWorkCardDataDto.setCashCardMoney(json.get("cashCardMoney").toString());
            brandWorkCardDataDto.setWechatCardMoney(json.get("wechatCardMoney").toString());
            brandWorkCardDataDto.setAliPayCardMoney(json.get("aliPayCardMoney").toString());
            brandWorkCardDataDto.setStarPayCardMoney(json.get("starCardMoney").toString());
            json3.add(brandWorkCardDataDto);
        }

        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("mothDate", mothDate);
        map.put("reportType", "实体卡开卡数据报表");// 表的头，第一行内容
        map.put("num", "16");// 显示的位置
        map.put("reportTitle", "实体卡开卡数据报表");// 表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] header3 = {
                {"公司名称", "20"},
                {"开卡总数量(张)", "23"},
                {"支票开卡金额(元)", "23"},
                {"现金开卡金额(元)", "23"},
                {"微信开卡金额(元)", "23"},
                {"支付宝开卡金额(元)", "23"},
                {"天子星开卡金额(元)", "23"}
        };
        String[] column3 = {"companyName","cardCount", "chequeCardMoney", "cashCardMoney","wechatCardMoney","aliPayCardMoney","starPayCardMoney"};

        HSSFWorkbook workbook3 = new HSSFWorkbook();

        ExcelUtil<BrandWorkCardDataDto> excelUtil3 = new ExcelUtil<>();

        String str3 = "员工卡开卡数据"+mothDate+"月份报表.xls";
        try {
            map.put("reportType", "员工卡开卡数据报表");
            excelUtil3.ExportCardExcel(str3,request,response,workbook3, header3, column3, json3, map);

            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(new RestResponseEntity(200,"失败",null,null), HttpStatus.OK);
    }

    /**
     *@Description:导出指定月份的普通开卡数据明细报表
     *@Author:disvenk.dai
     *@Date:18:45 2018/4/17 0017
     */
    @RequestMapping("exprotMothNormalAllExcel")
    public ResponseEntity exprotMothNormalAllExcel(@RequestParam("mothDate") String mothDate,
                                                   HttpServletResponse response,
                                                   HttpServletRequest request){

        List<BrandOneCompanyAllDto> list = activateDataService.selectJsonList3(mothDate);
        List<Object> json3 = new ArrayList<>();

        if(list!=null && !list.isEmpty()){
            for(BrandOneCompanyAllDto brandEntityPinCardDataDto : list){
                if(brandEntityPinCardDataDto!=null){}
                json3.add(brandEntityPinCardDataDto);
            }
        }

        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("mothDate", mothDate);
        map.put("reportType", "实体卡开卡数据报表");// 表的头，第一行内容
        map.put("num", "16");// 显示的位置
        map.put("reportTitle", "实体卡开卡数据报表");// 表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] header3 = {
                {"日期", "20"},
                {"开卡时间", "23"},
                {"姓名", "23"},
                {"手机号码", "23"},
                {"身份证号", "23"},
                {"支付方式", "23"},
                {"开卡金额", "23"}
        };
        String[] column3 = {"date","time", "customerName","tel","idCard","cardPayType","cardPayMoney"};

        HSSFWorkbook workbook3 = new HSSFWorkbook();

        ExcelUtil<BrandOneCompanyAllDto> excelUtil3 = new ExcelUtil<>();

        String str3 = "普通卡开卡数据"+mothDate+"月份报表.xls";
        try {
            map.put("reportType", "普通卡开卡数据报表");
            excelUtil3.ExportCardExcel(str3,request,response,workbook3, header3, column3, json3, map);

            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(new RestResponseEntity(200,"失败",null,null), HttpStatus.OK);
    }

    /**
     *@Description:导出每个公司指定某个月的员工卡开卡明细
     *@Author:disvenk.dai
     *@Date:14:43 2018/4/18 0018
     */
    @RequestMapping("selectOneCompanyAll")
    public ResponseEntity selectOneCompanyAll(@RequestParam("mothDate") String mothDate,
                                              HttpServletResponse response,
                                              HttpServletRequest request){


        String str4 = "各公司员工卡开卡数据"+mothDate+"月份报表.xls";


        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("mothDate", mothDate);
        map.put("num", "16");// 显示的位置
        map.put("timeType", "yyyy-MM-dd");

        String[][] header4 = {
                {"日期", "20"},
                {"开卡时间", "23"},
                {"公司名称", "23"},
                {"姓名", "23"},
                {"手机号码", "23"},
                {"身份证号", "23"},
                {"支付方式", "23"},
                {"开卡金额", "23"}
        };
        String[] column4 = {"date","time", "companyName", "customerName","tel","idCard","cardPayType","cardPayMoney"};

        JSONObject jsonObject = activateDataService.selectOneCompanyAll(mothDate);
        List<TbCardCompany> companys = (List<TbCardCompany>) jsonObject.get("companyName");
        List<List<BrandOneCompanyAllDto>> lists = (List<List<BrandOneCompanyAllDto>>) jsonObject.get("companyEntity");
        HSSFWorkbook workbook = new HSSFWorkbook();
        ExcelUtil<BrandOneCompanyAllDto> excelUtil = new ExcelUtil<>();


        try {
            for(int i=0;i<companys.size();i++){

                String companyName = companys.get(i).getCompanyName();
                map.put("companyName",companyName);
                map.put("reportType",companyName+"员工开卡明细");// 表的头，第一行内容
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
