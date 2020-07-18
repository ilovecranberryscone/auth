package com.auth.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransferRequest {
    private String bankCd;
    private String bankAccount;
    private String accountHolder;
    private String requestWord;
    private int authenticationRequestSerialNum;

}
