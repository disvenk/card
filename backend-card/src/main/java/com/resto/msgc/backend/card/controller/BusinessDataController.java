package com.resto.msgc.backend.card.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.resto.msgc.backend.card.dto.BrandBusinessDataDto;
import com.resto.msgc.backend.card.dto.BusinessSheet1;
import com.resto.msgc.backend.card.dto.ShopBusinessDataDto;
import com.resto.msgc.backend.card.responseEntity.RestResponseEntity;
import com.resto.msgc.backend.card.service.BusunessDataService;
import com.resto.msgc.backend.card.util.ExcelUtil;
import com.resto.msgc.backend.card.util.FileUtils;
import io.swagger.annotations.Api;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by disvenk.dai on 2018-04-14 10:46
 */

@Api
@Controller
@RequestMapping("businessData")
public class BusinessDataController {

    @Resource
    private BusunessDataService busunessDataService;

    /**
    *@Description:页面请求转发
    *@Author:disvenk.dai
    *@Date:11:31 2018/4/16 0016
    */
    @RequestMapping("businessData")
    public String index() {
        return "businessData/list";
    }

    /**
    *@Description:查询指定起始时间Foodmember营业报表
    *@Author:disvenk.dai
    *@Date:11:31 2018/4/16 0016
    */
    @RequestMapping(value = "selectBrandByDateFromStartAndEnd")
    public ResponseEntity selectBrandByDateFromStartAndEnd(String beginDate,String endDate){
        JSONObject jsonObject = busunessDataService.selectBrandByDateFromStartAndEnd(beginDate,endDate);
        return new ResponseEntity(new RestResponseEntity(100,"成功",jsonObject,null), HttpStatus.OK);
    }

