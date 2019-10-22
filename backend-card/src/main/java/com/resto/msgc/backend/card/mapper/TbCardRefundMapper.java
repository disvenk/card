package com.resto.msgc.backend.card.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.msgc.backend.card.dto.BrandEntityPinCardDataDto;
import com.resto.msgc.backend.card.dto.BrandRefundCardWorkerAllDto;
import com.resto.msgc.backend.card.entity.TbCardRefund;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbCardRefundMapper extends MyMapper<TbCardRefund> {

    BrandEntityPinCardDataDto selectEntityCardRefundByStartAndEnd(@Param("beginDate")String beginDate, @Param("endDate")String endDate);
    BrandEntityPinCardDataDto selectNormalCardRefundByStartAndEnd(@Param("beginDate")String beginDate, @Param("endDate")String endDate);
    BrandEntityPinCardDataDto selectTemporaryCardRefundByStartAndEnd(@Param("beginDate")String beginDate, @Param("endDate")String endDate);
    List<BrandEntityPinCardDataDto> selectWorkerCardRefundByStartAndEnd(@Param("beginDate")String beginDate, @Param("endDate")String endDate);

    //moth
    BrandEntityPinCardDataDto selectEntityCardRefundByMoth(@Param("mothDate")String mothDate);
    BrandEntityPinCardDataDto selectNormalCardRefundByMoth(@Param("mothDate")String mothDate);
    List<BrandEntityPinCardDataDto> selectWorkerCardRefundByMoth(@Param("mothDate")String mothDate);
    List<BrandRefundCardWorkerAllDto> selectRefundCardWorkerAllDto(@Param("companyId")Long companyId, @Param("mothDate")String mothDate);

    List<BrandRefundCardWorkerAllDto> selectRefundCardNormalAllDto(@Param("mothDate")String mothDate);
}