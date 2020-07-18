package com.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth.mapper.TransactionLogMapper;
import com.auth.vo.TransactionLog;


@Service
public class TransactionLogService {
	@Autowired
	public TransactionLogMapper mapper;
	public void insertTransactionLog(TransactionLog log) {
		mapper.insertTransactionLog(log);
	}

}
