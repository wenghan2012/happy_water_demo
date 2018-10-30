package com.demo.zuul.accessfilter;

import com.demo.zuul.utils.JwtUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.RedisTemplate;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 13:09 2018/9/30
 * @Modified By:
 */
public class ReceptionFilter extends ZuulFilter {
    private static final String key = "Happywater";

    private static Logger logger = LoggerFactory.getLogger(BackstageFilter.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request=context.getRequest();
        //获取url的路径
        String path=request.getRequestURI();
        //对url进行分割
        String[] uriList=path.split("/");

        //如果是后台路径则放行
        if("a".equals(uriList[1])){
            return false;
        }
        //如果是不需要登录的路径则放行
        if("o".equals(uriList[3])){
            logger.info("不需要登录的接口");
            return false;
        }

        //其余的接口是：前台需要登录才能访问的接口，进入run过滤器
        logger.info("需要登录的接口");
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context=RequestContext.getCurrentContext();
        HttpServletRequest request=context.getRequest();
        //获取路径
        String path=request.getRequestURI();
        //分割路径
        String[] uriList=path.split("/");

        //获取请求头中的JWT
        String jwt=request.getHeader("JWT");
        logger.info("jwt: "+jwt);

        //请求头中不存在JWT：未登录
        if(jwt==null){
            logger.info("无Token");
            context.put(FilterConstants.REQUEST_URI_KEY,"/reception/notJwt");
            return null;
        }

        //使用Map获取jwt中的值
        Map map=JwtUtil.parseJWT(jwt,key);

        //JWT验证：取jwt中的值与密钥重创jwt，对比验证
        if(!JwtUtil.createJWT(map,key).equals(jwt)){
            logger.info("JWT不可信");
            context.put(FilterConstants.REQUEST_URI_KEY,"/reception/UntrustworthyJwt");
            return null;
        }

        //从jwt中获取前台id：前台使用receptionId作为id的key
        Integer id=(Integer)map.get("receptionId");
        //通过前台id在redis从库中获取redisJwt
        String redisJwt=(String) redisTemplate.opsForValue().get("receptionId"+id);
        logger.info("redisJwt: "+redisJwt);

        //如果redis中不存在对应的JWT，表明已注销
        if(redisJwt==null){
            logger.info("已注销，请重新登录");
            context.put(FilterConstants.REQUEST_URI_KEY,"/reception/notCorrespondingJwt");
            return null;
        }

        //用户的jwt与redis中的jwt不相同,则表明请求头中的jwt已失效
        if(!jwt.equals(redisJwt)){
            logger.info("jwt已过期");
            context.put(FilterConstants.REQUEST_URI_KEY,"/reception/invalidJwt");
            return null;
        }

        //其余情况放行
        return null;
    }

}
