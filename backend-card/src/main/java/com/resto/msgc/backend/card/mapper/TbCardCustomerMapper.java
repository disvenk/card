package com.resto.msgc.backend.card.mapper;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.resto.conf.mybatis.util.MyMapper;
import com.resto.msgc.backend.card.dto.ActivatedDto;
import com.resto.msgc.backend.card.dto.BrandOneCompanyAllDto;
import com.resto.msgc.backend.card.dto.CardDto;
import com.resto.msgc.backend.card.dto.StartAndEndDto;
import com.resto.msgc.backend.card.entity.TbCardCompany;
import com.resto.msgc.backend.card.entity.TbCardCustomer;
import org.apache.ibatis.annotations.Param;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

public interface TbCardCustomerMapper extends MyMapper<TbCardCustomer> {

    List<TbCardCustomer> selectByPageNumSize(@Param("tbCardCustomer") TbCardCustomer tbCardCustomer,
                                             @Param("pageNum") int pageNum,
                                             @Param("pageSize") int pageSize);

    int insert(TbCardCustomer tbCardCustomer);

    List<TbCardCustomer> findByCardId(String cardId);

    CardDto findCardInfoByCardId(String cardId);

    int searchSelectCount(@Param("tbCardCustomer") TbCardCustomer tbCardCustomer);

    int updateByCardPP(TbCardCustomer tbCardCustomer);

    JSONObject selectBrandRechargeDateByBeginAndEnd(@Param("beginDate")String beginDate,@Param("endDate")String endDate);

    JSONObject selectNormalRechargeByBeginAndEnd(@Param("beginDate")String beginDate,@Param("endDate")String endDate);

    StartAndEndDto selectTemporaryCardByStartAndEnd(@Param("beginDate")String beginDate, @Param("endDate")String endDate);

    //查询所有公司开卡数量
    List<JSONObject> selectCompanyCardCount(@Param("beginDate")String beginDate,@Param("endDate")String endDate);

    Long selectCompanyCardMoney(@Param("beginDate")String beginDate,
                                @Param("endDate")String endDate,
                                @Param("id")int id,
                                @Param("payType")int payType);


    JSONObject selectBrandRechargeDateByMoth(@Param("mothDate")String mothDate);

    JSONObject selectNormalRechargeByMoth(@Param("mothDate")String mothDate);

    List<JSONObject> selectCompanyCardCountMoth(@Param("mothDate")String mothDate);

    Long selectCompanyCardMoneyMoth(@Param("mothDate")String mothDate,
                                @Param("id")int id,
                                @Param("payType")int payType);


    List<TbCardCompany> selectAllCompany();
    List<BrandOneCompanyAllDto> selectOneCompanyAll(@Param("mothDate")String mothDate,@Param("companyId")Long companyId);

    List<BrandOneCompanyAllDto> selectOneCompanyNormalAll(@Param("mothDate")String mothDate );

    //通过卡号查找
    TbCardCustomer selectByCardId(@Param("cardId")String cardId);
    //通过账户id查找
    TbCardCustomer selectByAccountId(@Param("accountId")String accountId);

    ActivatedDto activatedCardInfo(@Param("cardId")String cardId);

    int updateById();
}