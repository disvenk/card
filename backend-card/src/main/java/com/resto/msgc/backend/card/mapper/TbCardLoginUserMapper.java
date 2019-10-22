package com.resto.msgc.backend.card.mapper;

import com.resto.conf.mybatis.util.MyMapper;
import com.resto.msgc.backend.card.entity.TbCardLoginUser;

import java.util.List;

public interface TbCardLoginUserMapper extends MyMapper<TbCardLoginUser> {

    TbCardLoginUser findUserByTelephone(String telephone);

    List<TbCardLoginUser> findUserByBrandId(String brandId);

    TbCardLoginUser selectUserByFlag();

}