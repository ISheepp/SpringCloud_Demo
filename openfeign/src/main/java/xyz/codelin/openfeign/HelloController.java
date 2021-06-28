package xyz.codelin.openfeign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.codelin.commons.User;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author linzy
 * @create 2020-12-27 15:44:13
 */

@RestController
public class HelloController {

    @Autowired
    HelloService helloService;

    @GetMapping("/hello1")
    public String hello() throws UnsupportedEncodingException {
        String s = helloService.hello2("lzy");
        System.out.println(s);
        User user = new User();
        user.setId(1);
        user.setUsername("hhhhhhhh");
        user.setPassword("123");
        User u = helloService.addUser2(user);
        System.out.println(u);
        // 转码
        helloService.getUserByName(URLEncoder.encode("林子洋","UTF-8"));
        return helloService.hello();
    }

}