    /**
    *@Description:导出指定起始时间Foodmember营业报表
    *@Author:disvenk.dai
    *@Date:15:36 2018/4/16 0016
    */
    @RequestMapping("exprotBrandExcel")
    public ResponseEntity exprotBrandExcel(@RequestParam("beginDate") String beginDate,@RequestParam("endDate") String endDate,HttpServletResponse response, HttpServletRequest request) {
        // 导出文件名
        String str = "营业总额报表" + beginDate + "至" + endDate + ".xls";
        //String path = request.getSession().getServletContext().getRealPath(str);
        String shopName = "";
        List<String> names = busunessDataService.selectShopName();
        JSONObject jsonObject = busunessDataService.selectBrandByDateFromStartAndEnd(beginDate, endDate);
        List<JSONObject> shopObject = (List<JSONObject>) jsonObject.get("shop");
        List<BrandBusinessDataDto> brandReportEntities = new ArrayList<>();
        for(JSONObject json : shopObject){
            BrandBusinessDataDto brandReportEntity = new BrandBusinessDataDto();
            brandReportEntity.setShopName(json.get("shopName")==null?"":json.get("shopName").toString());
            brandReportEntity.setOrderCount(json.get("orderCount").toString());
            brandReportEntity.setOriginalAmount(json.get("originalAmount").toString());
            brandReportEntity.setPaymentAmount(json.get("paymentAmount").toString());
            brandReportEntity.setReductionAmount(json.get("reductionAmount").toString());
            brandReportEntity.setRefundMoney(new BigDecimal(json.get("refundMoney").toString()));
            brandReportEntities.add(brandReportEntity);
        }
        for (String name : names) {
            shopName += name + ",";
        }
        // 去掉最后一个逗号
        if(shopName!=null && !shopName.equals("") ){
            shopName = shopName.substring(0, shopName.length() - 1);
        }

        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("shops", shopName);
        map.put("beginDate", beginDate);
        map.put("reportType", "品牌营业额报表");// 表的头，第一行内容
        map.put("endDate", endDate);
        map.put("num", "16");// 显示的位置
        map.put("reportTitle", "品牌收入条目");// 表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] headers = {{"档口名称", "20"}, {"订单数(单)", "20"}, {"订单总额(元)", "16"}, {"实收金额(元)", "16"}, {"折扣金额(元)", "19"},{"退款金额(元)","19"}};
        String[] columns = {"shopName", "orderCount", "originalAmount", "paymentAmount", "reductionAmount","refundMoney"};


        ExcelUtil<BrandBusinessDataDto> excelUtil = new ExcelUtil<>();

        try {
            excelUtil.ExportExcel(str,request,response,headers, columns,brandReportEntities , map);

            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new ResponseEntity(new RestResponseEntity(200,"失败",null,null), HttpStatus.OK);
    }

    /**
    *@Description:导出某个月Foodmember营业报表
    *@Author:disvenk.dai
    *@Date:17:24 2018/4/16 0016
    */
    @RequestMapping("exprotBrandMothExcel")
    public ResponseEntity exprotBrandMothExcel(HttpServletResponse response,@RequestParam("mothDate") String mothDate, HttpServletRequest request){
// 导出文件名
        String str = "营业总额报表" + mothDate+ ".xls";
        //String path = request.getSession().getServletContext().getRealPath(str);
        String shopName = "";
        List<String> names = busunessDataService.selectShopName();
        JSONObject jsonObject = busunessDataService.selectShopByDateFromSomeMoth(mothDate);
        List<JSONObject> jsonObjects = (List<JSONObject>) jsonObject.get("shop");
        List<BrandBusinessDataDto> brandReportEntities = new ArrayList<>();
        for(JSONObject json : jsonObjects){
            BrandBusinessDataDto brandReportEntity = new BrandBusinessDataDto();
            brandReportEntity.setShopName(json.get("shopName")==null?"":json.get("shopName").toString());
            brandReportEntity.setOrderCount(json.get("orderCount").toString());
            brandReportEntity.setOriginalAmount(json.get("originalAmount").toString());
            brandReportEntity.setPaymentAmount(json.get("paymentAmount").toString());
            brandReportEntity.setReductionAmount(json.get("reductionAmount").toString());
            brandReportEntity.setRefundMoney(new BigDecimal(json.get("refundMoney").toString()));
            brandReportEntities.add(brandReportEntity);
        }

        for (String name : names) {
            shopName += name + ",";
        }

        if(shopName!=null && !shopName.equals("") ){
            shopName = shopName.substring(0, shopName.length() - 1);
        }
        // 去掉最后一个逗号

        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("shops", shopName);
        map.put("mothDate", mothDate);
        map.put("reportType", "品牌营业总额月报表");// 表的头，第一行内容
        map.put("num", "16");// 显示的位置
        map.put("reportTitle", "品牌收入条目");// 表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] headers = {{"档口名称", "20"}, {"订单数(单)", "20"}, {"订单总额(元)", "16"}, {"实收金额(元)", "16"}, {"折扣金额(元)", "19"},{"退款金额(元)", "19"}};
        String[] columns = {"shopName", "orderCount", "originalAmount", "paymentAmount", "reductionAmount","refundMoney"};


        ExcelUtil<BrandBusinessDataDto> excelUtil = new ExcelUtil<>();

        try {
            excelUtil.ExportExcel(str,request,response,headers, columns,brandReportEntities , map);

            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new ResponseEntity(new RestResponseEntity(200,"失败",null,null), HttpStatus.OK);
    }

    /**
     *@Description:多个sheet
     *@Author:disvenk.dai
     *@Date:17:24 2018/4/16 0016
     */
    @RequestMapping("exprotShopMothExcel")
    public ResponseEntity exprotShopMothExcel(HttpServletResponse response,@RequestParam("mothDate") String mothDate, HttpServletRequest request){
// 导出文件名
        String str = "各档口各月营业总额报表" + mothDate+ ".xls";
        //String path = request.getSession().getServletContext().getRealPath(str);

        List<List<BusinessSheet1>> lists = busunessDataService.selectShopBySomeMoth(mothDate);

        Map<String, String> map = new HashMap<>();
        map.put("brandName", "Foodmember");
        map.put("shops", "");
        map.put("mothDate", mothDate);
        map.put("reportType", "店铺营业总额月报表");// 表的头，第一行内容
        map.put("num", "16");// 显示的位置
        map.put("reportTitle", "品牌收入条目");// 表的名字
        map.put("timeType", "yyyy-MM-dd");

        String[][] headers = {{"日期", "20"}, {"订单数(单)", "20"}, {"订单总额(元)", "16"}, {"实收金额(元)", "16"}, {"折扣金额(元)", "19"},{"退款金额(元)", "19"}};
        String[] columns = {"date", "orderCount", "originalAmount", "paymentAmount", "reductionAmount","refundMoney"};

        HSSFWorkbook workbook = new HSSFWorkbook();
        ExcelUtil<BusinessSheet1> excelUtil = new ExcelUtil<>();

        try {
            for(List<BusinessSheet1> list : lists){
                if(list!=null && !list.isEmpty()) {
                    map.put("shops", list.get(0).getShopName());
                    map.put("reportTitle", list.get(0).getShopName());
                    excelUtil.ExportShopMothExcel(workbook, headers, columns, list, map);
                }
            }

            response.setContentType(
                    "application/vnd.ms-excel");

            String agent = request
                    .getHeader("user-agent");//获取所使用的浏览器类型
            str = FileUtils.encodeDownloadFilename(str, agent);//按照指定的浏览器进行编码
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + str);//设置下载的文件名称

            ServletOutputStream outputStream =response
                    .getOutputStream();//获取输入流
            workbook.write(outputStream);//写入导出

            // 关闭
            outputStream.close();
            return new ResponseEntity(new RestResponseEntity(100,"成功",null,null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new ResponseEntity(new RestResponseEntity(500,"失败",null,null), HttpStatus.OK);
    }
}
