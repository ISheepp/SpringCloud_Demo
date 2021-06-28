package xyz.codelin.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import xyz.codelin.api.IUserService;

/**
 * FeignClient
 * @author linzy
 * @create 2020-12-27 15:38:51
 */
// 跟provider绑定
@FeignClient(value = "provider", fallbackFactory = HelloServiceFallbackFactory.class)
public interface HelloService extends IUserService {
    // 自动具备了IUserService中的东西
}