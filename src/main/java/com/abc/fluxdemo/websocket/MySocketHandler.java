package com.abc.fluxdemo.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

/**
 * @Author Mads
 * @Date 2020/8/6 16:58
 */
@Slf4j
@Component
public class MySocketHandler implements WebSocketHandler {

    private WebSocketSession webSocketSession;
    /**
     * 重置websocketsession
     */
    public void resetWebSocketSession(){
        webSocketSession = null;
    }

    public WebSocketSession getImSession(){
        return webSocketSession;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        if( null == webSocketSession) {
            webSocketSession = session;
        }
        return session.receive()
                .map(result -> {
                    DataBuffer dataBuffer = result.getPayload();

                    //如果是心跳
                    if(WebSocketMessage.Type.PONG == result.getType()){
                        WebSocketMessage webSocketMessage = session.pongMessage(aa->dataBuffer);
                        if( null != webSocketMessage){
                            webSocketMessage.getPayloadAsText();
                            log.info("im websocket pong");
                        }
                        return Mono.empty();
                    }else {
                        //如果是自己业务的消息。就把下面这个转成你对应的封装类就可以出处理了。
                        ByteBuffer byteBuffer = dataBuffer.asByteBuffer();

                        return Mono.empty();
                    }
                })
                .onErrorContinue((error,object)->{
                    log.error("WebSocketSession error:{}",error);

                }).then();
    }
    /**
     * 心跳
     */
    public Mono<Void> heartbeat(){
        if( null != webSocketSession) {
            return webSocketSession.send(Mono.just(webSocketSession.pingMessage(ping -> ping.wrap(new byte[1]))));
        }else{
            return Mono.empty();
        }
    }
}
