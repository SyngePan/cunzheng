package com.cunzheng.controller;

import com.cunzheng.servie.UserService;
import com.cunzheng.contract.BlockUtil;
import com.cunzheng.contract.CunZhengContract;
import com.cunzheng.entity.UserRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/user")
@Slf4j
@Api(value = "用户管理", description = "电子存证用户管理模块功能")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CunZhengContract cunZhengContract;

    @PostMapping("/saveUser")
    @ApiOperation(value = "用户注册", notes = "用户注册")
    public String saveUser(
            @ApiParam("用户名")  @RequestParam String username,
            @ApiParam("密码") @RequestParam String password,
            @ApiParam("用户角色") @RequestParam UserRole userRole
    ) throws Exception {

        boolean userNameExists = userService.hasUserNameCreated(username);
        if (userNameExists) {
            return "Username already exists";
        }

        String accountJson = BlockUtil.newAccountSM2(password);

        cunZhengContract.saveUser(JSONObject.fromObject(accountJson).getString("address"), username, userRole.getCode());
        userService.saveUser(username, password, userRole, accountJson);

        return "success";
    }
}
