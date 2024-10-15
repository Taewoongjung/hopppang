package kr.hoppang.adapter.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ErrorResponseForDuplicateSso {

    private int errorCode;
    private String email;
    private String oAuthType;
    private String errorMessage;
}
