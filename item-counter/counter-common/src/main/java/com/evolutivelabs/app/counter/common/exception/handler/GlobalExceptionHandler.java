package com.evolutivelabs.app.counter.common.exception.handler;

import com.evolutivelabs.app.counter.common.annotation.PrintException;
import com.evolutivelabs.app.counter.common.exception.ApiErrors;
import com.evolutivelabs.app.counter.common.exception.ForbiddenException;
import com.evolutivelabs.app.counter.common.exception.NotFoundException;
import com.evolutivelabs.app.counter.common.exception.UnAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/**
 * 所有exception皆會在此處理
 * @since 2021-12-29
 * @author lucas
 */
@RestControllerAdvice(annotations = RestController.class, basePackages= "com.evolutivelabs.app.counter")
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 如未設定其他exception handler 皆以此訊息回覆
     * code 500
     */
    @PrintException
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandle(Exception ex) {
        return "其餘未知錯誤，請查詢log";
    }

    /**
     * 於controller 由@Validated @Valid檢核不通過的exception
     * code 400
     * @see javax.validation.Valid
     * @see org.springframework.validation.annotation.Validated
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ApiErrors(ex.getAllErrors());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unAuthorizedException(UnAuthorizedException ex) {
        return "未進行認證";
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String forbiddenException(ForbiddenException ex) {
        return "拒絕登入";
    }

    /**
     * 客制path如帶入參數未查詢到結果或查詢條件不符拋出的錯誤
     * code 404
     * @param ex
     * @return
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundException(NotFoundException ex) {
        return ex.getMessage();
    }
}
