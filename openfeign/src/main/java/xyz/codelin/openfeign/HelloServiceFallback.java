package xyz.codelin.openfeign;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.codelin.api.IUserService;
import xyz.codelin.commons.User;

import java.io.UnsupportedEncodingException;

/**
 * @author linzy
 * @create 2020-12-28 21:43:03
 */

@Component
@RequestMapping("/codelin") // 防止请求地址重复
public class HelloServiceFallback implements HelloService {
    @Override
    public String hello() {
        return "error";
    }

    @Override
    public String hello2(String name) {
        return "error2";
    }

    @Override
    public User addUser2(User user) {
        return null;
    }

    @Override
    public void getUserByName(String name) throws UnsupportedEncodingException {

    }
}
