package com.auth.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Transfer {
    private int authenticationRequestSerialNum;
    private int transferAuthenticationSerialNum;
    private ynCode successYn;
    private ynCode requestYn;
    private String requestTime;
    
    public enum ynCode{
    	Y, N
    }

}
