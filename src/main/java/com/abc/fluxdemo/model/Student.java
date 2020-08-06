package com.abc.fluxdemo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Setter
@Getter
@Table("student")
public class Student implements Serializable {


    private static final long serialVersionUID = -7954539896411114586L;

    private Long id;
    private String name;
    private Integer age;

    @Id
    public Long getId() {
        return id;
    }

}