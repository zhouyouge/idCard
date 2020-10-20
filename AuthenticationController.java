package com.yoyo.authentication.controller;

import cn.hutool.core.util.IdcardUtil;
import com.yoyo.authentication.service.TbUserAuthinfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Api(tags = "身份认证相关接口")
@RestController
public class AuthenticationController {
    @Autowired
   TbUserAuthinfoService tbUserAuthinfoService;
    @ApiOperation("验证身份信息")
    @PostMapping(value = "verifyIdentityInfo")
    public Object verifyIdentityInfo(  @RequestParam @ApiParam(name = "idCard", value = "身份证号码", required = true) String idCard,
                                       @RequestParam @ApiParam(name = "realName", value = "真实姓名", required = true) String realName) throws Exception {
        if (!IdcardUtil.isValidCard18(idCard)) {
            throw new Exception("身份证格式不正确");
        }
        return tbUserAuthinfoService.verifyIdentityInfo(idCard, realName);
    }

}
