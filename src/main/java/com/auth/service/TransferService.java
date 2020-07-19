package com.auth.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.auth.exception.BizException;
import com.auth.mapper.TransferMapper;
import com.auth.vo.ErrorCodes;
import com.auth.vo.ResultResponse;
import com.auth.vo.Transfer;
import com.auth.vo.TransferRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransferService {
	@Autowired
	public TransferMapper mapper;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private AsyncRestTemplate asyncRestTemplate;

	public int insertTransferAuthentication(int authenticationRequestSerialNum) throws BizException {
		Transfer transfer = Transfer.builder().authenticationRequestSerialNum(authenticationRequestSerialNum).build();
		int result = mapper.insertTransferAuthentication(transfer);
		if (result <= 0) {
			throw new BizException(ErrorCodes.E6, "DB 데이터 삽입에 실패하였습니다.");
		}
		return transfer.getTransferAuthenticationSerialNum();
	}

	public int retrieveTransferAuthenticationSerialNum(int authenticationRequestSerialNum) {
		return mapper.retrieveTransferAuthenticationSerialNum(authenticationRequestSerialNum);
	}

	public void doTransferAuthentication(Transfer transfer, TransferRequest transferRequest) throws BizException{
		try {
			bring1won();
		} catch (Exception e) {
			log.error("TransferService.doTransferAuthentication => {}", e.getMessage());
			throw new BizException(ErrorCodes.E1, "수신개발AP 인터페이스 시 오류가 발생하였습니다.");
		}
		try {
			retrieveAnotherBankAccountInfo(transferRequest);
			transferAnotherBank(transfer, transferRequest);
		} catch (Exception e) {
			log.error("TransferService.doTransferAuthentication => {}", e.getMessage());
			throw new BizException(ErrorCodes.E2, "[대외연계AP 인터페이스]" + e.getMessage());
		} finally {
			try {
				give1won();
			} catch (Exception e) {
				log.error("TransferService.doTransferAuthentication => {}", e.getMessage());
				throw new BizException(ErrorCodes.E1, "수신개발AP 인터페이스 시 오류가 발생하였습니다.");
			}
		}

	}

	public void bring1won() {
		restTemplate.getForObject("http://localhost:8080/reception/bring1won", ResultResponse.class);
	}

	public void give1won() {
		restTemplate.getForObject("http://localhost:8080/reception/give1won", ResultResponse.class);
	}

	public void retrieveAnotherBankAccountInfo(TransferRequest transferRequest) throws BizException {
		try {
			ResultResponse response = postForEntityAsynchronously("http://localhost:8080/another-bank/info", transferRequest);
			if (ErrorCodes.S0 != response.getErrCode()) {
				throw new BizException(response.getErrCode(), response.getErrMsg());
			}
		} catch (Exception e) {
			throw new BizException(ErrorCodes.E2, "타행 계좌 정보 조회시 에러가 발생하였습니다.");
		}
	}

	public void transferAnotherBank(Transfer transfer, TransferRequest transferRequest) throws BizException {
		Transfer.ynCode status = mapper.retrieveRequestYn(transfer.getTransferAuthenticationSerialNum());
		if (Transfer.ynCode.Y == status) {
			throw new BizException(ErrorCodes.E2, "중복 이체 요청이 발생하였습니다.");
		}
		transfer.setRequestYn(Transfer.ynCode.Y);
		mapper.updateRequestYn(transfer);

		ResultResponse response = ResultResponse.builder().build();
		try {
			response = postForEntityAsynchronously("http://localhost:8080/another-bank/transfer", transferRequest);
		} catch (Exception e) {
			throw new BizException(ErrorCodes.E2, "타행 계좌 이체시 에러가 발생하였습니다.");
		}
		if (ErrorCodes.S0 != response.getErrCode()) {
			throw new BizException(response.getErrCode(), response.getErrMsg());
		}
	}

	public int updateRequestYn(Transfer transfer, Transfer.ynCode requestYn) {
		transfer.setRequestYn(requestYn);
		return mapper.updateRequestYn(transfer);
	}

	public int updateSuccessYn(Transfer transfer, Transfer.ynCode result) {
		transfer.setSuccessYn(result);
		return mapper.updateSuccessYn(transfer);
	}

	public ResultResponse postForEntityAsynchronously(String url, TransferRequest transferRequest) throws Exception {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.set("bankCd", transferRequest.getBankCd());
		parameters.set("bankAccount", transferRequest.getBankAccount());
		parameters.set("accountHolder", transferRequest.getAccountHolder());
		parameters.set("requestWord", transferRequest.getRequestWord());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Accept", "application/json");
		headers.add("Connection", "keep-alive");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
		ListenableFuture<ResponseEntity<ResultResponse>> lf = asyncRestTemplate.postForEntity(url, request,
				ResultResponse.class);

		// timeout 옵션 넣어줘야함
		lf.get(10, TimeUnit.SECONDS);

		lf.addCallback(result -> {
			log.debug("TransferService.postForEntityAsynchronously get Body => " + result);
		}, ex -> {
			try {
				lf.get().getBody().setErrCode(ErrorCodes.E5);
				lf.get().getBody().setErrMsg("통신간 문제가 발생하였습니다.");
				lf.get().getBody().setData("");
				log.debug("TransferService.postForEntityAsynchronously => " + ex.getMessage());
			} catch (InterruptedException | ExecutionException e) {
				log.debug("TransferService.postForEntityAsynchronously => " + e.getMessage());
			}
		});

		return lf.get().getBody();
	}

}
