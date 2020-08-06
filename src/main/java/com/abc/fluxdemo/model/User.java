package com.abc.fluxdemo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.io.Serializable;

@Setter
@Getter
@Table("user")
public class User implements Serializable {


    private static final long serialVersionUID = 7954539896411114585L;

    private Long id;
    private String uid;
    private String nick;

    //这个一定要有。否则 操作数据库会爆出 ID不存在异常
    @Id
    public Long getId() {
        return id;
    }

}