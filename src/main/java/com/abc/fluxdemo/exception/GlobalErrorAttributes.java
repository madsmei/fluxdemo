package com.abc.fluxdemo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Slf4j
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    /*****
     *  全局异常捕获，统一处理
     * @Author Mads
    **/
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(request, options);
        log.error(String.format("GlobalErrorAttributes : requestUrl:%s,msg:%s",request.uri(),getError(request).getMessage()));
        for (StackTraceElement traceElement : getError(request).getStackTrace()){
            log.error("error:{}",traceElement);
        }
        if (getError(request) instanceof MadsException) {
            MadsException ex = (MadsException) getError(request);
            map.put("code", ex.getCode());
            map.put("msg", ex.getMsg());
            map.put("data", null);
            return map;
        }

        map.put("code", 500);
        map.put("msg", "System Error , Check logs!");
        map.put("data", null);
        return map;
    }
}
