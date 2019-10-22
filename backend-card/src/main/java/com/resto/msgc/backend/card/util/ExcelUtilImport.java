package com.resto.msgc.backend.card.util;

import com.resto.msgc.backend.card.dto.ExcelCardDto;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xielc on 2018/5/8.
 */
public class ExcelUtilImport {

    /**
     * 读取出filePath中的所有数据信息
     * @param filePath excel文件的绝对路径
     *
     */
    public static List<ExcelCardDto> getDataFromExcel(String filePath)
    {
        //String filePath = "E:\\123.xlsx";

        //判断是否为excel类型文件
        if(!filePath.endsWith(".xls")&&!filePath.endsWith(".xlsx"))
        {
            System.out.println("文件不是excel类型");
        }

        FileInputStream fis =null;
        Workbook wookbook = null;

        try
        {
            //获取一个绝对地址的流
            fis = new FileInputStream(filePath);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            //2007版本的excel，用.xlsx结尾
            wookbook = new XSSFWorkbook(fis);//得到工作簿
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //得到一个工作表
        Sheet sheet = wookbook.getSheetAt(0);
        List<ExcelCardDto> list=new ArrayList<>();
        for(Row row:sheet){
            if(row.getRowNum()==0){
              continue;
            }
            //System.out.println("--------------->"+row.getCell((short)0).getStringCellValue()+row.getCell((short)1).getStringCellValue()+row.getCell((short)2).getStringCellValue()+row.getCell((short)2).getStringCellValue());
            ExcelCardDto excelCardDto=new ExcelCardDto();
            excelCardDto.setCardId(row.getCell((short)0)==null?null:row.getCell((short)0).getStringCellValue());
            excelCardDto.setCustomerName(row.getCell((short)1)==null?null:row.getCell((short)1).getStringCellValue());
            excelCardDto.setTelephone(row.getCell((short)2)==null?null:row.getCell((short)2).getStringCellValue());
            excelCardDto.setCardType(row.getCell((short)3)==null?null:row.getCell((short)3).getStringCellValue());
            excelCardDto.setDiscount(row.getCell((short)4)==null?null:row.getCell((short)4).getStringCellValue());
            excelCardDto.setCompanyId(row.getCell((short)5)==null?null:row.getCell((short)5).getStringCellValue());
            excelCardDto.setChargeMoney(row.getCell((short)6)==null?null:row.getCell((short)6).getStringCellValue());
            excelCardDto.setRewardMoney(row.getCell((short)7)==null?null:row.getCell((short)7).getStringCellValue());
            list.add(excelCardDto);
        }
        return list;
    }
}

