package xyz.codelin.openfeign;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import xyz.codelin.commons.User;
import java.io.UnsupportedEncodingException;

/**
 * @author linzy
 * @create 2020-12-28 22:00:43
 */
@Component
public class HelloServiceFallbackFactory implements FallbackFactory<HelloService> {
    @Override
    public HelloService create(Throwable throwable) {
        return new HelloService() {
            @Override
            public String hello() {
                return "error";
            }

            @Override
            public String hello2(String name) {
                return "error---";
            }

            @Override
            public User addUser2(User user) {
                return null;
            }

            @Override
            public void getUserByName(String name) throws UnsupportedEncodingException {

            }
        };
    }
}
