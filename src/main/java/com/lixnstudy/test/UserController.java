package com.lixnstudy.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lixn
 * @ClassName UserController
 * @CreateDate 2021/9/14
 * @Description
 */
@RestController
@RequestMapping("/api")
public class UserController {
    @GetMapping("/test")
    public User getUser() {

        return new User("张三", 18, "男");
    }
}
