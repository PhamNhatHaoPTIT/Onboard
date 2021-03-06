package com.haopn.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable {
    private long id;
    private String abc;

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", abc='" + abc + '\'' +
                '}';
    }
}
