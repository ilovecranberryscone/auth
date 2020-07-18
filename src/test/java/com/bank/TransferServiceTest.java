package com.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.auth.AuthApplication;
import com.auth.exception.BizException;
import com.auth.service.TransferService;
import com.auth.vo.ErrorCodes;
import com.auth.vo.ResultResponse;
import com.auth.vo.Transfer;
import com.auth.vo.TransferRequest;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes=AuthApplication.class)
public class TransferServiceTest {

	@Autowired
	private TransferService tranferService;
	
    @Test
    public void insertTransferAuthenticationTest() throws Exception {
    	TransferRequest transferRequest = TransferRequest.builder()
				.bankAccount("23455678987654")
				.bankCd("H1")
				.accountHolder("떡볶이")
				.authenticationRequestSerialNum(1)
				.build();
    	int transferAuthenticationNum = tranferService.insertTransferAuthentication(transferRequest.getAuthenticationRequestSerialNum());
    	assertEquals(94, transferAuthenticationNum);
    	
    }
    
    @Test
    public void doTransferAuthenticationTest() throws Exception {
    	TransferRequest tr = TransferRequest.builder()
				.bankAccount("12345678987656")
				.bankCd("W1")
				.accountHolder("라볶이")
				.authenticationRequestSerialNum(2)
				.build();
    	Transfer transfer = Transfer.builder().build();
    	
    	ResultResponse response = new ResultResponse();
    	try {
    		tranferService.doTransferAuthentication(transfer, tr);
    	} catch(BizException e) {
    		response = ResultResponse.builder().errCode(e.getErrorCode()).errMsg(e.getMessage()).build();
    	}
    	
    	assertEquals(response.getErrCode(), ErrorCodes.E1);
    	
    }
    
    

}
