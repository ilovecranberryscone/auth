package com.auth.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.exception.BizException;
import com.auth.service.TransferService;
import com.auth.util.WordUtil;
import com.auth.vo.ErrorCodes;
import com.auth.vo.ResultResponse;
import com.auth.vo.Transfer;
import com.auth.vo.TransferRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth/transfer")
public class TransferController {
	@Resource
	private TransferService transferService;

	@PostMapping
	public ResultResponse transferAuth(@RequestBody TransferRequest transferRequest) throws Exception {

		ResultResponse response = ResultResponse.builder().build();
		Transfer transfer = Transfer.builder()
				.authenticationRequestSerialNum(transferRequest.getAuthenticationRequestSerialNum()).build();

		int transferAuthenticationSerialNum = transferService
				.insertTransferAuthentication(transferRequest.getAuthenticationRequestSerialNum());
		transfer.setTransferAuthenticationSerialNum(transferAuthenticationSerialNum);

		// WordUtil로 4글자 단어 생성
		String requestWord = WordUtil.make4characters();
		transferRequest.setRequestWord(requestWord);
		try {
			transferService.doTransferAuthentication(transfer, transferRequest);
		} catch(BizException e) {
			int resultNum = transferService.updateSuccessYn(transfer, Transfer.ynCode.N);
			log.info("transferService.updateSuccessYn result num => {}", resultNum);
			log.error("transferService.doTransferAuthentication => {}", e.getMessage());
			throw e;
		}
		int resultNum = 0;
		if(ErrorCodes.S0 == response.getErrCode()) {
			resultNum = transferService.updateSuccessYn(transfer, Transfer.ynCode.Y);
		} else {
			resultNum = transferService.updateSuccessYn(transfer, Transfer.ynCode.N);
		}
		log.info("transferService.updateSuccessYn result num => " + resultNum);

		return ResultResponse.builder().errCode(ErrorCodes.S0).errMsg("").data(transferRequest).build();
	}

}
