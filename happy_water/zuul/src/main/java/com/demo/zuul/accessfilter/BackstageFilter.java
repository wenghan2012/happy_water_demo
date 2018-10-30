package com.demo.zuul.accessfilter;

import com.demo.zuul.utils.JwtUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.RedisTemplate;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 9:45 2018/9/29
 * @Modified By:
 */
public class BackstageFilter extends ZuulFilter {

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
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request=context.getRequest();
        //获取url的路径
        String path=request.getRequestURI();
        String[] uriList=path.split("/");

        //如果是前台路径直接放行
        if("u".equals(uriList[1])){
            return false;
        }

        //如果是后台登录的路径则直接放行
        if("login".equals(uriList[3])){
            logger.info("后台登陆接口");
            return false;
        }

        //如果是后台接口(除后台登陆外)，则进入拦截器
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context=RequestContext.getCurrentContext();
        HttpServletRequest request=context.getRequest();
        //获取请求路径
        String path=request.getRequestURI();
        //分割路径,获取uri
        String[] uriList=path.split("/");

        //从Header中获取jwt
        String jwt=request.getHeader("JWT");
        logger.info("jwt: "+jwt);

        //请求头中不存在JWT
        if(jwt==null){
            logger.info("无Token");
            context.put(FilterConstants.REQUEST_URI_KEY,"/backstage/notJwt");
            return null;
        }

        //解密JWT，保存到map对象中
        Map map=JwtUtil.parseJWT(jwt,key);

        //JWT验证：取jwt中的值与密钥重创jwt，对比验证
        if(!JwtUtil.createJWT(map,key).equals(jwt)){
            //不对应表示：请求头中的JWT不可信
            logger.info("JWT不可信");
            context.put(FilterConstants.REQUEST_URI_KEY,"/backstage/UntrustworthyJwt");
            return null;
        }

        //获取后台JWT中的ID
        Integer id=(Integer)map.get("backstageId");
        logger.info("当前操作者的id: "+id);
        //从redis中获取对应ID的JWT
        String redisJwt=(String) redisTemplate.opsForValue().get("backstageId"+id);
        logger.info("redisJwt: "+redisJwt);

        //redis中无对应的jwt表明，已注销
        if(redisJwt==null){
            logger.info("已注销，请重新登录");
            context.put(FilterConstants.REQUEST_URI_KEY,"/backstage/notCorrespondingJwt");
            return null;
        }

        //用户的jwt与redis中的jwt不相同，表明请求头中的JWT已失效,通过此步代表登录成功
        if(!jwt.equals(redisJwt)){
            logger.info("jwt已过期");
            context.put(FilterConstants.REQUEST_URI_KEY,"/backstage/invalidJwt");
            return null;
        }

        //如果是注销操作或获取左边栏操作则直接放行(不需要权限验证)
        if("logout".equals(uriList[3])||"modules".equals(uriList[3])){
            return null;
        }

        //获取权限URI
        int size=uriList.length;
        String permission;
        //判断最后一个url是否为纯数字
        if(StringUtils.isNumeric(uriList[size-1])){
            permission=uriList[size-3];
        }else{
            permission=uriList[size-2];
        }

        logger.info("访问该接口需要的权限: "+permission);

        //获取当前账户jwt中的权限集合
        List<String> authority= (List) map.get("authority");

        logger.info("用户具有的权限数量为： "+authority.size());
        //遍历权限，查看是否存在接口所需的权限
        if(authority.size()!=0) {
            for (String s : authority) {
                logger.info("用户具有的权限： " + s);
                if (s.equals(permission)) {
                    logger.info("具有权限");
                    //如果有匹配的权限，放行
                    return null;
                }
            }
        }

        //设置没需要的权限，要跳转的路径
        context.put(FilterConstants.REQUEST_URI_KEY,"/backstage/notAuthority");
        return null;
    }
}
