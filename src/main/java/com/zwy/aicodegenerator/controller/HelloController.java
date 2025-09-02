package com.zwy.aicodegenerator.controller;

import com.zwy.aicodegenerator.common.BaseResponse;
import com.zwy.aicodegenerator.utils.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/allive")
public class HelloController {

    @GetMapping("/hello")
    public BaseResponse<String> hello() {
        return ResultUtils.success( "yesssssssssss");
    }
}
