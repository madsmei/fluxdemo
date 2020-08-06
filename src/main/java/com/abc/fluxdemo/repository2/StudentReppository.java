package com.abc.fluxdemo.repository2;

import com.abc.fluxdemo.model.Student;
import com.abc.fluxdemo.model.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/****
 *   写法 和JPA基本类似。发现没有，这里并没有使用@Repository注解,
 * @Author Mads
 * @Date 2020/7/14
**/
public interface StudentReppository extends
        ReactiveCrudRepository<Student, Long> {

    Mono<Student> findFirstByName(String name);


    @Query(value="select distinct age from student where name=:name order by id desc limit :size")
    Flux<Integer> getAllUidByStatus(@Param("name") String name, Integer size);

}
