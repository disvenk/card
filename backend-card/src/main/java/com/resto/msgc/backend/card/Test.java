package com.resto.msgc.backend.card;

import com.resto.msgc.backend.card.dto.ExcelCardDto;
import com.resto.msgc.backend.card.service.CardCustomService;
import com.resto.msgc.backend.card.util.ExcelUtilImport;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.File;
import java.util.List;

/**
 * Created by xielc on 2018/5/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest

public class Test {
    @Autowired
    private CardCustomService cardCustomService;

    @org.junit.Test
    public void excelOpenCard(){
        List<ExcelCardDto> list= ExcelUtilImport.getDataFromExcel("D:"+ File.separator +"01.xlsx");
        //System.out.println(list);
        cardCustomService.excelOpenCard(list,"三哥");

    }
}
