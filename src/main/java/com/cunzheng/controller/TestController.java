package com.cunzheng_01.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangrui on 2019/6/5.
 * com.blockchain.controller
 */
@RestController
@RequestMapping("v1/test")
@Slf4j
@Api(value = "测试接口", description = "测试系统模块功能")
public class TestController {

    @GetMapping("/hello world")
    @ApiOperation(value = "hello word", notes = "hello word")
    public String sendErrorEmail() {
        log.error("Hello world ");
        return "hello world.";
    }
}
