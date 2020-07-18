package com.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponse {
    private ErrorCodes errCode;
    private String errMsg;
    private Object data;

}
