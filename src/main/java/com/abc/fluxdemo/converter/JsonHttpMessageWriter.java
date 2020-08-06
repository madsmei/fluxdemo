package com.abc.fluxdemo.converter;

import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class JsonHttpMessageWriter implements HttpMessageWriter<Map<String, Object>> {

    @Override
    public List<MediaType> getWritableMediaTypes() {
        return Collections.singletonList(MediaType.APPLICATION_JSON);
    }

    @Override
    public boolean canWrite(ResolvableType resolvableType, MediaType mediaType) {
        return true;
    }

    @Override
    public Mono<Void> write(Publisher<? extends Map<String, Object>> publisher, ResolvableType resolvableType, MediaType mediaType, ReactiveHttpOutputMessage reactiveHttpOutputMessage, Map<String, Object> map) {
        return Mono.from(publisher).flatMap(m -> reactiveHttpOutputMessage.writeWith(Mono.just(reactiveHttpOutputMessage.bufferFactory().wrap(transform2Json(m).getBytes()))));
    }


    private String transform2Json(Map<String, Object> objectMap){
//        Map<String,Object> resultMap = new HashMap<>();
//        resultMap.put("code",objectMap.getOrDefault("code",500));

        return "{\"code\":"+objectMap.getOrDefault("code",500)+",\"msg\":\""+objectMap.getOrDefault("msg","error")+"\",\"data\":\"\"}";

    }
}
