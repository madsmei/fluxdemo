package com.abc.fluxdemo.router;

import com.abc.fluxdemo.handler.FluxHandler;
import com.abc.fluxdemo.model.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/*****
 *  Flux的路由配置。既然使用webflux，还是使用这种方式比较合理。
 * @Author Mads
 * @Date 2020/7/14
**/
@Configuration
public class FluxRouter {

    @Autowired
    private FluxHandler fluxHandler;

    @Bean
    public RouterFunction<ServerResponse> router() {

        return RouterFunctions.route().path("/test", builder -> builder
                .GET("/user", accept(MediaType.APPLICATION_JSON), fluxHandler::getUser)
                .GET("/student", accept(MediaType.APPLICATION_JSON), fluxHandler::getStudent)
                .GET("/getRedis", fluxHandler::getRedis)
                .GET("/getRedis1/{id}", fluxHandler::getRedis1))
                //增加请求过过滤，如果不想加。忽略这一行即可
                .filter((serverRequest, handlerFunction) -> filter(serverRequest,handlerFunction))
                .build();
    }

    /*****
     *  增加自己的过滤条件，可以在对特殊字段进行特殊拦截校验处理，
     * @Author Mads
     * @Date 2020/7/14
    **/
    private Mono<ServerResponse> filter(ServerRequest serverRequest, HandlerFunction handlerFunction){
        //示例：请求头信息中 必须包含 appid=1,否则验证不过
        String appid = serverRequest.headers().firstHeader("appid");
        if (!"1".equals(appid)) {
            return ResultModel.fail();
        }
        return handlerFunction.handle(serverRequest);
    }
}