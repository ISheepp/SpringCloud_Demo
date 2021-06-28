package xyz.codelin.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

/**
 * @author linzy
 * @create 2020-12-26 17:28:49
 */

@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    /**
     * 在这个方法中我们将发起一个远程调用，去调用provider中提供的/hello接口
     *
     * 但是这个调用可能会失败（比如provider掉线）
     *
     * 我们在这个方法上添加@HystrixCommand注解，配置fallbackMethod属性，这个属性表示该方法调用失败时的替代方法
     * @return
     */
    @HystrixCommand(fallbackMethod = "error")
    public String hello() {
        // 第二个参数：返回的数据类型
        return restTemplate.getForObject("http://provider/hello", String.class);
    }


    // 通过注解实现请求异步调用
    @HystrixCommand
    public Future<String> hello2() {
        return new AsyncResult<String>() {
            @Override
            public String invoke() {
                return restTemplate.getForObject("http://provider/hello", String.class);
            }
        };
    }

    /**
     * 注意，这个方法名字要和fallbackMethod一致
     * 方法返回值也要和对应的方法一致
     * @return
     */
    public String error() {
        return "error";
    }

}
