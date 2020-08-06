package com.abc.fluxdemo.helper;

import io.netty.channel.ChannelOption;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * WebClient 接口调用，对应 httpclint的功能点
 * @Author Mads
 */
@Component
public class WebClientHelper {

    private static TcpClient getTcpClient() {
        return TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
    }

    public static WebClient getWebClient(String url) {

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(getTcpClient())))
                .defaultHeader("Content-Type", "application/json")
                .baseUrl(url)
                .build();
    }


    public static <K> Mono<K> post(String url,String uri,Mono requestMono,Class<K> result) {
        WebClient.RequestBodyUriSpec post = getWebClient(url)
                .post();//请求类型

        if(null != requestMono) {
            post.body(requestMono,requestMono.getClass());//请求消息体
        }
        Mono<K> resultMono = post.uri(uri)//请求地址和参数
                .retrieve()//获取响应主体并对其进行解码的最简单方法
                .bodyToMono(result)
                .retry(2)//重试2次，加上首次的。一共是请求3次
//                .retryBackoff(2,Duration.ofSeconds(1))//重试两次，重试间隔1秒
//                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
//                    return Mono.error(new Cum5xxException(clientResponse.statusCode().value() + " error code"));
//                })
//                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
//                    return Mono.error(new Cum4xxException(clientResponse.statusCode().value() + " error code"));
//                })
//                .onStatus(HttpStatus::isError, clientResponse -> {
//                    return Mono.error(new Cum4xxException(clientResponse.statusCode().value() + " error code"));
//                })
//                .bodyToMono(Map.class)
//                .onErrorResume(SocketException.class, re -> {//如果是连接异常。切换地址在请求。
//                    webClient.post().uri("http://192.168.98.112").retrieve().bodyToMono(String.class)
//                }).onErrorReturn("{code: -1, msg: '服务异常'}")//如果还是异常。返回默认默认结果
                .timeout(Duration.ofSeconds(1));//超时
        return resultMono;//返回结果
    }
    public static <K> Mono<K> get(String url,String uri,Mono requestMono,Class<K> result) {
        WebClient.RequestHeadersUriSpec<?> get = getWebClient(url)
                .get();//请求类型

        Mono<K> resultMono = get.uri(uri)//请求地址和参数
                .retrieve()//获取响应主体并对其进行解码的最简单方法
                .bodyToMono(result)
                .retry(2)//重试2次，加上首次的。一共是请求3次
                .timeout(Duration.ofSeconds(1));//超时
        return resultMono;//返回结果
    }


    public static void main(String[] args) {

        Map<String,String> requestMap = new HashMap<>();
        requestMap.put("token","8c6a16396bcbe2aed2fe0lspf4");
        Mono<Map> requestMono = Mono.just(requestMap);

        Mono map = post("http://testv2.mads.mobi", "/api/getList?id=123", requestMono, Map.class);
        map.flatMap(result->{
            System.out.println("-----:"+result);
            return Mono.empty();
        }).subscribe();
        System.out.println(111);
    }
}
