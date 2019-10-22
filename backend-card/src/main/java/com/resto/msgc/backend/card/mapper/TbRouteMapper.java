package com.resto.msgc.backend.card.mapper;

import com.resto.msgc.backend.card.entity.TbRoute;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by disvenk.dai on 2018-08-31 13:47
 */
public interface TbRouteMapper {

     List<TbRoute> getNotSuccess();

    int insert(TbRoute tbRoute);

    int update(@Param("id")String id);
}
