package com.abc.fluxdemo.handler;

import com.abc.fluxdemo.model.ResultModel;
import com.abc.fluxdemo.model.Student;
import com.abc.fluxdemo.repository1.UserReppository;
import com.abc.fluxdemo.repository2.StudentReppository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class FluxHandler {

    @Autowired
    private UserReppository userReppository;//数据源1
    @Autowired
    private StudentReppository studentReppository;//数据源2

    @Resource(name="madsRedisRedissonTemplate")
    private ReactiveRedisTemplate reactiveRedisTemplate;

    /****
     * 从数据源1中拉取信息
     * @Author Mads
    **/
    public Mono<ServerResponse> getUser(ServerRequest request){

        //请求体中的参数。
        Mono<Map> params = request.bodyToMono(HashMap.class);
        return params.flatMap(map->{

            String id = (String)map.get("id");

            return ResultModel.ok(userReppository.findFirstByUid(id));
        });
    }
    /****
     * 从数据源2中拉取信息
     * @Author Mads
     **/
    public Mono<ServerResponse> getStudent(ServerRequest request){

        //请求体中的参数。
        Mono<Map> params = request.bodyToMono(HashMap.class);
        return params.flatMap(map->{

            String name = (String)map.get("name");

            return ResultModel.ok(studentReppository.findFirstByName(name).switchIfEmpty(Mono.just(new Student())));
        });
    }

    public Mono<ServerResponse> getRedis(ServerRequest request){
        //URL中的参数 ？name=shuai
        String name = request.queryParam("name").get();

        return ResultModel.ok(reactiveRedisTemplate.opsForValue().set("mads",name).flatMap(b->{
            return reactiveRedisTemplate.opsForValue().get("mads");
        }));
    }

    public Mono<ServerResponse> getRedis1(ServerRequest request){
        // url中的参数 xx/shuai/
        String name = request.pathVariable("name");

        return ResultModel.ok(reactiveRedisTemplate.opsForValue().set("mads",name).flatMap(b->{
            return reactiveRedisTemplate.opsForValue().get("mads");
        }));
    }

}
