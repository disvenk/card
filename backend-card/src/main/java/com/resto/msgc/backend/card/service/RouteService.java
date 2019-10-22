package com.resto.msgc.backend.card.service;

import com.resto.msgc.backend.card.entity.TbRoute;
import com.resto.msgc.backend.card.mapper.TbRouteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by disvenk.dai on 2018-08-31 13:58
 */
@Service
public class RouteService {

    @Autowired
    TbRouteMapper tbRouteMapper;

    public List<TbRoute> getNotSuccess(){
       return tbRouteMapper.getNotSuccess();
    }

    public int insert(TbRoute tbRoute){
        return tbRouteMapper.insert(tbRoute);
    }

    public int update(String id){
        return tbRouteMapper.update(id);
    }
}
