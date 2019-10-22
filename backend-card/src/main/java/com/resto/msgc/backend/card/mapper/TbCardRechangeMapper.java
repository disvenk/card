package com.resto.msgc.backend.card.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.msgc.backend.card.dto.*;
import com.resto.msgc.backend.card.entity.TbCardRechange;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TbCardRechangeMapper extends MyMapper<TbCardRechange> {
    public List<TbCardRechange> selectByPageNumSize(@Param("cardRechange") TbCardRechange cardRechange,
                                             @Param("pageNum") int pageNum,
                                             @Param("pageSize") int pageSize);

    //start end
    BrandEntityCardChargeDto selectEntityChargeByStartAndEnd(@Param("beginDate")String beginDate,@Param("endDate")String endDate);
    BrandNormalChargeDto selectNormalChargeByStartAndEnd(@Param("beginDate")String beginDate,@Param("endDate")String endDate);
    BrandNormalChargeDto selectTemporaryChargeByStartAndEnd(@Param("beginDate")String beginDate,@Param("endDate")String endDate);
    List<BrandWorkerChargeAllDto> selelctWorkerChargeAll(@Param("beginDate")String beginDate,@Param("endDate")String endDate);

   //moth
   BrandEntityCardChargeDto selectEntityChargeByMoth(@Param("mothDate")String mothDate);
    BrandNormalChargeDto selectNormalChargeByMoth(@Param("mothDate")String mothDate);
    List<BrandWorkerChargeAllDto> selelctWorkerChargeAllByMoth(@Param("mothDate")String mothDate);

    List<BrandChargeCardCompanyAllDto> selectChargeCardCompanyAll(@Param("mothDate")String mothDate,@Param("companyId")Long companyId);

    List<PinCardMoneyDto> pinCardMoney(@Param("cardId") String cardId);

    List<BrandChargeCardCompanyAllDto> selectChargeCardNormalAll(@Param("mothDate")String mothDate);


    List<TbCardRechange> selectEnableChargeRecord(@Param("customerId")Long customerId);
    int updateRewardBalance(@Param("id")Long id, @Param("reWardBalance")BigDecimal reWardBalance);
    int updateChargeBalance(@Param("id")Long id, @Param("chargeBalance")BigDecimal chargeBalance);


    int updateChargeBalanceForRefund1(@Param("refundMoney")BigDecimal refundMoney,@Param("id")Long id);
    int updateChargeBalanceForRefund2(@Param("refundMoney")BigDecimal refundMoney,@Param("id")Long id);
}