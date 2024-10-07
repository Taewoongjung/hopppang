package kr.hoppang.adapter.common.exception.custom;

import kr.hoppang.adapter.common.exception.ErrorType;
import lombok.Getter;

@Getter
public class InvalidInputException extends RuntimeException {

    private final int code;

    public InvalidInputException(final ErrorType errorType) {
        super(errorType.getMessage());
        this.code = errorType.getCode();
    }
}
