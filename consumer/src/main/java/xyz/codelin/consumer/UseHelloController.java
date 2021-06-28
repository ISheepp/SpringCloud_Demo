package xyz.codelin.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import xyz.codelin.commons.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author linzy
 * @create 2020-12-19 15:33:53
 */

@RestController
public class UseHelloController {

    @Autowired
    DiscoveryClient discoveryClient;

    // 写死

    @GetMapping("/hello1")
    public String hello1() {
        HttpURLConnection con = null;
        try{
            URL url = new URL("http://localhost:1113/hello");
            con = (HttpURLConnection  ) url.openConnection();
            if (con.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String s = br.readLine();
                br.close();
                return s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
    @Autowired
    @Qualifier("restTemplateOne")
    RestTemplate restTemplateOne;

    @GetMapping("/hello2")
    public String hello2() {
        // 根据服务名从Eureka Server上查询到一个服务的信息
        // discoveryClient查询到的可能是一个集合，因为可能是集群化部署
        List<ServiceInstance> list = discoveryClient.getInstances("provider");
        ServiceInstance instance = list.get(0);
        String host = instance.getHost();
        int port = instance.getPort();
        StringBuilder sb = new StringBuilder();
        // 拼接url地址
        sb.append("http://")
                .append(host)
                .append(":")
                .append(port)
                .append("/hello");
        // 一行代码实现http调用
        return restTemplateOne.getForObject(sb.toString(), String.class);
    }

    int count = 0;
    // 手动实现负载均衡
    @GetMapping("/hello3")
    public String hello3() {
        // 根据服务名从Eureka Server上查询到一个服务的信息
        // discoveryClient查询到的可能是一个集合，因为可能是集群化部署
        List<ServiceInstance> list = discoveryClient.getInstances("provider");
        ServiceInstance instance = list.get((count++) % list.size());
        String host = instance.getHost();
        int port = instance.getPort();
        StringBuilder sb = new StringBuilder();
        // 拼接url地址
        sb.append("http://")
                .append(host)
                .append(":")
                .append(port)
                .append("/hello");
        HttpURLConnection con = null;
        try{
            URL url = new URL(sb.toString());
            con = (HttpURLConnection) url.openConnection();
            if (con.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String s = br.readLine();
                br.close();
                return s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

    // 一行代码负载均衡
    @Autowired
    @Qualifier("restTemplate")
    RestTemplate restTemplate;

    @GetMapping("/hello4")
    public String hello4() {
        return restTemplate.getForObject("http://provider/hello", String.class);
    }

    /*测试restTemplate的get请求
    * 主要就两大类方法
    * 1. getForObject
    * 2. getForEntity
    * */
    @GetMapping("/hello5")
    public void hello5() {
        String s1 = restTemplate.getForObject("http://provider/hello2?name={1}", String.class, "ISheep");
        System.out.println(s1);
        // 除了包含服务端返回的结果还有一些http相应的数据
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://provider/hello2?name={1}",
                String.class,
                "ISheep");
        // 服务端返回的具体数据
        String body = responseEntity.getBody();
        System.out.println(body);
        // 状态码
        HttpStatus statusCode = responseEntity.getStatusCode();
        System.out.println(statusCode);
        // 状态码
        int code = responseEntity.getStatusCodeValue();
        System.out.println(code);
        // 响应头
        HttpHeaders headers = responseEntity.getHeaders();
        Set<String> keySet = headers.keySet();
        System.out.println("---------header----------");
        for (String s : keySet) {
            System.out.println(s + ":" +headers.get(s));
        }

    }

    // 测试三种不同的传参方式
    @GetMapping("/hello6")
    public void hello6() throws UnsupportedEncodingException {
        String s1 = restTemplate.getForObject("http://provider/hello2?name={1}", String.class, "ISheep");
        System.out.println(s1);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "codelin");
        String s2 = restTemplate.getForObject("http://provider/hello2?name={name}", String.class, map);
        System.out.println(s2);
        // 中文需要转码
        String url = "http://provider/hello2?name=" + URLEncoder.encode("张三", "UTF-8");
        URI uri = URI.create(url);
        String s = restTemplate.getForObject(uri, String.class);
        System.out.println(s);

    }

    @GetMapping("/hello7")
    public void hello7() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("username", "codelin");
        map.add("password", "123456");
        map.add("id", 99);
        // 参数已kv形式传递,服务端返回的也是User对象
        User user = restTemplate.postForObject("http://provider/user1", map, User.class);
        System.out.println(user);

        user.setId(98);
        user = restTemplate.postForObject("http://provider/user2", user, User.class);
        System.out.println(user);

    }

}
