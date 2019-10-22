package com.resto.msgc.backend.card.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.msgc.backend.card.dto.BrandEntityCardChargeDto;
import com.resto.msgc.backend.card.dto.BrandOncallBusinessDto;
import com.resto.msgc.backend.card.entity.TbCardShift;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbCardShiftMapper extends MyMapper<TbCardShift> {

    BrandEntityCardChargeDto selectBusinessCountByDate(@Param("date")String date);

    //BrandEntityCardChargeDto selectBusinessCountByDate2(@Param("date")String date);

    List<BrandOncallBusinessDto> selectOncallBusinessCountByDate(@Param("date")String date);

    BrandEntityCardChargeDto selectStartAndEndCharge(@Param("startDate")String startDate,@Param("endDate")String endDate);
}