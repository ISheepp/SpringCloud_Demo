package xyz.codelin.hystrix;

import com.netflix.hystrix.HystrixCommand;
import org.springframework.web.client.RestTemplate;

/**
 * 泛型是访问的请求结果
 * @author linzy
 * @create 2020-12-26 17:57:39
 */
public class HelloCommand extends HystrixCommand<String> {

    RestTemplate restTemplate;

    public HelloCommand(Setter setter, RestTemplate restTemplate) {
        super(setter);
        this.restTemplate = restTemplate;
    }

    // 调用的时候就会执行run方法
    // run方法，发起请求的地方
    @Override
    protected String run() throws Exception {
        return restTemplate.getForObject("http://provider/hello", String.class);
    }

    /**
     * 请求失败的回调
     * @return
     */
    @Override
    protected String getFallback() {
        return "error-extend";
    }
}
