package cn.master.nexus.common.exception;

import cn.master.nexus.common.result.HttpResultCode;
import cn.master.nexus.common.result.IResultCode;
import cn.master.nexus.common.result.ResultHolder;
import cn.master.nexus.common.util.LogUtils;
import cn.master.nexus.common.util.Translator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : 11's papa
 * @since : 2026/3/4, 星期三
 **/
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final Translator translator;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultHolder handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResultHolder.error(HttpResultCode.VALIDATE_FAILED.getCode(),
                translator.get(HttpResultCode.VALIDATE_FAILED.getMessage()),
                errors);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultHolder handleHttpRequestMethodNotSupportedException(HttpServletResponse response, Exception exception) {
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        return ResultHolder.error(HttpStatus.METHOD_NOT_ALLOWED.value(), exception.getMessage());
    }

    @NullMarked
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResultHolder> handleBusiness(BusinessException e) {
        IResultCode errorCode = e.getErrorCode();
        if (errorCode == null) {
            // 如果抛出异常没有设置状态码，则返回错误 message
            return ResponseEntity.internalServerError()
                    .body(ResultHolder.error(HttpResultCode.FAILED.getCode(), e.getMessage()));
        }
        int code = errorCode.getCode();
        String message = errorCode.getMessage();
        message = translator.get(message, message);
        if (errorCode instanceof HttpResultCode) {
            // 如果是 MsHttpResultCode，则设置响应的状态码，取状态码的后三位
            if (errorCode.equals(HttpResultCode.NOT_FOUND)) {
                message = getNotFoundMessage(message);
            }
            return ResponseEntity.status(code % 1000)
                    .body(ResultHolder.error(code, message, e.getMessage()));
        } else {
            // 响应码返回 500，设置业务状态码
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResultHolder.error(code, translator.get(message, message), e.getMessage()));
        }
    }

    private String getNotFoundMessage(String message) {
        message = String.format(message, translator.get("resource.name"));
        return message;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ResultHolder handleException(Exception e) {
        LogUtils.error(e);
        return ResultHolder.error(HttpResultCode.FAILED.getCode(),
                e.getMessage(), getStackTraceAsString(e));
    }

    public static String getStackTraceAsString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        return sw.toString();
    }
}
