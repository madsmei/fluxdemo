package com.abc.fluxdemo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/****
 *  封装 返回结构。。
 * @Author Mads
 * @Date 2020/7/14
**/
@Setter
@Getter
public class ResultModel<T> {

    private Integer code;

    private String msg;

    private Object data;

    public static <T> Mono<ServerResponse> ok(Mono<T> data){
        return ServerResponse.ok().body(responseBodyCreate(data, 200, "success"), ResultModel.class);
    }

    public static <T> Mono<ServerResponse> fail(){
        return ServerResponse.ok().body(responseBodyCreate(Mono.just(""), 500, "error"), ResultModel.class);
    }
    public static <T> Mono<ServerResponse> fail(Integer code,String msg){
        return ServerResponse.ok().body(responseBodyCreate(Mono.just(""), code, msg), ResultModel.class);
    }

    private static <T> Mono<ResultModel<T>> responseBodyCreate(Mono<T> monoData, int code, String msg) {
        return monoData.map(data-> {
            final ResultModel<T> responseInfo = new ResultModel<>();
            responseInfo.setCode(code);
            responseInfo.setData(data);
            responseInfo.setMsg(msg);
            return responseInfo;
        });
    }
}
