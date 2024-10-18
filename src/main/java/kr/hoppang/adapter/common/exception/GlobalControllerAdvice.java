package kr.hoppang.adapter.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.stream.Collectors;
import kr.hoppang.adapter.common.exception.custom.HoppangDuplicatedLoginException;
import kr.hoppang.adapter.common.exception.custom.HoppangException;
import kr.hoppang.adapter.common.exception.custom.HoppangExpiredRefreshToken;
import kr.hoppang.adapter.common.exception.custom.HoppangLoginException;
import kr.hoppang.adapter.common.exception.custom.InvalidInputException;
import kr.hoppang.adapter.outbound.alarm.dto.ErrorAlarm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final ApplicationEventPublisher eventPublisher;

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(final HttpServletRequest request, final Exception e) {

        errorPrinter(request, e);

        return ResponseEntity.badRequest().body(new ErrorResponse(-2, e.toString()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseError(final HttpServletRequest request, DataAccessException e) {

        errorPrinter(request, e);

        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(
                        0,
                        "Database connection error. Please try again later.")
                );
    }

    @ExceptionHandler(HoppangDuplicatedLoginException.class)
    public ResponseEntity<ErrorResponseForDuplicateSso> handleDuplicatedSsoLoginError(
            final HttpServletRequest request, final HoppangDuplicatedLoginException e) {

        onlyPrintError(request, e);

        return ResponseEntity.internalServerError()
                .body(new ErrorResponseForDuplicateSso(
                        e.getCode(),
                        e.getEmail(),
                        e.getOauthType(),
                        e.getMessage())
                );
    }

    @ExceptionHandler(HoppangException.class)
    public ResponseEntity<ErrorResponse> inputBadRequestExceptionHandler(
            final HttpServletRequest request, final HoppangException e) {

        errorPrinter(request, e);

        return ResponseEntity.badRequest().body(new ErrorResponse(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> invalidInputExceptionHandler(
            final HttpServletRequest request, final InvalidInputException e) {

        errorPrinter(request, e);

        return ResponseEntity.badRequest().body(new ErrorResponse(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(HoppangLoginException.class)
    public ResponseEntity<ErrorResponse> handleValidException(final HttpServletRequest request,
            HoppangLoginException e) {

        errorPrinter(request, e);

        return ResponseEntity.badRequest().body(new ErrorResponse(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(HoppangExpiredRefreshToken.class)
    public ResponseEntity<ErrorResponse> handleValidException(final HttpServletRequest request,
            HoppangExpiredRefreshToken e) {

        onlyPrintError(request, e);

        return ResponseEntity.badRequest().body(new ErrorResponse(e.getCode(), e.getMessage(), e.getRedirectUrl()));
    }

    /**
     * validation 애노테이션(유효성) 예외를 최종적으로 이곳에서 처리할 수 있음.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidException(final HttpServletRequest request,
            MethodArgumentNotValidException e) {

        errorPrinter(request, e);

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        -1,
                        "요청 형식이 맞지 않습니다."
                ));
    }


    public void errorPrinter(final HttpServletRequest request, final Exception e) {

        String requestBody = null;
        try {
            if (request instanceof CachedBodyHttpServletRequest cachedRequest) {
                requestBody = cachedRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (IOException ioException) {
            log.error("Failed to read request body", ioException);
        }

         String httpMethod = request.getMethod();
         String apiUrl = request.getRequestURI();
         String queryString = request.getQueryString();
         String body = requestBody;

         // header 설정
        StringBuilder header = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            header.append("\n    ").append(name).append(": ").append(value);
        }

        eventPublisher.publishEvent(
                new ErrorAlarm("[ " + httpMethod + " ] " + apiUrl, queryString, body, e.toString()));

        log.error(
                "\n # HEADER={}\n # METHOD={}\n # PATH={}\n # PARAMETER={}\n # BODY=\n{}\n # ERRMSG={}",
                header,
                httpMethod,
                apiUrl,
                queryString,
                body,
                e.toString());
    }

    public void onlyPrintError(final HttpServletRequest request, final Exception e) {

        String requestBody = null;
        try {
            if (request instanceof CachedBodyHttpServletRequest cachedRequest) {
                requestBody = cachedRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (IOException ioException) {
            log.error("Failed to read request body", ioException);
        }

         String httpMethod = request.getMethod();
         String apiUrl = request.getRequestURI();
         String queryString = request.getQueryString();
         String body = requestBody;

         // header 설정
        StringBuilder header = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            header.append("\n    ").append(name).append(": ").append(value);
        }

        log.error(
                "\n # HEADER={}\n # METHOD={}\n # PATH={}\n # PARAMETER={}\n # BODY=\n{}\n # ERRMSG={}",
                header,
                httpMethod,
                apiUrl,
                queryString,
                body,
                e.toString());
    }
}
