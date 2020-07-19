package com.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.vo.ErrorCodes;
import com.auth.vo.ResultResponse;

@RestController
@RequestMapping("/another-bank")
public class ExternalApiController {

	@PostMapping("/info")
	public ResultResponse retrieveBankAccountInfo() throws Exception {
		return ResultResponse.builder().errCode(ErrorCodes.S0).errMsg("").build();
	}

	@PostMapping("/transfer")
	public ResultResponse transferAnotherBank() throws Exception {
		return ResultResponse.builder().errCode(ErrorCodes.S0).errMsg("").build();
	}

}
