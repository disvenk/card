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

package com.resto.msgc.backend.card.controller;

import com.github.pagehelper.PageInfo;
import com.resto.msgc.backend.card.entity.TbCardRechange;
import com.resto.msgc.backend.card.service.RechangeService;
import com.resto.msgc.backend.card.util.Constant;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author liuzh
 * @since 2015-12-19 11:10
 */
@Api
@Controller
@RequestMapping(Constant.API+"rechange")
public class RechangeController {

    @Autowired
    private RechangeService rechangeService;

    @GetMapping
    @ResponseBody
    public Object getAll(@RequestParam(value = "pageNum", defaultValue = "1")
                                 Integer pageNum,
                         @RequestParam(value = "pageSize", defaultValue = "10")
                                 Integer pageSize) {
        List<TbCardRechange> countryList = rechangeService.getAll(pageNum, pageSize);
        PageInfo pageInfo = new PageInfo<TbCardRechange>(countryList);
        return pageInfo;
    }

    @RequestMapping("/helloJsp")
    public String helloJsp(Map<String,Object> map){
        System.out.println("HelloController.helloJsp().hello=");
        map.put("hello", "test");
        return"helloJsp";
    }
}
