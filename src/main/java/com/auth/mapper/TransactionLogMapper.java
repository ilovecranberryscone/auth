package com.auth.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.auth.vo.TransactionLog;

@Repository
@Mapper
public interface TransactionLogMapper {
	
	
	void insertTransactionLog(TransactionLog log);
}
