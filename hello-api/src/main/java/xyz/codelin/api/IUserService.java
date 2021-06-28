package xyz.codelin.api;

import org.springframework.web.bind.annotation.*;
import xyz.codelin.commons.User;

import java.io.UnsupportedEncodingException;

/**
 * @author linzy
 * @create 2020-12-27 17:10:27
 */
@RestController
public interface IUserService {

    @GetMapping("/hello")
    String hello(); // 这里的方法名无所谓 provider/hello

    @GetMapping("/hello2")
    String hello2(@RequestParam("name") String name); // 一定要指定参数名字

    @PostMapping("/user2")
    User addUser2(@RequestBody User user);

    @GetMapping("/user3")
    void getUserByName(@RequestHeader("name") String name) throws UnsupportedEncodingException;

}
