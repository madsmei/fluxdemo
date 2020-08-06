package com.abc.fluxdemo.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/*****
 *  异常封装 类
 * @Author Mads
**/
@Setter
@Getter
public class MadsException extends ResponseStatusException {


    private static final long serialVersionUID = 284869572380839465L;

    public MadsException(HttpStatus status) {
        super(status);
    }

    public MadsException(HttpStatus status, Integer code, String msg) {
        super(status);
        this.code = code;
        this.msg = msg;
    }


    private Integer code;

    private String msg;
}
