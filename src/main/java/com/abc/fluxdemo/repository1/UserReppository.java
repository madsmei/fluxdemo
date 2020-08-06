package com.abc.fluxdemo.repository1;

import com.abc.fluxdemo.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/****
 *   写法 和JPA基本类似。发现没有，这里并没有使用@Repository注解,
 * @Author Mads
 * @Date 2020/7/14
**/
public interface UserReppository extends
        ReactiveCrudRepository<User, Long> {

    Mono<User> findFirstByUid(String uid);


}
