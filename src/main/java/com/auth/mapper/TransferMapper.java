package com.auth.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.auth.vo.Transfer;

@Repository
@Mapper
public interface TransferMapper {
	
	
	int insertTransferAuthentication(Transfer transfer);
    String retrieveRequestYn(int transferAuthenticationSerialNum);
    int retrieveTransferAuthenticationSerialNum(int authenticationRequestSerialNum);
	int updateRequestYn(Transfer transferVo);
	int updateSuccessYn(Transfer transferVo);
}
