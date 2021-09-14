package com.lixnstudy.test;

import java.io.Serializable;

/**
 * @Author lixn
 * @ClassName User
 * @CreateDate 2021/9/14
 * @Description
 */
public class User implements Serializable {
    private String name;
    private Integer id;
    private String sex;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", sex='" + sex + '\'' +
                '}';
    }

    public User(String name, Integer id, String sex) {
        this.name = name;
        this.id = id;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
