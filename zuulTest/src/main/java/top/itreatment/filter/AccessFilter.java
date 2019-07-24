package top.itreatment.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class AccessFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(AccessFilter.class);

//    过滤类型，代表了过滤的时期，pre表示在路由转发之前进行过滤。
    @Override
    public String filterType() {
        return "pre";
    }

//    过滤的顺序，如果在一个时期有多个过滤器，
//    这他们的顺序就是根据该方法返回的值进行排序
    @Override
    public int filterOrder() {
        return 0;
    }

//    是否启动过滤功能，可以通过该方法进行确定范围
    @Override
    public boolean shouldFilter() {
        return true;
    }

//    过滤的主要的过滤逻辑
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();

//        获取请求实例request
        HttpServletRequest request = ctx.getRequest();
        log.info("send {} request to{}", request.getMethod(),
                request.getRequestURL().toString());

//        通过request获取发送的参数
        Object accessToken = request.getParameter("accessToken");

        if (accessToken == null) {
            log.warn("access token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseBody("this is a test !!!");
            ctx.setResponseStatusCode(401);
        } else {
            log.info("access token ok");
        }
        return null;
    }
}
