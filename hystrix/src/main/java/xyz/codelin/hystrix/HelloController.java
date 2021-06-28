package xyz.codelin.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author linzy
 * @create 2020-12-26 17:30:15
 */

@RestController
public class HelloController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HelloService helloService;

    @GetMapping("/hello")
    public String hello() {
        return helloService.hello();
    }

    @GetMapping("/hello2")
    public void hello2() {
        // new出来一个只能执行一次
        // 可以直接执行，也可以先入队后执行
        HelloCommand helloCommand =
                new HelloCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("codelin")),
                        restTemplate);
        String execute = helloCommand.execute(); // 直接执行
        try {
            Future<String> queue = helloCommand.queue();
            String s = queue.get();
            System.out.println(s); // 先入队 后执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    // 请求异步调用
    @GetMapping("/hello3")
    public void hello3() {
        Future<String> hello2 = helloService.hello2();
        try {
            String s = hello2.get();
            System.out.println(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
