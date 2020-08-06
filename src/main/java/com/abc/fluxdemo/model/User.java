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

    @Id
    public Long getId() {
        return id;
    }

}