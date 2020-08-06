package com.abc.fluxdemo.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@EnableScheduling
public class MySocketTest {

    @Autowired
    private MySocketHandler mySocketHandler;

    private Integer hasDestroy=0;

    /*****
     *  发心跳。维持链接
     * @Author Mads
     **/
    @Scheduled(cron = "0/10 * * * * ?")
    public void doHeartbeatTask()  {
        mySocketHandler.heartbeat().subscribe();
    }

    /*****
     *  项目启动 开启scoket
     * @Author Mads
    **/
    @PostConstruct
    public void inintWebSocket() {
        doScoketConnect();
    }
    /*****
     *  重连
     * @Author Mads
     **/
    public void restartConection() {
        //这一步不要缺失、会造成重连失败
        mySocketHandler.resetWebSocketSession();

        doScoketConnect().subscribe();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*****
     *  webscoket的通知方式都是异步的。结果在mySocketHandler里返回。
     * @Author Mads
    **/
    public Mono<Void> doScoketConnect() {

        //建立websocket链接
        ReactorNettyWebSocketClient reactorNettyWebSocketClient = new ReactorNettyWebSocketClient();
        String webscoketUrl = "https://mads.mobi/v1/websocket";
        return reactorNettyWebSocketClient.execute( URI.create(webscoketUrl),mySocketHandler)
                .doOnSubscribe(result->{

                })
                .doOnTerminate(()->{//开启个新线程处理重连
                    //webscoket 断开链接 如果是关机不重新链接
                    if(0 == hasDestroy) {
                        log.info("###################websocket do on terminate");

                        restartConection();
                    }
                })
                .then();
    }

    /****
     * 关机时 设置标志位。防止触发socket重连
     * @Author Mads
    **/
    @PreDestroy
    public void destroy() {
        hasDestroy = 1;
        log.info("#####################################destroy");
    }
}
