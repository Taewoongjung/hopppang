package kr.hoppang.adapter.common.exception.custom;

import kr.hoppang.adapter.common.exception.ErrorType;
import lombok.Getter;

@Getter
public class HoppangLoginException extends RuntimeException{

    private final int code;

    public HoppangLoginException(final ErrorType errorType) {
        super(errorType.getMessage());
        this.code = errorType.getCode();
    }
}
