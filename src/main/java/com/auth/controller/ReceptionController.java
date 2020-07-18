package com.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.vo.ErrorCodes;
import com.auth.vo.ResultResponse;

@RestController
@RequestMapping("/reception")
public class ReceptionController {


    @GetMapping("/bring1won")
    public ResultResponse bring1won() throws Exception{
    	return ResultResponse.builder().errCode(ErrorCodes.S0).errMsg("").data("").build();
    }
    
    @GetMapping("/give1won")
    public ResultResponse give1won() throws Exception{
    	return ResultResponse.builder().errCode(ErrorCodes.S0).errMsg("").data("").build();
    }


}
