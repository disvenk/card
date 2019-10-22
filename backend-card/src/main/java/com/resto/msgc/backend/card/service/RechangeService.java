/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.resto.msgc.backend.card.service;

import com.resto.conf.mybatis.base.BaseService;
import com.resto.msgc.backend.card.entity.TbCardRechange;
import com.resto.msgc.backend.card.mapper.TbCardRechangeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuzh
 * @since 2015-12-19 11:09
 */
@Service
public class RechangeService extends BaseService<TbCardRechange, TbCardRechangeMapper> {

    public List<TbCardRechange> getAll(Integer pageNum, Integer pageSize) {
        TbCardRechange cardRechange = new TbCardRechange();
//        country.setDynamicTableName("country_copy");
//        return this.dbSelectPage(country, pageNum, pageSize);
        return this.mapper.selectByPageNumSize(cardRechange, pageNum, pageSize);
    }

    public TbCardRechange getById(Integer id) {
        return this.dbSelectByPrimaryKey(id);
    }

    public void deleteById(Integer id) {
        this.dbDeleteByPrimaryKey(id);
    }

    public void save(TbCardRechange country) {
        if (country.getId() != null) {
            this.dbUpdate(country);
        } else {
            this.dbSave(country);
        }
    }


}