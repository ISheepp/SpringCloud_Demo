package xyz.codelin.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import xyz.codelin.api.IUserService;
import xyz.codelin.commons.User;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

/**
 * @author linzy
 * @create 2020-12-19 15:27:55
 */

@RestController
public class HelloController implements IUserService {

    @Value("${server.port}")
    Integer port;

    @Override
    public String hello() {
        return "hello codelin" + port;
    }

    public String hello2(String name) {
        System.out.println(new Date() + ">>>>" + name);
        return "hello " + name;
    }

    // post接口
    // 可以用json形式，也可以用kv形式
    @GetMapping("/user1")
    public User addUser(User user) {
        return user;
    }

    // 以json形式传
    @Override
    public User addUser2(@RequestBody User user) {
        return user;
    }

    @Override
    public void getUserByName(@RequestHeader String name) throws UnsupportedEncodingException {
        // 中文解码
        System.out.println(URLDecoder.decode(name, "UTF-8"));
    }

}
