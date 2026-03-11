package cn.master.nexus.common.exception;

import cn.master.nexus.common.result.IResultCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : 11's papa
 * @since : 2026/3/4, 星期三
 **/
@Getter
public class BusinessException extends RuntimeException {
    protected IResultCode errorCode;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable t) {
        super(t);
    }

    public BusinessException(IResultCode errorCode) {
        super(StringUtils.EMPTY);
        this.errorCode = errorCode;
    }

    public BusinessException(IResultCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(IResultCode errorCode, Throwable t) {
        super(t);
        this.errorCode = errorCode;
    }

    public BusinessException(String message, Throwable t) {
        super(message, t);
    }

}
