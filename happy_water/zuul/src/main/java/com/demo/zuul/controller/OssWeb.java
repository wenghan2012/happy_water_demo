package com.demo.zuul.controller;


import com.demo.zuul.accessfilter.BackstageFilter;
import com.demo.zuul.entity.OssUtil;
import com.demo.zuul.entity.Result;
import com.demo.zuul.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 11:08 2018/10/17
 * @Modified By:
 */
@RestController
public class OssWeb {

    private static final String key = "Happywater";

    private static Logger logger = LoggerFactory.getLogger(BackstageFilter.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping(value = "/image")
    public Result postImage(@RequestParam("file") MultipartFile file,
                            HttpServletRequest request,
                            @RequestParam(required = true)
                                        Integer mode) throws IOException {

        Boolean flag=false;

        String jwt=request.getHeader("JWT");

        if(jwt==null){
            logger.info("无Token");
            return new Result(false,"请求中无JWT");
        }

        Map jwtMap=JwtUtil.parseJWT(jwt,key);

        //JWT验证：取jwt中的值与密钥重创jwt，对比验证
        if(!JwtUtil.createJWT(jwtMap,key).equals(jwt)){
            //不对应表示：请求头中的JWT不可信
            logger.info("JWT不可信");
            return new Result(false,"JWT不可信");
        }

        //获取后台JWT中的ID
        Integer id=(Integer)jwtMap.get("backstageId");
        logger.info("当前操作者的id: "+id);
        //从redis中获取对应ID的JWT
        String redisJwt=(String) redisTemplate.opsForValue().get("backstageId"+id+"");
        logger.info("redisJwt: "+redisJwt);

        //redis中无对应的jwt表明，已注销
        if(redisJwt==null){
            logger.info("已注销，请重新登录");
            return new Result(false,"jwt已注销");
        }

        //用户的jwt与redis中的jwt不相同，表明请求头中的JWT已失效,通过此步代表登录成功
        if(!jwt.equals(redisJwt)){
            logger.info("jwt已过期");
            return new Result(false,"jwt已过期");
        }

        //获取当前账户jwt中的权限集合
        List<String> authority= (List) jwtMap.get("authority");

        String permission=null;

        if(mode==1){
            permission="recommendation";
        }else {
            permission="bannere";
        }

        logger.info("访问该接口需要的权限: "+permission);

        //遍历权限，查看是否存在接口所需的权限
        if(authority.size()!=0) {
            for (String s : authority) {
                logger.info("用户具有的权限： " + s);
                if (s.equals(permission)) {
                    flag=true;
                    logger.info("具有权限");
                }
            }
        }

        if(!flag){
            return new Result(false,"不具备访问权限");
        }

        System.out.println(jwt);
        //判断是否有文件上传
        if(file==null) {
            new Result(false,"没有文件上传");
        }
        //获取文件流
        FileInputStream inputStream = (FileInputStream) file.getInputStream();
        //获取文件尾缀
        String surround = file.getContentType();
        //判断文件是否为图片
        if(!"image/png".equals(surround)&&!"image/jpg".equals(surround)&&
                !"image/gif".equals(surround)){
            new Result(false,"上传的文件不是图片");
        }
        String[] surrounds=surround.split("/");
        //通过UUID创建文件名与文件尾缀拼接
        String objectName= UUID.randomUUID()+"."+surrounds[1];
        //调用阿里云OSS上传接口，获取上传路径
        String url = OssUtil.uploadFile(inputStream,objectName);

        //返回给前端，key与url
        Map map=new HashMap();
        map.put("key",objectName);
        map.put("url",url);
        return new Result<>(true,map);
    }
}
