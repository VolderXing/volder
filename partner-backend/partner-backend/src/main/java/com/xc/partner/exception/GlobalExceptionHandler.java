package com.xc.partner.exception;

import com.xc.partner.common.BaseResponse;
import com.xc.partner.common.ErrorCode;
import com.xc.partner.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author xingchen
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse handleBusinessException(BusinessException e){
        log.error("BusinessException:"+e.getMessage(),e);
        return ResultUtils.error(ErrorCode.PARAMS_ERROR,e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse handleRuntimeException(RuntimeException e){
        log.error("RuntimeException:"+e.getMessage(),e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }
}
