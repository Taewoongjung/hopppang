package kr.hoppang.adapter.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;

@Getter
@JsonInclude(Include.NON_NULL)
public class ErrorResponse {

    private final int errorCode;
    private final String errorMessage;
    private String redirectUrl;

    public ErrorResponse(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(int errorCode, String errorMessage, String redirectUrl) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.redirectUrl = redirectUrl;
    }
}
