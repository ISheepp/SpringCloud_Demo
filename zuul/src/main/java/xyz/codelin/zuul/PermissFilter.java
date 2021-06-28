package xyz.codelin.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author linzy
 * @create 2021-01-02 15:44:39
 */
@Component
public class PermissFilter extends ZuulFilter {

    /**
     * 是否过滤
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器
     * 这里做核心的过滤逻辑
     * @return 这个方法虽然有返回值，但是这个返回值无所谓
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest(); // 获取当前请求
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // 满足的话直接往下走
        // 如果请求条件不满足的话，直接从这里给出响应
        if (!"codelin".equals(username) || !"123".equals(password)) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            ctx.addZuulResponseHeader("content-type", "text/html;charset=utf-8");
            ctx.setResponseBody("非法访问");
        }
        return null;
    }

    /**
     * 过滤器的类型，权限判断一般是pre
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器优先级
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }
}
